package gipsy.GEE.IDP.demands;

import java.io.Serializable;


/**
 * This is the demand type enumeration class. 
 * 
 * @author Emil Vassev
 * @since 1.0.0
 * @version $Id: DemandType.java,v 1.5 2010/03/01 08:26:32 bin_ha Exp $
 */
public class DemandType
implements Serializable 
{
	/*
	 * Constants
	 */
	
	/**
	 * XXX: redo. 
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	 * Data Members
	 */
	
	private static final String DT_INTENSIONAL  = "intensional demand";  
	private static final String DT_PROCEDURAL  = "procedural demand";  
	private static final String DT_RESOURCE = "resource demand";  
	private static final String DT_SYSTEM = "system demand";  
	
	/**
	 * XXX.
	 */
	private String strType = "";

	public static final DemandType INTENSIONAL = new DemandType(DT_INTENSIONAL);  
	public static final DemandType PROCEDURAL = new DemandType(DT_PROCEDURAL); 
	public static final DemandType RESOURCE = new DemandType(DT_RESOURCE); 
	public static final DemandType SYSTEM = new DemandType(DT_SYSTEM); 

	
	/**
	 * XXX.
	 * @param pstrState
	 */
	private DemandType(String pstrState) 
	{ 
		this.strType = pstrState.toLowerCase();
	} 

	/**
	 * XXX.
	 * @return
	 */
	public boolean isIntensional()
	{
		return DT_INTENSIONAL.equals(this.strType);
	}

	/**
	 * XXX.
	 * @return
	 */
	public boolean isProcedural()
	{
		return DT_PROCEDURAL.equals(this.strType);
	}

	/**
	 * XXX.
	 * @return
	 */
	public boolean isResource()
	{
		return DT_RESOURCE.equals(this.strType);
	}

	/**
	 * XXX.
	 * @return
	 */
	public boolean isSystem()
	{
		return DT_SYSTEM.equals(this.strType);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * For the purpose of testing.
	 */
	public String toString()
	{
		if(this.strType.equals(DT_INTENSIONAL))
		{
			return "DT_INTENSIONAL";
		}
		else if(this.strType.equals(DT_PROCEDURAL))
		{
			return "DT_PROCEDURAL";
		}
		else if(this.strType.equals(DT_RESOURCE))
		{
			return "DT_RESOURCE";
		}
		else
			return "DT_SYSTEM";
		
	}
}

// EOF
