package com.capgemini.map.horizon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
@EnableMongoRepositories(
		basePackages = "com.capgemini.map.horizon.repository",
		mongoTemplateRef = "primaryMongoTemplate"
		)
public class FindConfig{  

	@Value("${primary.mongodb.host}")
	private String host;
	
	@Value("${primary.mongodb.database}")
	private String database;

	@Value("${primary.mongodb.port}")
	private int port;     


	/**      
	 * Implementation of the MongoTemplate factory method      
	 * @Bean gives a name (primaryMongoTemplate) to the created MongoTemplate instance      
	 * @Primary declares that if MongoTemplate is autowired without providing a specific name, 
	 * this is the instance which will be mapped by         default      
	 */    
	@Primary    
	@Bean(name = "primaryMongoTemplate") 
	public MongoTemplate getMongoTemplate() throws Exception {        
		return new MongoTemplate(mongoDbFactory(),database);    
	}

	/*      
	 * Method that creates MongoDbFactory     
	 * Common to both of the MongoDb connections     
	 */    
	public Mongo mongoDbFactory() throws Exception { 
		MongoClientURI uri = new MongoClientURI("mongodb://vamsi:vamsi@ds153659.mlab.com:53659/dtc");
		return new MongoClient(uri);    
	} 
}
