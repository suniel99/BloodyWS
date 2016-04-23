package gipsy.lang.context;
import gipsy.lang.GIPSYType;
import marf.util.FreeVector;
public class OrderedFinitePeriodicTagSet extends TagSet implements IOrdered, IFinite, IPeriodic{
	public OrderedFinitePeriodicTagSet()
	{
		super();
	}
	public OrderedFinitePeriodicTagSet(FreeVector<GIPSYType> poPeriod, int piTimes)
	{
		
		super.setPeriod(poPeriod);
		super.setTimes(piTimes);
	}
	
	//period and times
	public boolean equals(Object poOtherObject)
	{
		if(getClass() != poOtherObject.getClass())
		{
			return false;
		}
		else
		{
			return(this.getPeriod().equals(((OrderedFinitePeriodicTagSet)poOtherObject).getPeriod())
					&& this.getTimes()==((OrderedFinitePeriodicTagSet)poOtherObject).getTimes());
		}
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
		GIPSYType result=null; //get null if there's a semantic error.
		int currentIndex=this.getPeriod().indexOf(poCurrentTag); //the index of the current tag
		
		//There should be an error message, but how to get the line number???
		if(isInTagSet(poCurrentTag))
		{		
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
	
	public String toString()
	{
		String subString=this.getPeriod().elementAt(0).toString();
		for(int i=1;i<this.getPeriod().size();i++)
		{
			subString=subString+", "+this.getPeriod().elementAt(i);
		}
		return("{<"+subString+"> : "+this.getTimes()+"}");
	}

}
