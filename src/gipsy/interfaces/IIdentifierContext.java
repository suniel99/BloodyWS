package gipsy.interfaces;

//import java.rmi.*;

import gipsy.GEE.GEEException;

//public interface IdentifierClassInterface extends java.rmi.Remote {
public interface IIdentifierContext
{
//  public IC ( int[] ) throws RemoteException;
//  public Object cal( ) throws RemoteException;
	public Object cal() throws GEEException;

	public boolean isReady();
	public int getValue();

	public void setReady();
	public void setValue(int piValue);

	public int getName();
	public int getHcode();
	public int[] getCont();
}

// EOF
