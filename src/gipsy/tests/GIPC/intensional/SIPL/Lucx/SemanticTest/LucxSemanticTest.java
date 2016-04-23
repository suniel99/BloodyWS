package gipsy.tests.GIPC.intensional.SIPL.Lucx.SemanticTest;

import gipsy.GIPC.GIPC;
import gipsy.GIPC.GIPCException;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.GIPC.intensional.SIPL.Lucx.LucxCompiler;
import gipsy.interfaces.AbstractSyntaxTree;
import gipsy.storage.Dictionary;

/**
 * @author Xin Tong
 * @version $GIPSYId$
 */
public class LucxSemanticTest
{
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{
			LucxCompiler lucx =new LucxCompiler(args[0]);
			//LucxCompiler lucx=new LucxCompiler("C:\\Documents and Settings\\x_ton\\workspace\\GIPSY\\src\\gipsy\\tests\\lucx\\TestTagSetTypes.ipl") ;
	        lucx.init();
	        
	        GIPC.siPrimaryParserType=GIPC.LUCX_PARSER;
	        AbstractSyntaxTree Tree=lucx.parse();
	        //Tree.dump(" ");
	        
	        LucxSemanticAnalyzer oSemanticAnalyzer=new LucxSemanticAnalyzer();
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
