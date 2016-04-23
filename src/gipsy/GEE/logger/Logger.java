package gipsy.GEE.logger;

/**
 * 
 * This class implements ILogger to store error messages
 *
 */

public class Logger implements ILogger {


	//Level - info
	public void info(String message)
	{
		
		 String finalMsg="[INFO]: " + " "+message;
		 System.out.println(finalMsg);
		 LoggerClient cl = new LoggerClient();
	     cl.clientConnection(finalMsg);
		
		
	}
	
	//Level - error
	public void error(String message)
	{
		
		 String finalMsg="[ERROR]: " + " "+message;
		 System.out.println(finalMsg);
		 LoggerClient cl = new LoggerClient();
	     cl.clientConnection(finalMsg);
		
	}
	
	//Level - debug
	public void debug(String message)
	{
		
		 String finalMsg="[DEBUG]: " + " "+message;
		 System.out.println(finalMsg);
		 LoggerClient cl = new LoggerClient();
	     cl.clientConnection(finalMsg);
		
		
	}

	

}
