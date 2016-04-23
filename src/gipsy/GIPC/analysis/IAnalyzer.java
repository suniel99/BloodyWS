package gipsy.GIPC.analysis;

import gipsy.interfaces.AbstractSyntaxTree;
import gipsy.interfaces.GIPSYProgram;

/**
 * Analysis and annotation of the GEERs to help scheduling
 * and management.
 *
 * @version $Id: IAnalyzer.java,v 1.1 2010/05/27 16:41:05 mokhov Exp $
 * @author Serguei Mokhov
 * @since May 27, 2010
 */
public interface IAnalyzer
extends Runnable
{
	/**
	 * Perform the AST analysis and annotation. May exist
	 * before the GEER exists.
	 * @param poTree
	 * @return
	 */
	AbstractSyntaxTree analyze(AbstractSyntaxTree poTree);
	
	//? This would be the 2nd stage analysis perhaps
	//  which is performed at the very end of the compilation
	//  process.
	GIPSYProgram analyze(GIPSYProgram poProgram);
}

// EOF
