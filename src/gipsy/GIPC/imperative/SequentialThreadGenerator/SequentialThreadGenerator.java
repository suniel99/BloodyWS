package gipsy.GIPC.imperative.SequentialThreadGenerator;

import gipsy.GIPC.imperative.ImperativeCompilerException;
import gipsy.interfaces.ISequentialThread;

import java.lang.reflect.Method;


/**
 * <p>Generates actual sequential threads.</p>
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.14 $
 * @since 1.0.0
 */
public abstract class SequentialThreadGenerator
{
	/**
	 * 
	 */
	protected SequentialThreadSourceGenerator oSourceGenerator = null;

	protected ISequentialThread[] aoSTs = null;
	
	/**
	 * Be it genuine Java method or a native method.
	 */
	protected Method[] aoSTBodies = null;

	public SequentialThreadGenerator()
	{
		this.oSourceGenerator = new SequentialThreadSourceGenerator();
	}

	/**
	 * Must be implemented by the derivatives.
	 * @throws ImperativeCompilerException
	 */
	public abstract void generate()
	throws ImperativeCompilerException;

	public ISequentialThread[] getSequentialThreads()
	{
		return this.aoSTs;
	}
}

// EOF
