package bmx.batch.java.importing.configuration;

import bmx.batch.java.importing.listener.MyChunkListener;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import bmx.batch.java.importing.listener.ImporterJobListener;
import bmx.batch.java.importing.listener.MyItemWriteListener;
import bmx.batch.java.importing.listener.MySkipListener;
import bmx.batch.java.importing.model.Customer;
import bmx.batch.java.importing.processor.MyItemProcessor;
import bmx.batch.java.importing.service.GoogleCloudStorageService;
import bmx.batch.java.importing.utils.Utils;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

    @Autowired
    private BatchConfigProperties bcp;

    @Autowired
    private GoogleCloudStorageService gcsService;

    @Bean
    public FlatFileItemReader<Customer> reader() {

        log.info("reading started..");

        Resource resource = gcsService.loadFileAsResource();
        log.info("resource {}", resource.getFilename());

        return new FlatFileItemReaderBuilder<Customer>().name("importerFileReader").linesToSkip(1)
                .resource(resource).delimited().delimiter(",")
                .names("index", "customerId", "firstName", "lastName", "company", "city", "country", "phone1", "phone2",
                        "email", "subscriptionDate")
                .fieldSetMapper(fieldSet -> {
                    return Customer.builder().customerId(fieldSet.readString("customerId"))
                            .firstName(fieldSet.readString("firstName")).lastName(fieldSet.readString("lastName"))
                            .company(fieldSet.readString("company")).city(fieldSet.readString("city"))
                            .country(fieldSet.readString("country")).phone1(fieldSet.readString("phone1"))
                            .phone2(fieldSet.readString("phone2")).email(fieldSet.readString("email"))
                            .subscriptionDate(Utils.parseDate(fieldSet.readString("subscriptionDate"))).build();
                }).build();
    }

    @Bean
    public JpaItemWriter<Customer> writer(EntityManagerFactory entityManagerFactory) {
        log.info("writing started..");
        return new JpaItemWriterBuilder<Customer>().entityManagerFactory(entityManagerFactory).usePersist(false).build();
    }

    @Bean
    public MySkipListener mySkipListener() {
        return new MySkipListener();
    }
    @Bean
    public MyChunkListener myChunkListener() {
        return new MyChunkListener();
    }

    @Bean
    public Step importerStep(ItemReader<Customer> reader, ItemWriter<Customer> writer, JobRepository jobRepository,
                             PlatformTransactionManager transactionManager, MyItemProcessor processor) {
        log.info("Creating importerStep...");

        return new StepBuilder("importerStep", jobRepository)
                .<Customer, Customer>chunk(bcp.getChunksize(), transactionManager).reader(reader).processor(processor)
                .writer(writer).faultTolerant().skip(IllegalArgumentException.class).skipLimit(bcp.getSkipLimit())
                .listener(mySkipListener()).listener(myChunkListener()).listener(new MyItemWriteListener()).allowStartIfComplete(true).build();
    }

    @Bean
    public Job importerJob(Step importerStep, JobRepository jobRepository, ImporterJobListener listener) {
        log.info("Job started...");
        return new JobBuilder("importerJob", jobRepository).incrementer(new RunIdIncrementer()).listener(listener)
                .flow(importerStep).end().build();
    }

}
