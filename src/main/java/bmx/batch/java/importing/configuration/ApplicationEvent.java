package bmx.batch.java.importing.configuration;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobRepository;
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
    private final JobRepository jobRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void applicationEvent() throws JobExecutionException {
        log.info("Event started..");
        try {
            cleanupStuckExecutions(importerJob.getName());

            // Create unique job parameters based on time or other logic
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()) // Add a unique parameter
                    .toJobParameters();

            // Check if a job is already running
            /*if (isJobRunning(importerJob)) {
                log.error("Job is already running.");
                return; // Don't start the job again
            }*/

            // Launch the job with unique parameters
            jobLauncher.run(importerJob, jobParameters);
            log.info("Job launched successfully.");

        } catch (JobExecutionAlreadyRunningException e) {
            log.error("Job execution already running", e);
        } catch (Exception e) {
            log.error("Error launching job", e);
        }
    }

   /* private boolean isJobRunning(Job job) {
        // Query the JobExplorer to find all job executions for the job
        Set<JobExecution> jobExecutions = jobExplorer.findRunningJobExecutions(job.getName());

        // Check if there is any running job execution
        return !jobExecutions.isEmpty();
    }*/

    private void cleanupStuckExecutions(String jobName) {
        Set<JobExecution> runningJobs = jobExplorer.findRunningJobExecutions(jobName);
        for (JobExecution exec : runningJobs) {
            log.warn("Found stuck execution (ID: {}), forcing it to FAILED", exec.getId());

            exec.setStatus(BatchStatus.FAILED);
            exec.setEndTime(LocalDateTime.now());

            exec.getStepExecutions().forEach(step -> {
                step.setStatus(BatchStatus.FAILED);
                step.setEndTime(LocalDateTime.now());
                jobRepository.update(step);
            });

            jobRepository.update(exec);
        }
    }


}
