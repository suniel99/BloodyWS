package gipsy.GIPC.intensional.SIPL.JOOIP;

import gipsy.GIPC.intensional.SimpleNode;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.Expression;

import java.util.Hashtable;
import gipsy.storage.Dictionary;
import gipsy.storage.DictionaryItem;
/**
 * A table record Java Identifiers (Variables and Parameters).
 *
 * @author  aihua_wu
 * @version 1.0
 * @since October,2007
 */
// JavaIdentifierSymbolTable
public class JavaIdentifierSymbolTable {	
	String strID; //strID	
	boolean bIsJavaMember = false; //bIsJavaMember	
	int iMapType; //iMapType
	String strClassName; 
    String strClassInit = null;
    SimpleNode oEntry = null;
    
    // oLucidIdentifierDictionary
    Dictionary oLucidIdentifierDictionary = new Dictionary();    
    
	/**
     * Constructor.
     */
	public JavaIdentifierSymbolTable()
	{
		this("",false,-1, "", "", null, null);
	}

	public JavaIdentifierSymbolTable(String pstrID,boolean pbIsJavaMember,int piMapType, String pstrClassName, String pstrClassInit, SimpleNode poEntry, Dictionary poLucidIdentifierDictionary)
	{
		this.strID = pstrID;		
		this.bIsJavaMember = pbIsJavaMember;		
		this.iMapType = piMapType;		
		this.strClassName = pstrClassName;
		this.strClassInit = pstrClassInit;
		this.oEntry = poEntry;
		this.oLucidIdentifierDictionary = poLucidIdentifierDictionary;
	}
}
