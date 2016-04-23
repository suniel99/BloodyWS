/**
 * WSDSTWrapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gipsy.GEE.multitier.DST.ws;

public interface WSDSTWrapper extends java.rmi.Remote {
    public WSDemand getHashTableValue(WSDemandSignature podSignature) throws java.rmi.RemoteException;
    public WSDemandSignature setHashTable(WSDemand poIdObj) throws java.rmi.RemoteException;
    public WSDemand getPendingDemand() throws java.rmi.RemoteException;
}
