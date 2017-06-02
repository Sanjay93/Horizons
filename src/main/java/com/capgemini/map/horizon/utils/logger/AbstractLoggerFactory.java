package com.capgemini.map.horizon.utils.logger;

public abstract class AbstractLoggerFactory {
	
	@SuppressWarnings("rawtypes")
	public static com.capgemini.map.horizon.utils.logger.Logger getLogger(Class clazz){
		
		final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(clazz);
		return new com.capgemini.map.horizon.utils.logger.Logger(logger);
	}
}
