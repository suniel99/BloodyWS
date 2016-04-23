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


/**
 * Class WorkResultHD.
 *
 * @author Emil Vassev
 * @author Serguei Mokhov
 * @since 1.0.0
 * @version 2.0.0, $Id: WorkResultHD.java,v 1.16 2010/02/18 18:07:20 mokhov Exp $
 */
public class WorkResultHD
extends ProceduralDemand
{
	/*
	 * ---------
	 * Constants
	 * ---------
	 */
	
	/**
	 * XXX: regenerate using serialver 
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	 * ------------
	 * Data Members
	 * ------------
	 */

	/**
	 * XXX. 
	 */
	private String strResult;
	
//	***************************************************************
//	**** object lifetime
	
	/**
	 * Constructor by default.
	 * XXX is it valid with the null/empty values?
	 */
	public WorkResultHD() 
	{
		this(null, null);
	}

	/**
	 * XXX.
	 * @param poSignature XXX
	 * @param pstrInCreatorAddress XXX
	 */
	public WorkResultHD(DemandSignature poSignature, String pstrInCreatorAddress) 
	{
		super(poSignature);
		this.strResult = pstrInCreatorAddress + ", " + poSignature;
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
		return this.strResult;
	}
	
	/* (non-Javadoc)
	 * @see gipsy.GEE.IDP.demands.Demand#storeResult(java.lang.Object)
	 */
	@Override
	public DemandSignature storeResult(Serializable poDirName)
	{
		storeResult(poDirName.toString());
		return getSignature();
	}

	/**
	 * XXX.
	 * @param pstrDirName XXX.
	 */
	private void storeResult(String pstrDirName)
	{
		FileOutputStream oFileOut; 
		BufferedWriter oWriterFile;
		String strFileName = pstrDirName + "/" + DateTimeUtils.getCurrentDateTime() + "_" + "WorkResultHD" + ".out";
		
		try
		{
			oFileOut = new FileOutputStream(strFileName);
			oWriterFile = new BufferedWriter(new OutputStreamWriter(oFileOut, "ASCII"));
			
			oWriterFile.write(this.strResult);
			// XXX: hardcoding
			oWriterFile.write("" + (char)13 + (char)10);
			oWriterFile.write(this.oTimeLine.getTimeLine());
			
			oWriterFile.flush();
			oWriterFile.close(); 
			oFileOut.close();
		}
		catch(IOException ex)
		{
			// XXX: why empty?
		}
	}
}

// EOF
