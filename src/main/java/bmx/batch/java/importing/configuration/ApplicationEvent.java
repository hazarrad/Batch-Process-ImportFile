package bmx.batch.java.importing.configuration;
import java.util.Set;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationEvent {

    private final JobLauncher jobLauncher;
    private final Job importerJob;
    private final JobExplorer jobExplorer;

    @EventListener(ApplicationReadyEvent.class)
    public void applicationEvent() throws JobExecutionException {
        try {
            // Create unique job parameters based on time or other logic
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()) // Add a unique parameter
                    .toJobParameters();

            // Check if a job is already running
            if (isJobRunning(importerJob)) {
                log.error("Job is already running.");
                return; // Don't start the job again
            }

            // Launch the job with unique parameters
            jobLauncher.run(importerJob, jobParameters);
            log.info("Job launched successfully.");

        } catch (JobExecutionAlreadyRunningException e) {
            log.error("Job execution already running", e);
        } catch (Exception e) {
            log.error("Error launching job", e);
        }
    }

    private boolean isJobRunning(Job job) {
        // Query the JobExplorer to find all job executions for the job
        Set<JobExecution> jobExecutions = jobExplorer.findRunningJobExecutions(job.getName());

        // Check if there is any running job execution
        return !jobExecutions.isEmpty();
    }
}
