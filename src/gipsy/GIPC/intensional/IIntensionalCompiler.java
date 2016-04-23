package gipsy.GIPC.intensional;

import gipsy.GIPC.ICompiler;
import gipsy.interfaces.AbstractSyntaxTree;


/**
 * All Intensional Compilers should implement this interface
 * if they cannot subclass IntensionalCompiler.
 * 
 * $Id: IIntensionalCompiler.java,v 1.6 2009/08/25 18:46:01 mokhov Exp $
 * $Revision: 1.6 $
 * $Date: 2009/08/25 18:46:01 $
 * 
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 */
public interface IIntensionalCompiler
extends ICompiler
{
	/**
	 * Perform required translations of the parser-generated AST.
	 * E.g. SIPL-to-GIPL translation can be performed.
	 * 
	 * @return AST after translation
	 * 
	 * @throws IPLCFException if there was an error during the translation process.
	 */
	public AbstractSyntaxTree translate()
	throws IntensionalCompilerException;
}

// EOF
