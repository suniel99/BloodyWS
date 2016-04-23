package gipsy.GIPC.imperative.Java;

import gipsy.GEE.IDP.demands.ProceduralDemand;
import gipsy.GIPC.imperative.ImperativeCompilerException;
import gipsy.GIPC.imperative.SequentialThreadGenerator.SequentialThreadGenerator;
import gipsy.interfaces.ISequentialThread;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import marf.util.Arrays;
import marf.util.Debug;


/**
 * Generates Java Sequential Threads.
 *
 * @author Serguei Mokhov
 * @version $Id: JavaSequentialThreadGenerator.java,v 1.22 2010/12/06 13:38:41 mokhov Exp $
 * @since
 */
public class JavaSequentialThreadGenerator
extends SequentialThreadGenerator
{
	protected Class<?> oSTClass = null;

	public JavaSequentialThreadGenerator(Class<?> poSTClass)
	{
		super();
		
		if(poSTClass == null)
		{
			throw new NullPointerException("Class parameter cannot be null.");
		}

		this.oSTClass = poSTClass;
	}

	/**
	 * @param pstrSTClassName
	 * @throws ImperativeCompilerException
	 * @throws ClassNotFoundException
	 */
	public JavaSequentialThreadGenerator(String pstrSTClassName)
	throws ImperativeCompilerException, ClassNotFoundException
	{
		this(Class.forName(pstrSTClassName));
	}

	/**
	 * Generates Java Sequential Threads
	 * (binary and source).
	 */
	public void generate()
	throws ImperativeCompilerException
	{
		//this.oSourceGenerator.generate();
		
		Method[] aoRawSTs = this.oSTClass.getDeclaredMethods();
		Constructor<?>[] aoConstructors = this.oSTClass.getConstructors();
	
		if(aoRawSTs.length == 0)
		{
			throw new ImperativeCompilerException("Java ST Generator: No sequential thread methods were found to be defined.");
		}
	

		this.aoSTs = new ISequentialThread[1 + aoRawSTs.length];
		//this.aoSTs = new ISequentialThread[aoRawSTs.length];
		this.aoSTBodies = new Method[aoRawSTs.length];
		Arrays.copy(this.aoSTBodies, 0, aoRawSTs);
		
		for(int i = 0; i < aoRawSTs.length; i++)
		{
			// For CPs
			// Java 1.5
			//Type[] aoParams = aoRawSTs[i].getGenericParameterTypes();
			//Type oReturnType = aoRawSTs[i].getGenericReturnType();
			// Java 1.4
			Class<?>[] aoParams = aoRawSTs[i].getParameterTypes();
			Class<?> oReturnType = aoRawSTs[i].getReturnType();

			// For dictionary
			String strSTID = aoRawSTs[i].getName();
			System.out.println("ST name: " + strSTID);

			JavaCommunicationProcedureGenerator oCPGenerator = new JavaCommunicationProcedureGenerator(oReturnType, aoParams);
			oCPGenerator.generate();
			
			this.aoSTs[i + 1] = new ISequentialThread()
			{
				/**
				 * All STs are Serializable and need an UID. 
				 */
				private static final long serialVersionUID = -335282364640761605L;
				
				public Method oJavaMethod = null;
				//public WorkResult oWorkResult = null;
				//public Object oWorkResult = null;
				public Serializable oWorkResult = null;
				
				public void run()
				{
					this.oWorkResult = work();
				}
				
				//public Object work()
				public Serializable work()
				{
					try
					{
						//return new ProceduralDemand(this.oJavaMethod.invoke(null, null));
						return new ProceduralDemand
						(
							(Serializable)
							this.oJavaMethod.invoke
							(
								(Object[])null,
								(Object[])null
							)
						);
					}
					catch(Throwable e)
					{
						return new ProceduralDemand(e);
					}
				}

				//public Object getWorkResult()
				public Serializable getWorkResult()
				{
					return this.oWorkResult;
				}
				
				public void setMethod(Method poSTMethod)
				{
					this.oJavaMethod = poSTMethod;
				}
			};
			
			this.aoSTs[i + 1].setMethod(aoRawSTs[i]);

			System.out.println("ST generated: " + this.aoSTs[i + 1]);
		}
		
		try
		{
			this.aoSTs[0] = (ISequentialThread)this.oSTClass.newInstance();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new ImperativeCompilerException(e);
		}

		System.out.println("ST generated: " + this.aoSTs[0]);
		
		Debug.debug("All Java STs generated.");
	}
}

// EOF
