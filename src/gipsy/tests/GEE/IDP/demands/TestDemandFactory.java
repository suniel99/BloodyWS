package gipsy.tests.GEE.IDP.demands;

import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.DMSException;
import gipsy.GEE.IDP.DemandGenerator.DemandGenerator;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.DemandType;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.IntensionalDemand;
import gipsy.GEE.IDP.demands.ProceduralDemand;
import gipsy.GEE.IDP.demands.ResourceDemand;
import gipsy.GEE.IDP.demands.SystemDemand;
import gipsy.GIPC.GIPC;
import gipsy.GIPC.GIPCException;
import gipsy.GIPC.imperative.ImperativeNode;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.interfaces.GIPSYProgram;
import gipsy.lang.GIPSYContext;
import gipsy.lang.GIPSYIdentifier;
import gipsy.lang.GIPSYType;
import gipsy.storage.Dictionary;
import gipsy.tests.GEE.simulator.demands.LightUniqueDemand;
import gipsy.tests.GEE.simulator.demands.WorkDemandHD;
import gipsy.tests.GEE.simulator.demands.WorkDemandPi;
import gipsy.tests.GEE.simulator.demands.WorkDemandScrSht;
import gipsy.util.GIPSYException;
import gipsy.util.StringInputStream;


/**
 * This class is designed to produce realistically looking
 * demand instances of various types for testing purposes.
 *
 * @author Serguei Mokhov
 * @since Feb 11, 2010
 * @version $Id: TestDemandFactory.java,v 1.13 2010/12/23 19:39:47 mokhov Exp $
 * 
 * @see IntensionalDemand
 * @see ProceduralDemand
 * @see ResourceDemand
 * @see SystemDemand
 */
public class TestDemandFactory
extends DemandGenerator
{
	/**
	 * Our factory is a singleton. 
	 */
	protected static TestDemandFactory soInstance = null;

	/**
	 * Static reference to the compiler instance.
	 */
	protected static GIPC soGIPC = null;

	/**
	 * Static reference to the sample compiled GEER.
	 * Primarily used in the intensional and resource
	 * test demands generation.
	 */
	protected static GIPSYProgram soIntensionalGEER = null;
	
	/**
	 * Compiled GEER containing coarse-grained procedural (imperative) node
	 * for generation of procedural demands.
	 */
	protected static GIPSYProgram soProceduralGEER = null;

	/**
	 * Procedural demands provided by the simulator. We can
	 * re-use them in our test factory and instantiate them
	 * at random via reflection.
	 */
	protected static final String[] SIMULATOR_PROCEDURAL_DEMAND_CLASSES = 
	{
		LightUniqueDemand.class.getName(),
		WorkDemandHD.class.getName(),
		WorkDemandPi.class.getName(),
		WorkDemandScrSht.class.getName()
	};
	
	/**
	 * Construct the factory and compile a test GEER at the same time.
	 * @throws GIPCException if the test compilation fails
	 */
	@SuppressWarnings("deprecation")
	protected TestDemandFactory()
	throws GIPCException
	{
		// Invoke the compiler on some test GIPL program
		soGIPC = new GIPC
		(
			// The program to compile
			new StringInputStream
				//("N @.d 2 where dimension d; N = if (#.d) <= 0 then 1 else (N+1) @.d (#.d) - 1 fi; end"),
				//("N @ d 2 where dimension d; N = 123; end"),
				//("N where N = 123 + 2; end"),
				("N + M where N = 123 + 2 - A; A = 5; M = 2 + N; end"),

			// Assuming compiling as GIPL code above in the debug mode for verbosity
			new String[]
				{"--gipl", "--debug"}
		);
		
		soGIPC.compile();
		
		// Will be used in intensional and resource demands
		soIntensionalGEER = soGIPC.getGEER();
		
		// XXX: kludge for LegacyInterpreter
		soIntensionalGEER.setContext("d=2");
		
		// XXX: for now use the context and signature of the intensional GEER
		soProceduralGEER = soIntensionalGEER; 
		
//		new GEE(soIntensionalGEER).eval(soIntensionalGEER.getContextValue());
	}

	/**
	 * Get an instance of the factory.
	 * @return
	 * @throws GIPSYException
	 */
	public static TestDemandFactory getInstance()
	throws GIPSYException
	{
		if(soInstance == null)
		{
			soInstance = new TestDemandFactory();
		}
		
		return soInstance;
	}
	
	/**
	 * Creates a demand of the specified type.
	 * 
	 * @param poType type of the demand to create
	 * @return interface-bound demand instance.
	 * 
	 * @see DemandType#INTENSIONAL
	 * @see DemandType#PROCEDURAL
	 * @see DemandType#RESOURCE
	 * @see DemandType#SYSTEM
	 */
	public IDemand create(DemandType poType)
	throws GIPSYException
	{
		if(poType.isIntensional())
		{
			return createIntensionalDemand();
		}
		
		if(poType.isProcedural())
		{
			return createProceduralDemand();
		}

		if(poType.isResource())
		{
			return createResourceDemand();
		}

		if(poType.isSystem())
		{
			return createSystemDemand();
		}

		return null;
	}
	
	/**
	 * Intensional demands are in the basic form of
	 * <code>{GEERid, programId, context}</code>.
	 * 
	 * @return a concrete instance of an intensional demand 
	 */
	public IntensionalDemand createIntensionalDemand()
	{
		// Context
		IntensionalDemand oDemand = new IntensionalDemand(soIntensionalGEER.getContextValue());
		
		// GEERid
		oDemand.setSignature(new DemandSignature(soIntensionalGEER.getSignature()));
		
		// programId
		oDemand.setProgramID(new GIPSYIdentifier(soIntensionalGEER.getContextValue().getID()));
		
		// XXX: hack/shortcut/kludge
		{
			/*
			 * This shortcut would allow immediate testing at the
			 * worker's site without demanding for the GEER first.
			 * This should be removed once the functionality implementing
			 * the workers' demanding for GEERs is done.
			 * 
			 * In the bellow we silently transport the GEER itself
			 * as a stowaway in the placeholder for results. The
			 * resulting test working tier can extract the GEER
			 * from there, then invoke the runtime (GEE) on it,
			 * and replace the value with the actual result.
			 * 
			 * The test worker can do:
			 * 
			 * ... receive the demand ...
			 * GIPSYProgram oGEER = (GIPSYProgram)oDemand.getResult();
			 * GIPSYType oResult = new GEE(oGEER).eval(oGEER.getContextValue());
			 * oDemand.storeResult(oResult);
			 * ... send the result ...
			 */
			oDemand.storeResult(soIntensionalGEER);
		}
		
		return oDemand;
	}
	
	/**
	 * @return a random ProceduralDemand from the simulator
	 * @throws DMSException if instantiation of simulator demand fails for some reason
	 */
	public ProceduralDemand createProceduralDemand()
	throws DMSException
	{
		ProceduralDemand oDemand = null;
	
		try
		{
			int iSelectProceduralDemand = (int)(Math.random() * SIMULATOR_PROCEDURAL_DEMAND_CLASSES.length);

			// Pick a procedural demand at random
			oDemand =
				(ProceduralDemand)
				Class
					.forName(SIMULATOR_PROCEDURAL_DEMAND_CLASSES[iSelectProceduralDemand])
					.newInstance();
		
			// Set its context, signature, and program ID
			oDemand.setContext(soProceduralGEER.getContextValue());
			oDemand.setSignature(new DemandSignature(soProceduralGEER.getSignature()));
			oDemand.setProgramID(new GIPSYIdentifier(soProceduralGEER.getContextValue().getID()));

			// XXX (2): no arguments (argument list is empty)
			oDemand.setParameters(new GIPSYType[] { });
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			throw new DMSException(e);
		}
		
		return oDemand;
	}
	
	/**
	 * @return
	 */
	public ResourceDemand createResourceDemand()
	{
		ResourceDemand oDemand = new ResourceDemand();
		
		// XXX: need to perhaps update signature that is generated
		//      by Jini or JMS during transportation as this signature
		//      at the moment may be useless.
		oDemand.setResourceID(soProceduralGEER.getSignature());
		
		return oDemand;
	}
	
	/**
	 * @return
	 */
	public SystemDemand createSystemDemand()
	{
		// XXX: not done really
		return new SystemDemand();
	}

	/**
	 * Allows querying for the intensional GEER.
	 * @return returns the value soIntensionalGEER field.
	 */
	public static GIPSYProgram getIntensionalGEER()
	{
		return soIntensionalGEER;
	}

	/**
	 * Allows querying for the procedural GEER.
	 * @return returns the value soProceduralGEER field.
	 */
	public static GIPSYProgram getProceduralGEER()
	{
		return soProceduralGEER;
	}

	/*
	 * XXX: IDemandGenerator API to implement.
	 * Makes sense as a testing ground.
	 */
	/*
	@Override
	public String generateDemand(int piID, int[] paiContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GIPSYType generateDemandAndWait(IDemand poDemand) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateDemand(IDemand poDemand) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IDemand getComputedDemand(DemandSignature poSiganture) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDemand getComputedDemand(DemandSignature poDemandSignature,
			GEERSignature poGEERSignature) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDemand getDemand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putResult(IDemand poResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean isComputed(DemandSignature poSignature) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isComputed(DemandSignature poDemandSignature,
			GEERSignature poGEERSignature) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean registerWithDemandStore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean registerWithDemandStore(IVWControl poStorageSubsystem) {
		// TODO Auto-generated method stub
		return null;
	}
	*/
	
	@Override
	public GIPSYType execute(Dictionary poDictionary, GIPSYContext poDimensionTags)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GIPSYType eval(SimpleNode poRoot, GIPSYContext[] paoContext, int piIndent)
	throws GEEException
	{
		// TODO Auto-generated method stub
		return null;
	}
}

// EOF
