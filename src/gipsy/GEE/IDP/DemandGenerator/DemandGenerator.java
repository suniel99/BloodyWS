package gipsy.GEE.IDP.DemandGenerator;

import gipsy.GEE.CONFIG;
import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.DemandDispatcher.IDemandDispatcher;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IVW.Warehouse.IVWControl;
import gipsy.GEE.IVW.Warehouse.IVWInterface;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.interfaces.GEERSignature;
import gipsy.interfaces.GIPSYProgram;
import gipsy.interfaces.LocalDemandStore;
import gipsy.lang.GIPSYContext;
import gipsy.lang.GIPSYType;
import marf.util.BaseThread;


/**
 * <p>This class should instantiate whatever implementation
 * of GEE (RMI or Threaded) is requested and use it.
 * </p>
 * 
 * @author Serguei Mokhov
 * @version $Id: DemandGenerator.java,v 1.17 2010/12/10 04:00:27 mokhov Exp $
 * @since inception
 */
public abstract class DemandGenerator
extends BaseThread
implements IDemandGenerator
{
	/**
	 * "My pending demand list". 
	 */
	protected LocalDemandStore oPendingDemands = new LocalDemandStore();
	
	/**
	 * Associated demand dispatcher link. 
	 */
	protected IDemandDispatcher oDispatcher = null;

	/**
	 * Associated GEER to generate demands from.
	 */
	protected GIPSYProgram oGEER = null;
	
	public DemandGenerator()
	{
		this(null);
	}

	public DemandGenerator(IVWInterface poValueHouse)
	{
		//TODO
		//super(poValueHouse);
	}

	/**
	 * XXX: rewrite!!
	 * @param piID
	 * @param paiContext
	 * @return
	 */
	public String generateDemand(int piID, int[] paiContext)
	{
		return generateLegacyDemand(piID, paiContext);
	}

	public static String generateLegacyDemand(int piID, int[] paiContext)
	{
		String strDemand = Integer.toString(piID) + ":";

		for(int i = 0 ; i < CONFIG.DIMENSION_MAX; i++)
		{
			strDemand += Integer.toString(paiContext[i]) + ":";
		}

		return strDemand;
	}

	public void generateDemand(IDemand poDemand)
	{
		// TODO Auto-generated method stub
	}

	public GIPSYType generateDemandAndWait(IDemand poDemand)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public IDemand getComputedDemand(DemandSignature poDemandSignature, GEERSignature poGEERSignature)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public IDemand getComputedDemand(DemandSignature poSiganture)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public IDemand getDemand()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean isComputed(DemandSignature poDemandSignature, GEERSignature poGEERSignature)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean isComputed(DemandSignature poSignature)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void putResult(IDemand poResult)
	{
		// TODO
		//this.oValueHouse.setValue(poResult.getSignature().toString(), poResult);
	}

	public Boolean registerWithDemandStore()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean registerWithDemandStore(IVWControl poStorageSubsystem)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDemandDispatcher(IDemandDispatcher poDispatcher)
	{
		this.oDispatcher = poDispatcher;
	}

	@Override
	public void setGEER(GIPSYProgram poGEER)
	{
		this.oGEER = poGEER;
	}

	@Override
	public GIPSYType eval()
	throws GEEException
	{
		return eval
		(
			(SimpleNode)this.oGEER.getAbstractSyntaxTrees()[0].getRoot(),
			new GIPSYContext[] { this.oGEER.getContextValue() },
			0
		);
	}

	@Override
	public GIPSYType execute()
	{
		return execute(this.oGEER.getDictionary(), this.oGEER.getContextValue());
	}
}

// EOF
