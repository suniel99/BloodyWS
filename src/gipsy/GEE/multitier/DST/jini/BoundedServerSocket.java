package gipsy.GEE.multitier.DST.jini;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * The server socket used to export Jini services. It limits the maximum of
 * concurrent connections to prevent JVM crash.
 * 
 * @author Yi Ji
 * @version $Id: BoundedServerSocket.java,v 1.2 2010/09/09 18:18:43 mokhov Exp $
 */
public class BoundedServerSocket 
extends ServerSocket 
{	
	/**
	 * The maximum currently unclosed connection count
	 */
	private static int siMaxConnection = 5000;
	
	/**
	 * @throws IOException
	 * @see {@link java.net.ServerSocket#ServerSocket()}
	 */
	public BoundedServerSocket() 
	throws IOException 
	{
		super();
	}

	/**
	 * @param piPort
	 * @param piBacklog
	 * @param bindAddr
	 * @throws IOException
	 * @see {@link java.net.ServerSocket#ServerSocket(int, int, InetAddress)}
	 */
	public BoundedServerSocket(int piPort, int piBacklog, InetAddress bindAddr)
	throws IOException 
	{
		super(piPort, piBacklog, bindAddr);
	}

	/**
	 * 
	 * @param piPort
	 * @throws IOException
	 * @see {@link java.net.ServerSocket#ServerSocket(int)}
	 */
	public BoundedServerSocket(int piPort) 
	throws IOException
	{
		super(piPort);
	}

	/**
	 * 
	 * @param piPort
	 * @param piBacklog
	 * @throws IOException
	 * @see {@link java.net.ServerSocket#ServerSocket(int, int)}
	 */
	public BoundedServerSocket(int piPort, int piBacklog) 
	throws IOException 
	{
		super(piPort, piBacklog);
	}


	public Socket accept() 
	throws IOException 
	{
		BoundedSocket.siCounter ++;
		Socket oSocket = new BoundedSocket();
		this.implAccept(oSocket);
		
		if(BoundedSocket.siCounter <= siMaxConnection)
		{
			return oSocket;
		}
		else
		{
			OutputStream oOutputStream = oSocket.getOutputStream();
			IOException oException = new RemoteException("Max connection reached!");
			oOutputStream.write(oException.toString().getBytes());
			oOutputStream.flush();
			oSocket.close();
			throw oException;
		}
	}
}
