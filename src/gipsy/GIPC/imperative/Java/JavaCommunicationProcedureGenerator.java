package gipsy.GIPC.imperative.Java;

import java.util.Enumeration;

import gipsy.GIPC.imperative.CommunicationProcedureGenerator.CORBACommunicationProcedure;
import gipsy.GIPC.imperative.CommunicationProcedureGenerator.CommunicationProcedureGenerator;
import gipsy.GIPC.imperative.CommunicationProcedureGenerator.DCOMCommunicationProcedure;
import gipsy.GIPC.imperative.CommunicationProcedureGenerator.JiniCommunicationProcedure;
import gipsy.GIPC.imperative.CommunicationProcedureGenerator.NullCommunicationProcedure;
import gipsy.GIPC.imperative.CommunicationProcedureGenerator.RMICommunicationProcedure;
import gipsy.interfaces.ICommunicationProcedure;
import gipsy.lang.GIPSYType;
import marf.util.Arrays;
import marf.util.Debug;


/**
 * <p>Generates a set of defined communication procedures for
 * the Java language. Does the mapping between Java and GIPSY
 * types.</p>
 *
 * @author Serguei Mokhov
 * @version $Id: JavaCommunicationProcedureGenerator.java,v 1.12 2010/12/07 01:37:09 mokhov Exp $
 * @since
 */
public class JavaCommunicationProcedureGenerator
extends CommunicationProcedureGenerator
{
	protected Class<?> oReturnType = null;
	protected Class<?>[] aoParams = null;

	public JavaCommunicationProcedureGenerator(Class<?> poReturnType, Class<?>[] paoParams)
	{
		this.oReturnType = poReturnType;
		this.aoParams = paoParams;
	}

	public void generate()
	{
		Debug.debug("Return type (Java): " + this.oReturnType);
		GIPSYType oGIPSYReturnType = TypeMap.getGIPSYType(this.oReturnType.toString()); 
		Debug.debug("Return type (GIPSY): " + oGIPSYReturnType);

		Debug.debug("Parameter types:");

		GIPSYType[] aoGIPSYParamTypes = new GIPSYType[this.aoParams.length];
		
		for(int j = 0; j < this.aoParams.length; j++)
		{
			Debug.debug("Java Param " + j + ": " + this.aoParams[j]);
			aoGIPSYParamTypes[j] = TypeMap.getGIPSYType(this.aoParams[j].toString());
			Debug.debug("GIPSY Param " + j + ": " + aoGIPSYParamTypes[j]);
		}

		Enumeration<Integer> oCPImplEnum = this.oCPImplementations.keys();
		ICommunicationProcedure[] aoPotentialCPs = new ICommunicationProcedure[this.oCPImplementations.size()]; 
		
		int iCPCount = 0;
		
		while(oCPImplEnum.hasMoreElements())
		{
			Integer oCPImplCode = oCPImplEnum.nextElement();
			int iCPImplEnum = oCPImplCode.intValue();
			boolean bImplemented = this.oCPImplementations.get(oCPImplCode).booleanValue();

			if(bImplemented)
			{
				switch(iCPImplEnum)
				{
					case NULL_CP:
						aoPotentialCPs[iCPCount++] = new NullCommunicationProcedure(oGIPSYReturnType, aoGIPSYParamTypes);
						break;

					case RMI_CP:
						aoPotentialCPs[iCPCount++] = new RMICommunicationProcedure(oGIPSYReturnType, aoGIPSYParamTypes);
						break;

					case JINI_RMI_CP:
					case JINI_JERI_CP:
						aoPotentialCPs[iCPCount++] = new JiniCommunicationProcedure(oGIPSYReturnType, aoGIPSYParamTypes);
						break;

					case COM_CP:
						aoPotentialCPs[iCPCount++] = new DCOMCommunicationProcedure(oGIPSYReturnType, aoGIPSYParamTypes);
						break;

					case CORBA_CP:
						aoPotentialCPs[iCPCount++] = new CORBACommunicationProcedure(oGIPSYReturnType, aoGIPSYParamTypes);
						break;
						
					default:
						assert false : "Unknown CP implementation enumeration (" + iCPImplEnum + ")";
				}

				Debug.debug("Generated CP: " + aoPotentialCPs[iCPCount - 1]);
			}
		}
		
		assert iCPCount == 0 : "No CPs were generated.";
		
		this.aoCPs = new ICommunicationProcedure[iCPCount];
		Arrays.copy(this.aoCPs, 0, aoPotentialCPs, 0, iCPCount);
		
		Debug.debug("Java CPs generated: " + iCPCount);
	}
}

// EOF
