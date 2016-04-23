package gipsy.GEE.multitier.DST.jini;

import java.io.IOException;
import java.net.Socket;

/**
 * The Socket with a static volatile counter to count the number of 
 * unclosed sockets.
 * 
 * @author Yi Ji
 * @version $Id: BoundedSocket.java,v 1.2 2010/09/09 18:18:43 mokhov Exp $
 */
public class BoundedSocket 
extends Socket 
{
	/**
	 * The counter to count unclosed sockets of this type.
	 */
	public static volatile long siCounter = 0;
	
	public synchronized void close() 
	throws IOException 
	{
		super.close();
		siCounter --;
	}
}
