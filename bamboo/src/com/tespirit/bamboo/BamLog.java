package com.tespirit.bamboo;

public class BamLog {
	private static Logger mLogger = new DefaultLogger();
	
	public interface Logger {
		void debug(String message);
		void error(String message);
		void error(Exception exception);
		void warning(String message);
		void info(String message);
	}
	
	private static class DefaultLogger implements Logger{
		@Override
		public void debug(String message) {
			System.out.println("[DEBUG] "+message);
		}

		@Override
		public void error(String message) {
			System.err.println("[ERROR] "+message);
		}

		@Override
		public void error(Exception exception) {
			this.error("An exception has occured:");
			exception.printStackTrace();
		}

		@Override
		public void warning(String message) {
			System.out.println("[WARNING] "+message);
		}

		@Override
		public void info(String message) {
			System.out.println("[INFO] "+message);
		};
	}
	
	public static void setLogger(Logger logger){
		mLogger = logger;
	}
	
	public static void debug(String message){
		mLogger.debug(message);
	}
	
	public static void error(String message){
		mLogger.error(message);
	}
	
	public static void error(Exception exception){
		mLogger.error(exception);
	}
	
	public static void warning(String message){
		mLogger.warning(message);
	}
	
	public static void info(String message){
		mLogger.info(message);
	}
}
