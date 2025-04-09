package bmx.batch.java.importing.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ImporterJobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("Job {} started", jobExecution.getJobInstance().getJobName());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

			log.info("Job completed successfully -> The job completed successfully at"
					+ jobExecution.getJobInstance().getJobName());

		} else {

			log.error("Job failed -> The job failed at " + jobExecution.getEndTime());
			log.info("Job {} failed", jobExecution.getJobInstance().getJobName());
		}
	}
}