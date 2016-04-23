package gipsy.lang.context;

import gipsy.lang.GIPSYType;

import java.util.Vector;

import marf.util.FreeVector;

public class UnorderedFiniteNonPeriodicTagSet extends TagSet implements IUnordered, IFinite, INonPeriodic{

	public UnorderedFiniteNonPeriodicTagSet()
	{
		super();
	}
	public UnorderedFiniteNonPeriodicTagSet(FreeVector<GIPSYType> poEnmuneratedElement)
	{
		super.setEnmuneratedElement(poEnmuneratedElement);
	}
	@Override
	public boolean equals(Object poOtherObject) {
		boolean result=false;
		if(this.getClass()!=poOtherObject.getClass())
			return false;
		else
		{
			Vector<GIPSYType> copy=new Vector<GIPSYType>(((UnorderedFiniteNonPeriodicTagSet)poOtherObject).getEnmuneratedElement());
			if(this.getEnmuneratedElement().size()==copy.size())
			{			
				for(int i=0; i<this.getEnmuneratedElement().size(); i++)
				{
					for(int j=0; j<copy.size(); j++)
					{
						result=this.getEnmuneratedElement().elementAt(i).equals(copy.elementAt(j));
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

	/**
	 * {black, white, red} just get the next tag inside the enumerated tag set
	 */
	@Override
	public GIPSYType getNext(GIPSYType poCurrentTag) {
		return null;
		
	}

	@Override
	public boolean isInTagSet(GIPSYType poTag) {
		boolean result=false;
		for(int i=0; i<this.getEnmuneratedElement().size();i++)
		{
			if(this.getEnmuneratedElement().elementAt(i).equals(poTag))
			{
				result=true;
			    break;
			}
	    }
		return result;
	}

}
