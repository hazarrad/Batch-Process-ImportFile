package bmx.batch.java.importing.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import bmx.batch.java.importing.listener.ImporterJobListener;
import bmx.batch.java.importing.listener.MyItemWriteListener;
import bmx.batch.java.importing.listener.MySkipListener;
import bmx.batch.java.importing.model.Customer;
import bmx.batch.java.importing.processor.MyItemProcessor;
import bmx.batch.java.importing.utils.Utils;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

//@EnableConfigurationProperties(BatchConfigProperties.class)
@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

	@Bean
	public FlatFileItemReader<Customer> reader() {

		return new FlatFileItemReaderBuilder<Customer>()
				.name("importerFileReader")
				.linesToSkip(1)
 				.resource(new ClassPathResource("customers.csv"))
				.delimited()
				.delimiter(",")
				.names("index", "customerId", "firstName", "lastName", "company",
                        "city", "country", "phone1",
                        "phone2", "email", "subscriptionDate")
				.fieldSetMapper(fieldSet -> {
                    return Customer.builder()
                            .customerId(fieldSet.readString("customerId"))
                            .firstName(fieldSet.readString("firstName"))
                            .lastName(fieldSet.readString("lastName"))
                            .company(fieldSet.readString("company"))
                            .city(fieldSet.readString("city"))
                            .country(fieldSet.readString("country"))
                            .phone1(fieldSet.readString("phone1"))
                            .phone2(fieldSet.readString("phone2"))
                            .email(fieldSet.readString("email"))
                            .subscriptionDate(Utils.parseDate(fieldSet.readString("subscriptionDate")))
                            .build();
                })
				.build();	
	}

	
    @Bean
    public JpaItemWriter<Customer> writer(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<Customer>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(false) // Avoid merge

                .build();
    }
    
	@Bean
	public MySkipListener mySkipListener() {
		return new MySkipListener();
	}
    
    @Bean
    public Step importerStep(ItemReader<Customer> reader, ItemWriter<Customer> writer,
    		JobRepository jobRepository, PlatformTransactionManager transactionManager,MyItemProcessor processor) {
        log.info("Creating importerStep...");

        return new StepBuilder("importerStep", jobRepository)
                .<Customer, Customer>chunk(200, transactionManager)
                .reader(reader)
                .writer(writer)
                .processor(processor)
                .listener(mySkipListener()) 
                .listener(new MyItemWriteListener())
                .allowStartIfComplete(true)
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skipLimit(1)
                .build();
    }
    
    @Bean
    public Job importerJob(Step importerStep, JobRepository jobRepository, ImporterJobListener listener) {
        return new JobBuilder("importerJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(importerStep)
                .end()
                .build();
    }

}
