package com.capgemini.map.horizon.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;

import com.capgemini.map.horizon.batch.LocationDataWriter;
import com.capgemini.map.horizon.batch.LocationFieldSetMapper;
import com.capgemini.map.horizon.batch.LocationItemProcessor;
import com.capgemini.map.horizon.model.Details;

/**
 * Job to extract data from file and insert to DB
 * 
 * @author adeshpa1
 *
 */
@Configuration
@EnableBatchProcessing
@Import({FindConfig.class})
public class ExtractLocationDataJob {

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job addNewLocationJob(){
		return jobs.get("readAndSaveLocationDetails")
				.start(step())
				.build();
	}
	
	@Bean
	public Step step(){
		return stepBuilderFactory.get("step")
				.<Details,Details>chunk(1) //important to be one in this case to commit after every line read
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.faultTolerant()
				.build();
	}
	
	@Bean
	public ItemReader<Details> reader(){
		FlatFileItemReader<Details> reader = new FlatFileItemReader<Details>();
		reader.setLinesToSkip(1);//first line is title definition
		reader.setResource(new FileSystemResource("src/main/resources/CA_2000_States.csv"));
		reader.setLineMapper(lineMapper());
		return reader;
	}
	
	@Bean
	public LineMapper<Details> lineMapper() {
		DefaultLineMapper<Details> lineMapper = new DefaultLineMapper<Details>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[]{"id", "name", "address", "city", "county", "state", "zip", "plan name", "specialty", "type"});

		BeanWrapperFieldSetMapper<Details> fieldSetMapper = new BeanWrapperFieldSetMapper<Details>();
		fieldSetMapper.setTargetType(Details.class);

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(locationFieldSetMapper());

		return lineMapper;
	}
	
	@Bean
	public LocationFieldSetMapper locationFieldSetMapper() {
		return new LocationFieldSetMapper();
	}
	
	 @Bean
	 public ItemProcessor<Details, Details> processor() {
	      return new LocationItemProcessor();
	 }

	 @Bean
	 public ItemWriter<Details> writer() {
	  	return new LocationDataWriter();
	 }
}
