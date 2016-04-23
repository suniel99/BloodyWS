package gipsy.tests.GEE.simulator.demands;

import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.ProceduralDemand;
import gipsy.GEE.IDP.demands.TimeLine;
import gipsy.util.DateTimeUtils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Class WorkResultPi.
 *
 * @author Emil Vassev
 * @author Serguei Mokhov
 * @since 1.0.0
 * @version 2.0.0, $Id: WorkResultPi.java,v 1.17 2011/01/26 05:11:36 ji_yi Exp $
 */
public class WorkResultPi
extends ProceduralDemand
{
	/*
	 * ---------
	 * Constants
	 * ---------
	 */
	
	/**
	 * XXX: redo properly using serialver 
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
	public String strResult;

	private BigDecimal oResult;
	
//	***************************************************************
//	**** object lifetime

	/**
	 * Constructor by default.
	 * XXX: review necessity and correctness.
	 */
	public WorkResultPi() 
	{
		this(null, null);
	}

	/**
	 * XXX.
	 * @param poSignature XXX
	 * @param pstrInCreatorAddress XXX
	 */
	public WorkResultPi(DemandSignature poSignature, String pstrInCreatorAddress) 
	{
		super(poSignature);
		
		Pi oPi = new Pi();
		this.oResult = oPi.calculatePi(1000);
		// XXX: hardcoding of 5000
		//this.strResult = pstrInCreatorAddress + ", Pi calculation: " + oPi.calculatePi(5000);
	}

//	***************************************************************
//	**** local methods

//	***************************************************************
//	**** IDemand methods


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
		//return this.strResult;
		return this.oWorkResult;
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
		FileOutputStream oFileOut; 
		BufferedWriter oWriterFile;
		String strFileName = pstrDirName + "/" + DateTimeUtils.getCurrentDateTime() + "_" + "WorkResultPi" + ".out";

		try
		{
			oFileOut = new FileOutputStream(strFileName);
			oWriterFile = new BufferedWriter(new OutputStreamWriter(oFileOut, "ASCII"));
			
			oWriterFile.write(this.strResult);
			// XXX: EOL hardcoding
			oWriterFile.write("" + (char)13 + (char)10);
			oWriterFile.write(this.oTimeLine.getTimeLine());
				
			oWriterFile.flush();
			oWriterFile.close(); 
			oFileOut.close();
		}
		catch(IOException ex)
		{
			// XXX: handle or document why no action in handling
		}
	}
}

// EOF
