package gipsy.lang.context;

import gipsy.lang.GIPSYType;

/**
 * Up till now, did not see any way to use it
 * @author tongxin
 *
 */
public class UnorderedInfiniteNonPeriodicTagSet extends TagSet implements IUnordered, IInfinite, INonPeriodic {

	@Override
	public boolean equals(Object poOtherObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GIPSYType getNext(GIPSYType poCurrentTag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInTagSet(GIPSYType poTag) {
		// TODO Auto-generated method stub
		return false;
	}

}
