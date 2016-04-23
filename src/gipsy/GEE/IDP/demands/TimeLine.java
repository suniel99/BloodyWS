package gipsy.GEE.IDP.demands;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class is used for demand statistics.
 * It represents an array of {TierId, timeTag} tuples. 
 * Each time a tier manipulates a demand, it tags it with its own TierId and its current 
 * system time (we make the assumption that the different nodes' clocks are approximately 
 * synchronized). Having this information, it will then be possible to trace the route 
 * of all demands, as well as infer their communication and processing times. 
 * 
 * @author Emil Vassev
 */
public class TimeLine 
implements Serializable 
{
	/**
	 * Constants
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The dynamic array to store the demand timeline.
	 */
	public LinkedList<Entry<String, Date>> oTimeRecords;
	
	public TimeLine()
	{
		oTimeRecords = new LinkedList<Entry<String, Date>>(); 
	}
	
	public void addTimeLine(TimeLine poTimeLine)
	{
		int iRecSize = poTimeLine.oTimeRecords.size(); 
		for (int i=0; i<iRecSize; ++i)
		{
			TimeLineRecord oCurrRecord = (TimeLineRecord) poTimeLine.oTimeRecords.get(i);
			this.oTimeRecords.add(new TimeLineRecord(oCurrRecord.getKey(), oCurrRecord.getValue()));
		}
	}
	
	public void addTimeLine(String pstrTierID)
	{
		oTimeRecords.add(new TimeLineRecord(pstrTierID, new Date()));
	}
	
	public Date[] getTimeLine(String pstrTierID)
	{
		Date[] aoDates;
		LinkedList<Date> oDateList = new LinkedList<Date>();
		
		for (int i=0; i<oTimeRecords.size(); ++i)
		{
			TimeLineRecord oCurrRecord = (TimeLineRecord) oTimeRecords.get(i);
			if (oCurrRecord.getKey() == pstrTierID)
				oDateList.add(oCurrRecord.getValue());
		}
		
		aoDates = (Date[])oDateList.toArray();
		
		return aoDates;
	}
	
	public String getTimeLine()
	{
		String strTimeLines = "";
		
		for (int i=0; i<oTimeRecords.size(); ++i)
		{
			TimeLineRecord oCurrRecord = (TimeLineRecord) oTimeRecords.get(i);
			strTimeLines += " >> ";
			strTimeLines += "Tier ID: " + oCurrRecord.getKey();
			strTimeLines += ", Time: " + oCurrRecord.getValue().getTime();
			strTimeLines += " << \r\n";
		}
		
		return strTimeLines;
	}
}

class TimeLineRecord 
implements Map.Entry<String, Date>, Serializable
{
	/**
	 * Constants
	 */
	public static final long serialVersionUID = 1L;
	
	private String strTierID;
	private Date oTierTime;
	
	public TimeLineRecord(String pstrTierID, Date poTierTime)
	{
		this.strTierID = pstrTierID;
		this.oTierTime = poTierTime;
	}
	
	public String getKey()
	{
		return strTierID;
	}

	public Date getValue()
	{
		return oTierTime;
	}
	
	public Date setValue(Date poTierTime)
	{
		this.oTierTime = poTierTime;
		return this.oTierTime;
	}
}