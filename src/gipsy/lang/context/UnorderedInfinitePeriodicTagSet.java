package gipsy.lang.context;

import gipsy.lang.GIPSYType;

import java.util.Vector;

import marf.util.FreeVector;

public class UnorderedInfinitePeriodicTagSet extends TagSet implements IUnordered, IInfinite, IPeriodic{

	public UnorderedInfinitePeriodicTagSet()
	{
		super();		
	}
	public UnorderedInfinitePeriodicTagSet(FreeVector<GIPSYType> poPeriod)
	{
		super.setPeriod(poPeriod);
	}
	
	
	@Override
	public boolean equals(Object poOtherObject)
	{
		boolean bResult = false;

		if(getClass() != poOtherObject.getClass())
		{
			return false;
		}
		else
		{
			Vector<GIPSYType> oSetCopy = new Vector<GIPSYType>
			(
				((UnorderedInfinitePeriodicTagSet)poOtherObject).getPeriod()
			);

			if(getPeriod().size() == oSetCopy.size())
			{			
				for(int i = 0; i < this.getPeriod().size(); i++)
				{
					for(int j = 0; j<oSetCopy.size(); j++)
					{
						bResult = getPeriod().elementAt(i).equals(oSetCopy.elementAt(j));

						if(bResult == true)
						{
							oSetCopy.remove(j);
						}

						// XXX: this break makes j++ above unreachable
						break;
					}
				}
			}
		}

		return bResult;
	}

	@Override
	public GIPSYType getNext(GIPSYType poCurrentTag) {
		return null;
	}

	@Override
	public boolean isInTagSet(GIPSYType poTag) {
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

}
