package gipsy.tests.GEE.IDP.demands;

import java.nio.MappedByteBuffer;

import gipsy.GEE.IDP.demands.Demand;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;

public class LargeByteArrayDemand 
extends Demand 
{
	public byte[] oatLargeByteArray;
	
	/**
	 * Wrap a byte array inside this instance. Note that each byte element 
	 * in the array actually occupies 4 bytes in memory.
	 * @param piLength
	 */
	public LargeByteArrayDemand(int piLength) 
	{
		oatLargeByteArray = new byte[piLength];
	}

	@Override
	public IDemand execute() {
		// TODO Auto-generated method stub
		return this;
	}
}
