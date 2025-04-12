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
@ConfigurationProperties(prefix = "batch")
public class BatchConfigProperties {

	// Batch Properties
	private int chunksize;
	private int skipLimit;
	private int corepool;
	private int maxpool;
	private int queuelimit;

}