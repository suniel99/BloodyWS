package gipsy.lang.context.forensic;

import java.util.Vector;

import marf.util.FreeVector;
import gipsy.lang.GIPSYContext;
import gipsy.lang.GIPSYType;

/**
 * @author serguei
 */
public class GIPSYForensicContext
extends GIPSYContext
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2947693543537351367L;

	public GIPSYForensicContext() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GIPSYForensicContext(FreeVector<GIPSYType> poSet) {
		super(poSet);
		// TODO Auto-generated constructor stub
	}

	public GIPSYForensicContext(GIPSYContext poOtherContext) {
		super(poOtherContext);
		// TODO Auto-generated constructor stub
	}

	public GIPSYForensicContext(int piContextType, FreeVector<GIPSYType> poSet) {
		super(piContextType, poSet);
		// TODO Auto-generated constructor stub
	}

	public GIPSYForensicContext(int piContextType) {
		super(piContextType);
		// TODO Auto-generated constructor stub
	}


}
