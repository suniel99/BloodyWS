package gipsy.GEE.multitier.DST.jini;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;
/**
 * The ServerSocketFactory used to export Jini services based on
 * BoundedServerSocket.
 * 
 * @author Yi Ji
 * @version $Id: BoundedServerSocketFactory.java,v 1.2 2010/09/09 18:18:43 mokhov Exp $
 */
public class BoundedServerSocketFactory 
implements RMIServerSocketFactory 
{
	public ServerSocket createServerSocket(int piPort) 
	throws IOException 
	{
		/* 
		 * Set the backlog of this socket to 500 rather than the default
		 * JDK implementation value 50
		 */
		return new BoundedServerSocket(piPort, 500);
	}
}
