package gipsy.GEE.logger;

/**
 * interface declares the methods that describe the levels of error in an applications
 */
public interface ILogger 
{
	/**
	 * Logger method to log the information by passing value as string parameter.
	 * @param str
	 */
	public void info(String str);
	/**
	 * Logger ERROR method to log about errors to log file.
	 * @param str
	 */
	public void error(String str);
	/**
	 * logger DEBUG method to send debug msg to log file.
	 * @param str
	 */
	public void debug(String str);
	
}
