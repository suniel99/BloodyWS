package gipsy.util;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * A common set of network-related utility methods.
 * @author Serguei Mokhov
 * @author Emil Vassev
 * @since
 * @version $Id: NetUtils.java,v 1.2 2009/08/21 17:20:24 mokhov Exp $
 */
public class NetUtils
{
	public static final String MSG_UNKNOWN_IP_ADDR = "Unknown IP address";
	
	/**
	 * Get the IP address of the host machine. 
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getLocalIPAddressIfKnown()
	throws UnknownHostException
	{
		InetAddress oAddr = InetAddress.getLocalHost();
		String strIPAddr = oAddr.getHostAddress();
			
		return strIPAddr;
	}

	
	/**
	 * Get the IP address of the host machine. 
	 * This method returns the IP address of the local machine. <br>
	 * The class InetAddress is used to get the host address.
	 * <p>
	 * <code>
	 * InetAddress oAddr = InetAddress.getLocalHost();
	 * String strIPAddr = oAddr.getHostAddress();
	 * </code>
	 * 
	 * @return either the string of IP address or and error message.
	 * 
	 * @see #MSG_UNKNOWN_IP_ADDR
	 */
	public static String getLocalIPAddress()
	{
		return getLocalIPAddress("");
	}

	
	public static String getLocalIPAddress(String pstrErrorPrefix)
	{
		try
		{
			return getLocalIPAddressIfKnown();
    	}
		catch(UnknownHostException e) 
		{
            e.printStackTrace();
            return pstrErrorPrefix + MSG_UNKNOWN_IP_ADDR;
		}
	}
}

// EOF
