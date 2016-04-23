package gipsy.GIPC.intensional.SIPL.JOOIP;

import java.util.Hashtable;

/**
 * A table record Java classes (Variables and Parameters).
 *
 * @author  aihua_wu
 * @version 1.0
 * @since October,2007
 */
//JavaClassSymbolTable
public class JavaClassSymbolTable {
	protected String strClassName;
	protected String strExtendName;
	protected String strInterfaceName;

	/**
	 * Identifiers for data and method members of the class. 
	 */
	protected Hashtable oMemberTable;    
    
	/**
     * Constructor.
     */
	public JavaClassSymbolTable()
	{
		this("", "", "", new Hashtable());
	}

	public JavaClassSymbolTable(String pstrClassName, String pstrExtendName, String pstrInterfaceName, Hashtable poMemberTable)
	{
		this.strClassName = pstrClassName;
		this.strExtendName = pstrExtendName;		
		this.strInterfaceName = pstrInterfaceName;
		this.oMemberTable = poMemberTable;				
	}
}
