package gipsy.GIPC;

import marf.util.Debug;
import gipsy.interfaces.AbstractSyntaxTree;
import gipsy.interfaces.GIPSYProgram;

/**
 * General Eduction Engine Resource Generator, a.k.a Linker
 * where GEER is GIPSYProgram's instance.
 * 
 * Performs final linking of AST parts and replaces imperative
 * stub nodes with the actual content.
 * 
 * @author Serguei Mokhov
 */
public class GEERGenerator
{
	/**
	 * Local reference to the compiled GIPSY program being produced.
	 */
	private GIPSYProgram oGIPSYProgram = null;
	
	/**
	 * Local reference to the AST for linking.
	 */
	private AbstractSyntaxTree oAST = null;
	
	/**
	 * Accepts AST as an argument for linking.
	 * @param poAST AST to link
	 */
	public GEERGenerator(AbstractSyntaxTree poAST)
	{
		this.oAST = poAST;
	}

	/**
	 * Main linking routine. Calls replaceImperativeStubs()
	 * and produceCompositeAST().
	 * 
	 * @return a compiled and linked GIPSYProgram if there was no any error
	 * @throws GIPCException if there was a link error   
	 */
	public GIPSYProgram link()
	throws GIPCException
	{
		Debug.debug("Linking begun.");
		this.oGIPSYProgram = new GIPSYProgram();

		replaceImperativeStubs();
		produceCompositeAST();
		
		this.oGIPSYProgram.setAbstractSyntaxTree(this.oAST);
		
		Debug.debug("Linking completed: GIPSY program: " + this.oGIPSYProgram);
		return this.oGIPSYProgram;
	}

	/**
	 * Retrieves local references to the compiled binary
	 * GIPSY program.
	 * 
	 * @return local reference to GIPSYProgram
	 */
	public GIPSYProgram getGEER()
	{
		return this.oGIPSYProgram;
	}
	
	/**
	 * Replaces imperative stub nodes in the AST
	 * with their actual contents.
	 * TODO: implement
	 */
	private void replaceImperativeStubs()
	{
		Debug.debug("Stub: replaceImperativeStubs().");
	}
	
	/**
	 * Produces the AST suitable for GEE's consumption.
	 * TODO: implement
	 */
	private void produceCompositeAST()
	{
		Debug.debug("Stub: produceCompositeAST().");
	}
}

// EOF
