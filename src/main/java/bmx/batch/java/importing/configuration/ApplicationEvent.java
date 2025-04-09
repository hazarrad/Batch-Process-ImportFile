package bmx.batch.java.importing.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationEvent {

	private final JobLauncher jobLauncher;
	private final Job importerJob;

	@EventListener(ApplicationReadyEvent.class)
	public void applicationEvent() throws JobExecutionException {
		jobLauncher.run(importerJob, new JobParameters());

	}
}