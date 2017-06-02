package com.capgemini.map.horizon.utils.logger;

public class Logger {

	private org.slf4j.Logger sl4jLogger;

	public Logger(org.slf4j.Logger logger) {
		this.sl4jLogger = logger;
	}

	public String getName() {
		return sl4jLogger.getName();
	}

	public boolean isDebugEnabled() {
		return sl4jLogger.isDebugEnabled();
	}

	public void debug(String string) {
		sl4jLogger.debug(string);
	}

	public void debug(String string, Object... o) {
		sl4jLogger.debug(string, o);
	}

	public void debug(String string, Throwable thrwbl) {
		sl4jLogger.debug(string, thrwbl);
	}

	public void warn(String string) {
		sl4jLogger.warn(string);
	}

	public void warn(String string, Object... o) {
		sl4jLogger.warn(string, o);
	}

	public void warn(String string, Throwable thrwbl) {
		sl4jLogger.warn(string, thrwbl);
	}

	public void error(String string) {
		sl4jLogger.warn(string);
	}

	public void error(String string, Object... o) {
		sl4jLogger.error(string, o);
	}

	public void error(String string, Throwable thrwbl) {
		sl4jLogger.error(string, thrwbl);
	}

	public void info(String string) {
		sl4jLogger.info(string);
	}

	public void info(String string, Object... o) {
		sl4jLogger.info(string, o);
	}

	public boolean isTraceEnabled() {
		return sl4jLogger.isTraceEnabled();
	}

	public void trace(String string) {
		sl4jLogger.trace(string);
	}

	public void trace(String string, Object... o) {
		sl4jLogger.trace(string, o);
	}
	
}
