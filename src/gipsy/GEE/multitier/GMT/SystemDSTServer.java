package gipsy.GEE.multitier.GMT;

import gipsy.GEE.IDP.ITransportAgent;

/**
 * This class manages a system DST registered at the GMT.
 * Whenever there is a new system DST registered at the GMT, 
 * the GMT launches an instance of this class to process
 * system demands received from that DST.
 * 
 * @author Yi Ji
 * @version $Id: SystemDSTServer.java,v 1.1 2010/09/12 02:13:06 ji_yi Exp $
 */
public class SystemDSTServer 
extends Thread 
{
	private GMTWrapper oGMT;
	private ITransportAgent oSystemDSTTA;
	
	public SystemDSTServer(GMTWrapper poGMT, ITransportAgent poSystemDSTTA) 
	{
		this.oGMT = poGMT;
		this.oSystemDSTTA = poSystemDSTTA;
	}


	public void run() 
	{
		
	}
	
	
}
