package gipsy.util;


/**
 * Platform-specific utilities.
 * 
 * @author Serguei Mokhov
 * @author Sleiman Rabah
 * @version $Id: Platform.java,v 1.1 2012/06/19 16:58:06 mokhov Exp $
 * @since June 19, 2012
 */
public class Platform
{
	/**
	 * @return true of the underlying OS platform is a Windows variant. 
	 */
	public static boolean isWindows()
	{
		return System.getProperty("os.name").toLowerCase().matches(".*win.*");
	}
 
	/**
	 * @return true of the underlying OS platform is a Mac OS variant. 
	 */
	public static boolean isMac()
	{
		return System.getProperty("os.name").toLowerCase().matches(".*mac.*");
	}
 
	/**
	 * @return true of the underlying OS platform is a Linux or Unix variant. 
	 */
	public static boolean isUnix()
	{
		return System.getProperty("os.name").toLowerCase().matches(".*n[iu]x.*");
	}
 
	/**
	 * @return true of the underlying OS platform is a Solaris variant. 
	 */
	public static boolean isSolaris()
	{
		return System.getProperty("os.name").toLowerCase().matches(".*sunos.*");
 	}

}

// EOF
