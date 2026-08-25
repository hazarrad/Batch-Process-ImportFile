package bmx.batch.java.importing.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import bmx.batch.java.importing.configuration.BMXConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GoogleCloudStorageService {

	@Autowired
	private BMXConfig bmxConfig;

	private Storage storage;
	private Bucket bucket;

	@PostConstruct
	public void  init() {
		try {
			log.info("Initializing GCS with bucket: {}", bmxConfig.getBucketName());
			Path resourcesPath = Paths.get(bmxConfig.getResourcesFolder());
			log.info("Resources Path: {}", resourcesPath);

			String serviceAccountKeyFile = resourcesPath + bmxConfig.getKey();
			log.info("----------> service account key file : {}", serviceAccountKeyFile);

			GoogleCredentials credentials = GoogleCredentials.fromStream(
					new FileInputStream(new ClassPathResource(bmxConfig.getKey()).getFile().getAbsolutePath()));

			// Create a Storage client with the specified credentials
			log.info("init storage..");
			storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
			bucket = storage.get(bmxConfig.getBucketName());

			if (bucket == null) {
				throw new IllegalStateException("Unable to access bucket: " + bmxConfig.getBucketName());
			}

			log.info("GCS initialization completed.");
		} catch (IOException e) {
			log.error("Failed to load service account key file: {} ", e.getMessage());
			throw new RuntimeException("Failed to initialize GCS connection: " + e.getMessage(), e);
		}
	}

	public InputStreamResource loadFileAsResource() {
		log.info("Loading file '{}' from bucket '{}'", bmxConfig.getFileName(), bmxConfig.getBucketName());
		Blob blob = bucket.get(bmxConfig.getFilePath() + bmxConfig.getFileName());
		log.info("File loaded..");

		if (blob == null || !blob.exists()) {
			log.error("Failed to load file: {} ", bmxConfig.getFileName());
			throw new IllegalArgumentException("File not found in GCS bucket: " + bmxConfig.getFileName());
		}

		InputStream inputStream = Channels.newInputStream(blob.reader());
		return new InputStreamResource(inputStream);
	}
}
