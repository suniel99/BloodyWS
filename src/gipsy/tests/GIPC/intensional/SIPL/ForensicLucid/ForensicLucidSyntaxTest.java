package gipsy.tests.GIPC.intensional.SIPL.ForensicLucid;

import gipsy.GIPC.GIPC;
import gipsy.GIPC.GIPCException;
import gipsy.GIPC.intensional.SIPL.ForensicLucid.ForensicLucidCompiler;
import gipsy.interfaces.AbstractSyntaxTree;

/**
 * @author Serguei Mokhov
 * @version $Id: ForensicLucidSyntaxTest.java,v 1.9 2015/07/08 14:45:37 mokhov Exp $
 * @version $GIPSYId$
 */
public class ForensicLucidSyntaxTest
{
	/**
	 * @param argv
	 */
	public static void main(String[] argv)
	throws GIPCException
	{
		if(argv == null || argv.length == 0)
		{
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/Lucx/SyntaxTest/cxt1stclassvalue1.ipl"};
			argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/blackmail-case.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/combine.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/duplicate-context-value-tags-code.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/observation-decl.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/mac-spoofer-analyzer-basic-claims.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/282936.spoofer.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/282936.notspoofer.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/observation-o-P.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/observation.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/printer-case.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/product.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/three-ob-marf-flucid-complete.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/three-ob-marf-flucid-simplified.ipl"};
			
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/dstme-limb.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/dstme-light-source.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/dstme-raining-flucid.ipl"};
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/raining-flucid.ipl"};

			//
			//argv = new String[] {"src/gipsy/tests/GIPC/intensional/SIPL/ForensicLucid/284623-activity-orig.ipl"};
		}
		
		
		
		ForensicLucidCompiler oCompiler = new ForensicLucidCompiler(argv[0]);
        oCompiler.init();
        
        GIPC.siPrimaryParserType = GIPC.FORENSIC_LUCID_PARSER;
        AbstractSyntaxTree oTree = oCompiler.parse();
        System.err.println("Now compiling: " + argv[0]);
        oTree.dump(" ");
	}
}

// EOF
