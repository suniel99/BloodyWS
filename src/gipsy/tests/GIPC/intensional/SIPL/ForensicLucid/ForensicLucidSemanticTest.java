package gipsy.tests.GIPC.intensional.SIPL.ForensicLucid;

import gipsy.GIPC.GIPC;
import gipsy.GIPC.GIPCException;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.GIPC.intensional.SIPL.ForensicLucid.ForensicLucidCompiler;
import gipsy.GIPC.intensional.SIPL.ForensicLucid.ForensicLucidSemanticAnalyzer;
import gipsy.interfaces.AbstractSyntaxTree;
import gipsy.storage.Dictionary;

/**
 * @author Serguei Mokhov
 * @version $GIPSYId$
 */
public class ForensicLucidSemanticTest
{
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{
			ForensicLucidCompiler oCompiler =new ForensicLucidCompiler(args[0]);
	        oCompiler.init();
	        
	        GIPC.siPrimaryParserType=GIPC.LUCX_PARSER;
	        AbstractSyntaxTree Tree=oCompiler.parse();
	        //Tree.dump(" ");
	        
	        ForensicLucidSemanticAnalyzer oSemanticAnalyzer=new ForensicLucidSemanticAnalyzer();
	        oSemanticAnalyzer.setupDictionary((SimpleNode)Tree.getRoot());
		}
		catch(GIPCException e)
		{
			System.err.print(e.getMessage());
		}
		
	}
	
	public static void printCurrentDictionary(Dictionary pDictionary)
	{
		for(int i=0; i<pDictionary.size(); i++)
		{
			System.out.println(pDictionary.elementAt(i));
		}
	}
}

// EOF
