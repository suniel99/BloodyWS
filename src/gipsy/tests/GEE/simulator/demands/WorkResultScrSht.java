package gipsy.tests.GEE.simulator.demands;

import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.ProceduralDemand;
import gipsy.GEE.IDP.demands.TimeLine;
import gipsy.util.DateTimeUtils;
import gipsy.util.NetUtils;

import java.io.Serializable;


/**
 * Class WorkResultScrSht.
 *
 * @author Emil Vassev
 * @author Serguei Mokhov
 * @since 1.0.0
 * @version 2.0.0, $Id: WorkResultScrSht.java,v 1.16 2010/02/18 18:07:20 mokhov Exp $
 */
public class WorkResultScrSht
extends ProceduralDemand
{
	/*
	 * ---------
	 * Constants
	 * ---------
	 */

	/**
	 * XXX.
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * ------------
	 * Data Members
	 * ------------
	 */
	
	/**
	 * XXX: public?
	 */
	public PrintScreen oPrintScreen;


//	***************************************************************
//	**** object lifetime
	
	/**
	 * Constructor by default.
	 * XXX
	 */
	public WorkResultScrSht() 
	{
		this(null, "");
	}

	/**
	 * XXX.
	 * @param poSignature XXX
	 * @param pstrInCreatorAddress XXX
	 */
	public WorkResultScrSht(DemandSignature poSignature, String pstrInCreatorAddress) 
	{
		super(poSignature);
		
		this.oPrintScreen = new PrintScreen(pstrInCreatorAddress);
		this.oPrintScreen.takeScreenShot(NetUtils.getLocalIPAddress());
	}
   
//	***************************************************************
//	**** local methods

//	***************************************************************
//	**** IDemand methods

	@Override
	public void addTimeLine(TimeLine poTimeLine)
	{
		poTimeLine.addTimeLine(poTimeLine);
	}

	@Override
	public double getSize()
	{
		return 1;
	}
   
	@Override
	public IDemand execute()
	{
		return this;
	}
   
	@Override
	public Serializable getResult()
	{
		this.oPrintScreen.saveImageToFile("");
		return "The screenshot has been savedImage is saved.";
	}
   
	@Override
	public DemandSignature storeResult(Serializable poDirName)
	{
		storeResult(poDirName.toString());
		return getSignature();
	}
	
	/**
	 * XXX.
	 * @param pstrDirName XXX
	 */
	private void storeResult(String pstrDirName)
	{
		this.oPrintScreen.saveImageToFile(pstrDirName, DateTimeUtils.getCurrentDateTime());
	}  
}

// EOF
