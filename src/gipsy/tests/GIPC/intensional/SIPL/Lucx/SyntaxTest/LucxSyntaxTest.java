package gipsy.tests.GIPC.intensional.SIPL.Lucx.SyntaxTest;

import gipsy.GIPC.GIPC;
import gipsy.GIPC.GIPCException;
import gipsy.GIPC.intensional.SIPL.Lucx.LucxCompiler;
import gipsy.interfaces.AbstractSyntaxTree;

/**
 * @author Xin Tong
 */
public class LucxSyntaxTest
{
	/**
	 * @param argv
	 */
	public static void main(String[] argv)
	throws GIPCException
	{
		if(argv == null || argv.length == 0)
		{
			argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/Lucx/SyntaxTest/cxt1stclassvalue1.ipl"};
		}
		
		LucxCompiler oCompiler = new LucxCompiler(argv[0]);
        oCompiler.init();
        
        GIPC.siPrimaryParserType = GIPC.LUCX_PARSER;
        AbstractSyntaxTree oTree = oCompiler.parse();
        System.err.println("Now compiling: " + argv[0]);
        oTree.dump(" ");
	}
}

// EOF
