package bmx.batch.java.importing.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "export")
public class ExportConfig {

	private String resourcesFolder;
	private String bucketName;
	private String projectId;
	private String key;

}