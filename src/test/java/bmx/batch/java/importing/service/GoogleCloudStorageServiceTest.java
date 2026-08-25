package bmx.batch.java.importing.service;

import bmx.batch.java.importing.configuration.BMXConfig;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GoogleCloudStorageService Unit Tests")
class GoogleCloudStorageServiceTest {

    @Mock
    private BMXConfig bmxConfig;

    @Mock
    private Storage storage;

    @Mock
    private Bucket bucket;

    @Mock
    private Blob blob;

    @Mock
    private ReadChannel readChannel;

    @InjectMocks
    private GoogleCloudStorageService gcsService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(gcsService, "storage", storage);
        ReflectionTestUtils.setField(gcsService, "bucket", bucket);
    }

    @Test
    @DisplayName("Should successfully load file as resource from GCS")
    void testLoadFileAsResourceSuccess() {
        String fileName = "customers.csv";
        String filePath = "imports/";
        when(bmxConfig.getFileName()).thenReturn(fileName);
        when(bmxConfig.getFilePath()).thenReturn(filePath);
        when(bucket.get(filePath + fileName)).thenReturn(blob);
        when(blob.exists()).thenReturn(true);
        when(blob.reader()).thenReturn(readChannel);

        InputStreamResource result = gcsService.loadFileAsResource();

        assertNotNull(result);
        verify(bucket).get(filePath + fileName);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when file does not exist")
    void testLoadFileAsResourceFileNotFound() {
        String fileName = "nonexistent.csv";
        String filePath = "imports/";
        when(bmxConfig.getFileName()).thenReturn(fileName);
        when(bmxConfig.getFilePath()).thenReturn(filePath);
        when(bucket.get(filePath + fileName)).thenReturn(blob);
        when(blob.exists()).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gcsService.loadFileAsResource()
        );

        assertTrue(exception.getMessage().contains("File not found in GCS bucket"));
        verify(blob, never()).reader();
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when blob is null")
    void testLoadFileAsResourceBlobNull() {
        String fileName = "missing.csv";
        String filePath = "imports/";
        when(bmxConfig.getFileName()).thenReturn(fileName);
        when(bmxConfig.getFilePath()).thenReturn(filePath);
        when(bucket.get(filePath + fileName)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> gcsService.loadFileAsResource());
        verify(bucket).get(filePath + fileName);
    }

    @Test
    @DisplayName("Should load file from correct path")
    void testLoadFileAsResourceCorrectPath() {
        String fileName = "data.csv";
        String filePath = "batch/input/";
        when(bmxConfig.getFileName()).thenReturn(fileName);
        when(bmxConfig.getFilePath()).thenReturn(filePath);
        when(bucket.get(filePath + fileName)).thenReturn(blob);
        when(blob.exists()).thenReturn(true);
        when(blob.reader()).thenReturn(readChannel);

        InputStreamResource result = gcsService.loadFileAsResource();

        assertNotNull(result);
        verify(bucket).get(filePath + fileName);
    }

    @Test
    @DisplayName("Should handle file with special characters in name")
    void testLoadFileAsResourceWithSpecialCharacters() {
        String fileName = "customer_data_2024-01.csv";
        String filePath = "imports/special/";
        when(bmxConfig.getFileName()).thenReturn(fileName);
        when(bmxConfig.getFilePath()).thenReturn(filePath);
        when(bucket.get(filePath + fileName)).thenReturn(blob);
        when(blob.exists()).thenReturn(true);
        when(blob.reader()).thenReturn(readChannel);

        InputStreamResource result = gcsService.loadFileAsResource();

        assertNotNull(result);
        verify(bucket).get(filePath + fileName);
    }
}
