package gipsy.GIPC.intensional.SIPL.JOOIP;

import gipsy.GIPC.intensional.SimpleNode;

import java.util.Hashtable;
import gipsy.storage.Dictionary;
import gipsy.storage.DictionaryItem;
/**
 * Defines items in the translation-table
 *
 * @author  aihua_wu
 * @version 1.0
 * @since October,2007
 */

// JOOIPToJavaTranslationItem
public class JOOIPToJavaTranslationItem {
	
	String strIntensionalID;
	String strJavaClassName;
	boolean bIsJavaMember;	
	String strIntensionalCompilerName;
	StringBuffer oIntensionalCode;
	String strReplacementCode;
	SimpleNode oEntry;

	Dictionary oSemanticDictionary = new Dictionary();
	
	/**
	 * Constructor.
	 */
	public JOOIPToJavaTranslationItem()
	{
		this("", "", false,"", new StringBuffer(), "", null, null);
	}

	public JOOIPToJavaTranslationItem(String pstrIntensionalID, String pstrJavaClassName, boolean pbIsJavaMember, String pstrICompilerName, 
		StringBuffer poIntensionalCode, String pstrReplacementCode, SimpleNode poEntry, Dictionary poSementicDictionary)
	{
		this.strIntensionalID = pstrIntensionalID;
		this.strJavaClassName = pstrJavaClassName;
		this.bIsJavaMember = false;		
		this.strIntensionalCompilerName = pstrICompilerName;
		this.oIntensionalCode = poIntensionalCode;
		this.strReplacementCode = pstrReplacementCode;		
		this.oEntry = poEntry;
		this.oSemanticDictionary = poSementicDictionary;
	}
}
