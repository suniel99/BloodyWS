package gipsy.lang.context;

import gipsy.lang.GIPSYType;
import marf.util.FreeVector;

public class OrderedInfinitePeriodicTagSet  extends TagSet implements IOrdered, IInfinite, IPeriodic{
	public OrderedInfinitePeriodicTagSet()
	{
		super();
	}
	public OrderedInfinitePeriodicTagSet(FreeVector<GIPSYType> poPeriod)
	{
		super.setPeriod(poPeriod);
	}
	
	public boolean equals(Object poOtherObject)
	{
		if(getClass() != poOtherObject.getClass())
		{
			return false;
		}
		else
		{
			return(this.getPeriod().equals(((OrderedInfinitePeriodicTagSet)poOtherObject).getPeriod()));
		}
	}
	
	public boolean isInTagSet(GIPSYType poTag)
	{
		boolean result=false;
		for(int i=0; i<this.getPeriod().size();i++)
		{
			if(this.getPeriod().elementAt(i).equals(poTag))
			{
				result=true;
				break;
			}
		}
		return result;
	}
	
	public GIPSYType getNext(GIPSYType poCurrentTag)
	{
		GIPSYType result=null;
		int currentIndex=this.getPeriod().indexOf(poCurrentTag); //the index of the current tag
		
		//There should be an error message, but how to get the line number???
		if(!isInTagSet(poCurrentTag))
		{
			System.out.println("Semantic Error: The given tag is not in the tag set, cannot get next.");
		}
		else{
			if(currentIndex==this.getPeriod().size()-1) //if it is the last element in a period, repeat the first one.
			{
				result=(GIPSYType)this.getPeriod().elementAt(0);
			}
			else
			{
				result=(GIPSYType)this.getPeriod().elementAt(currentIndex+1);
			}
			
         }
	
		return result;
	}
	

}
