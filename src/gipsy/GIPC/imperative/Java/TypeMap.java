package gipsy.GIPC.imperative.Java;

import java.util.Hashtable;

import gipsy.lang.GIPSYType;


/**
 * <p>Maps Java and GIPSY types.
 * This is a type factory.
 * </p>
 * 
 * @author Serguei Mokhov
 * @version $Revision: 1.1 $
 */
public class TypeMap
{
	protected static Hashtable soTypeMapHashtable = new Hashtable();
	
	static
	{
		soTypeMapHashtable.put("int", GIPSYType.getType(GIPSYType.TYPE_INT));
		soTypeMapHashtable.put("float", GIPSYType.getType(GIPSYType.TYPE_FLOAT));
		soTypeMapHashtable.put("double", GIPSYType.getType(GIPSYType.TYPE_DOUBLE));
		soTypeMapHashtable.put("string", GIPSYType.getType(GIPSYType.TYPE_STRING));
		soTypeMapHashtable.put("char", GIPSYType.getType(GIPSYType.TYPE_CHARACTER));
		soTypeMapHashtable.put("boolean", GIPSYType.getType(GIPSYType.TYPE_BOOLEAN));
		soTypeMapHashtable.put("void", GIPSYType.getType(GIPSYType.TYPE_VOID));
		soTypeMapHashtable.put("array", GIPSYType.getType(GIPSYType.TYPE_ARRAY));
	}
	
	public static final GIPSYType getGIPSYType(String pstrJavaLexeme)
	{
		GIPSYType oType = (GIPSYType)soTypeMapHashtable.get(pstrJavaLexeme);
		
		if(oType == null && pstrJavaLexeme.startsWith("class"))
		{
			oType = GIPSYType.getType(GIPSYType.TYPE_OBJECT);
			oType.setLexeme(pstrJavaLexeme);
		}
		
		return oType;
	}
}

// EOF
