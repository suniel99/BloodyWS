/**
 * 
 */
package gipsy.tests.GEE.simulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import marf.util.BaseThread;

import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.TimeLine;
import gipsy.util.DateTimeUtils;

/**
 * The class whose method is to collect and write timelines into files
 * 
 * @author Yi Ji
 * @version $Id: ResultAnalyst.java,v 1.4 2011/01/12 22:03:17 ji_yi Exp $
 */
public class ResultAnalyst 
implements Runnable
{
	private ResultPool oResultPool = null;
	private String strDirName = null;
	private String strFileName = null;
	
	
	public ResultAnalyst(String pstrDirName)
	{
		this.oResultPool = ResultPool.getInstance();
		this.strDirName = pstrDirName;
		this.strFileName = this.strDirName + "/" + DateTimeUtils.getCurrentDateTime() + "_" + "ResponseTime" + ".csv";
	}
	
	public void run()
	{
		IDemand oDemand = null;
		
		synchronized(this.oResultPool)
		{
			oDemand = this.oResultPool.get();
		}
		
		FileOutputStream oFileOut = null;
		BufferedWriter oWriter = null;
		
		if(oDemand != null)
		{
			File oDir = new File(this.strDirName);
			
			if(!oDir.isDirectory())
			{
				oDir.mkdir();
			}
			
			
			try 
			{
				oFileOut = new FileOutputStream(this.strFileName, true);
				oWriter = new BufferedWriter(new OutputStreamWriter(oFileOut, "ASCII"));
				
				while(oDemand != null)
				{
					TimeLine oTimeline = oDemand.getTimeLine();
					List<Entry<String, Date>> oTimeStamps = oTimeline.oTimeRecords;
					
					for(int i = 0; i<oTimeStamps.size(); i++)
					{
						Entry<String, Date> oTimeStamp = oTimeStamps.get(i);
						
						oWriter.write(oTimeStamp.getKey()+ ", " + oTimeStamp.getValue().getTime());
						
						if(i == (oTimeStamps.size() -1))
						{
							oWriter.newLine();
						}
						else
						{
							oWriter.write(", ");
						}
					}
						
					oWriter.flush();
					
					synchronized(this.oResultPool)
					{
						oDemand = this.oResultPool.get();
						GlobalDef.slNumProcessedDemands++;
						// Update the statistics info
						GlobalDef.soStatisticsUpdator.updateStatisticsInfo();
					}
				}
				
				oWriter.close(); 
				oFileOut.close();
			} 
			catch (IOException ex) 
			{
				GlobalDef.handleCriticalException(ex);
			}
			
		}
	}
}
