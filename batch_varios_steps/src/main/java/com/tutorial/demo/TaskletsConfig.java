package com.tutorial.demo;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.tutorial.demo.steps.LinesProcessor;
import com.tutorial.demo.steps.LinesReader;
import com.tutorial.demo.steps.LinesWriter;

@Configuration
public class TaskletsConfig {
	
	@Bean
	public LinesReader linesReader() {
		return new LinesReader();
	}
	
	@Bean
	public LinesProcessor linesProcessor() {
		return new LinesProcessor();
	}
	
	@Bean
	public LinesWriter linesWriter() {
		return new LinesWriter();
	}

	@Bean
	protected Step readLines(JobRepository jobRepository, PlatformTransactionManager manager) {
		return new StepBuilder("readLines", jobRepository)
				.tasklet(linesReader(), manager)
				.build();
	}
	
	@Bean
	protected Step processLines(JobRepository jobRepository, PlatformTransactionManager manager) {
		return new StepBuilder("processLines", jobRepository)
				.tasklet(linesProcessor(), manager)
				.build();
	}
	
	@Bean
	protected Step writeLines(JobRepository jobRepository, PlatformTransactionManager manager) {
		return new StepBuilder("writeLines", jobRepository)
				.tasklet(linesWriter(), manager)
				.build();
	}
	
	@Bean
	public Job job(JobRepository jobRepository, PlatformTransactionManager manager) {
		return new JobBuilder("taskletsJob", jobRepository)
				.start(readLines(jobRepository, manager))
				.next(processLines(jobRepository, manager))
				.next(writeLines(jobRepository, manager))
				.build();		
	}
	
	
	@Bean
	public JobRepository jobRepository() throws Exception {
	    JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
	    factory.setDataSource(dataSource());
	    factory.setTransactionManager(transactionManager());
	    return factory.getObject();
	}

	@Bean
	public DataSource dataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName("org.sqlite.JDBC");
	    dataSource.setUrl("jdbc:sqlite:repository.sqlite");
	    return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
	    return new ResourcelessTransactionManager();
	}

	@Bean
	public JobLauncher jobLauncher() throws Exception {
	    TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
	    jobLauncher.setJobRepository(jobRepository());
	    jobLauncher.afterPropertiesSet();
	    return jobLauncher;
	}
}
