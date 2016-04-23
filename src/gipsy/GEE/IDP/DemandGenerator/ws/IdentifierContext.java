package gipsy.GEE.IDP.DemandGenerator.ws;

import gipsy.GEE.CONFIG;

/**
 * IC Implementation for WS
 * 
 * $Header: /cvsroot/gipsy/gipsy/src/gipsy/GEE/IDP/DemandGenerator/ws/IdentifierContext.java,v 1.7 2004/07/06 22:37:59 mokhov Exp $
 * 
 * @author Paula
 */
public class IdentifierContext 
extends IdentifierContextClient 
implements CONFIG 
{
	/**
	 * each icx object has its specific context.
	 */ 
	private int[] aContext = new int[DIMENSION_MAX];
	
	/**
	 * each icx object would have a value eventually;
	 */ 
	private Object oValue;

	public IdentifierContext() 
	{
		super();
	}

	/**
	 * TODO: Use arraycopy()
	 */
	public void init(int[] paContext) 
	{
		for(int i = 0 ; i < DIMENSION_MAX; i++)
			this.aContext[i] = paContext[i];
	}

	public Object cal() 
	{
		return null;
	}
}

// EOF
