package bmx.batch.java.importing.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ImporterJobListener Unit Tests")
class ImporterJobListenerTest {

    @InjectMocks
    private ImporterJobListener listener;

    private JobExecution jobExecution;
    private JobInstance jobInstance;

    @BeforeEach
    void setUp() {
        jobInstance = new JobInstance(1L, "importerJob");
        jobExecution = new JobExecution(jobInstance, new JobParameters());
    }

    @Test
    @DisplayName("Should log job start in beforeJob")
    void testBeforeJob() {
        // Act
        listener.beforeJob(jobExecution);

        // Assert
        assertEquals("importerJob", jobExecution.getJobInstance().getJobName());
        assertNotNull(jobExecution.getJobInstance());
    }

    @Test
    @DisplayName("Should log success message when job completes successfully")
    void testAfterJobCompleted() {
        // Arrange
        jobExecution.setStatus(BatchStatus.COMPLETED);

        // Act
        listener.afterJob(jobExecution);

        // Assert
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        assertEquals("importerJob", jobExecution.getJobInstance().getJobName());
    }

    @Test
    @DisplayName("Should log error message when job fails")
    void testAfterJobFailed() {
        // Arrange
        jobExecution.setStatus(BatchStatus.FAILED);
        jobExecution.setEndTime(LocalDateTime.now());

        // Act
        listener.afterJob(jobExecution);

        // Assert
        assertEquals(BatchStatus.FAILED, jobExecution.getStatus());
        assertNotNull(jobExecution.getEndTime());
        assertEquals("importerJob", jobExecution.getJobInstance().getJobName());
    }

    @Test
    @DisplayName("Should handle STOPPED job status")
    void testAfterJobStopped() {
        // Arrange
        jobExecution.setStatus(BatchStatus.STOPPED);
        jobExecution.setEndTime(LocalDateTime.now());

        // Act
        listener.afterJob(jobExecution);

        // Assert
        assertEquals(BatchStatus.STOPPED, jobExecution.getStatus());
        assertNotEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }

    @Test
    @DisplayName("Should handle ABANDONED job status")
    void testAfterJobAbandoned() {
        // Arrange
        jobExecution.setStatus(BatchStatus.ABANDONED);
        jobExecution.setEndTime(LocalDateTime.now());

        // Act
        listener.afterJob(jobExecution);

        // Assert
        assertEquals(BatchStatus.ABANDONED, jobExecution.getStatus());
        assertNotEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }
}
