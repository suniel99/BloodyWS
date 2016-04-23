package gipsy.lang.context;

import gipsy.lang.GIPSYType;

import java.util.Vector;

import marf.util.FreeVector;

public class UnorderedFinitePeriodicTagSet extends TagSet implements IUnordered, IFinite, IPeriodic{
	public UnorderedFinitePeriodicTagSet()
	{
		super();		
	}
	public UnorderedFinitePeriodicTagSet(FreeVector<GIPSYType> pPeriod, int pTimes)
	{
		super.setPeriod(pPeriod);
		super.setTimes(pTimes);
		
	}
	
	//Two UnorderedFinitePeriodicTagSet are equal if they have the equal periods. And since it's unordered, <black, white> and <white, black> are the same.
	public boolean equals(Object poOtherObject)
	{
		boolean result=false;
		if(this.getClass()!=poOtherObject.getClass())
			return false;
		else
		{
			Vector<GIPSYType> copy=new Vector<GIPSYType>(((UnorderedFinitePeriodicTagSet)poOtherObject).getPeriod());
			if(this.getPeriod().size()==copy.size() && this.getTimes()==((UnorderedFinitePeriodicTagSet)poOtherObject).getTimes())
			{			
				for(int i=0; i<this.getPeriod().size(); i++)
				{
					for(int j=0; j<copy.size(); j++)
					{
						result=this.getPeriod().elementAt(i).equals(copy.elementAt(j));
						if(result == true)
						{
							copy.remove(j);
						}
						break;
				    }
			    }				
			}
		}
		return result;		
	}
	
	public boolean isInTagSet(GIPSYType poTag)
	{
		int length=this.getPeriod().size();
		boolean result=false;
		for(int i=0; i<length; i++)
		{
			if(this.getPeriod().elementAt(i).equals(poTag))
			{
				result=true;
				break;
			}
		}
		return result;
	}
	
	/*
	 * With the given current tag, return the next tag inside the tagset. 
	   First should check if the current tag is inside the period
	 */
	public GIPSYType getNext(GIPSYType poCurrentTag)
	{
		return null;
	 }
	
}
