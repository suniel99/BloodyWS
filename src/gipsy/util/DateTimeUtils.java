package gipsy.util;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A common standard place for date/time uniform formatting.
 * 
 * Primarily based on refactoring of Emil's various demand
 * classes from the simulator.
 *
 * @author Serguei Mokhov
 * @since September 29, 2009
 * @version $Id: DateTimeUtils.java,v 1.1 2009/09/29 16:11:18 mokhov Exp $
 */
public class DateTimeUtils
{
	/**
	 * A common date/time formatting of "yyyy_MM_dd-HH-mm-ssZ".
	 */
	public static final String COMMON_FULL_TIMSTAMP_FORMAT = "yyyy_MM_dd-HH-mm-ssZ";

	/**
	 * Intentionally protected to allow inheritance, but not
	 * instantiation. 
	 */
	protected DateTimeUtils()
	{
		// Nothing to initialize
	}
	
	/**
	 * @return XXX
	 */
	public static String getCurrentDateTime()
	{
		String strDateTime;
		SimpleDateFormat oSDF = new SimpleDateFormat(COMMON_FULL_TIMSTAMP_FORMAT);
				
		//**** new Date() gets current date/elapsedTime
		strDateTime = oSDF.format(new Date());

		return strDateTime;
	}

}

// EOF
