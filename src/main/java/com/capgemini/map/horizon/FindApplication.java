package com.capgemini.map.horizon;

import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


import com.capgemini.map.horizon.utils.logger.AbstractLoggerFactory;

@SpringBootApplication
public class FindApplication {
	
	private static final com.capgemini.map.horizon.utils.logger.Logger LOGGER = AbstractLoggerFactory.getLogger(FindApplication.class); 
	
	public static void main(String[] args) throws BeansException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		ConfigurableApplicationContext ctx = SpringApplication.run(FindApplication.class, args);
		
		LOGGER.info("Inside Logger");
		
		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);

		jobLauncher.run(ctx.getBean(Job.class), new JobParameters()); 
	}
	
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

}
