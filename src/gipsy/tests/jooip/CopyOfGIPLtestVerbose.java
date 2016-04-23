package gipsy.tests.jooip;
import gipsy.GIPC.GIPC;
import gipsy.lang.*;
import gipsy.lang.converters.type.*;
import gipsy.interfaces.*;
import gipsy.lang.context.Dimension;
import gipsy.GEE.GEE;
import gipsy.util.*;

import java.io.Serializable;
import java.lang.reflect.Method;

public class CopyOfGIPLtestVerbose  implements ISequentialThread
{
	private static GIPSYProgram soGEER1;
	private static GIPSYProgram soGEER2;
	private static GIPSYProgram soGEER3;
    private int N = 0;
	private boolean bNIsWritten = false;
	private GIPSYContext oContext;

	public GEE oGEE3 = new GEE(soGEER3);
	public GEE oGEE2 = new GEE(soGEER2);
	//public GEE oGEE1 = new GEE(soGEER1);

	static{
		try
		{
			//GIPC oGIPC1 = new GIPC(new StringInputStream("    				if (#.d) <= 0 then 1 else (N+1) @.d (#.d) - 1 fi                    where                      dimension d;                    end "), new String[] {"--gipl", "--debug"});
			GIPC oGIPC1 = new GIPC(new StringInputStream("    				if (#.d) <= 0 then 1 else (N+1) @.d (#.d) - 1 fi"), new String[] {"--gipl", "--debug"});
			oGIPC1.compile();
			soGEER1 = oGIPC1.getGEER();
			GIPC oGIPC2 = new GIPC(new StringInputStream(" N@.d f - 1 where dimension d; end "), new String[] {"--gipl", "--debug"});
			oGIPC2.compile();
			soGEER2 = oGIPC2.getGEER();
			GIPC oGIPC3 = new GIPC(new StringInputStream(" N@.d f + 1 where dimension d; end "), new String[] {"--gipl", "--debug"});
			oGIPC3.compile();
			soGEER3 = oGIPC3.getGEER();
		}
		catch(Exception e)
		{
			System.err.println(e);
			e.printStackTrace(System.err);
		}
	}

	public CopyOfGIPLtestVerbose(GIPSYContext poContext)
	{
		this.oContext = poContext;
	}

	public CopyOfGIPLtestVerbose()
	{
	}

	public Serializable work()
	{
		CopyOfGIPLtestVerbose.main(null);
		return null;
	}

	public Serializable getWorkResult()
	{
		return null;
	}


	public void setMethod(Method poSTMethod)
	{
	}


	public void run()
	{
		work();
	}
/**
 * @version $Id: CopyOfGIPLtestVerbose.java,v 1.3 2009/09/30 10:47:20 mokhov Exp $
 */


    public int computeLocalAverage(int f)
    {
		this.oContext = new GIPSYContext();
		Dimension oDimension = new Dimension();
		oDimension.setDimensionName(new GIPSYIdentifier("d"));
		oDimension.setCurrentTag(new GIPSYInteger(f));
		this.oContext.addDimension(oDimension);
		
		soGEER2.getDictionary().addAll(soGEER1.getDictionary());
		soGEER3.getDictionary().addAll(soGEER1.getDictionary());

		//soGEER1.getDictionary().addAll(soGEER2.getDictionary());
		//soGEER2 = soGEER1;

		System.out.println("----------------------------------------");
		soGEER1.getAbstractSyntaxTrees()[0].showTree();
		System.out.println("----------------------------------------");
		soGEER2.getAbstractSyntaxTrees()[0].showTree();
		System.out.println("----------------------------------------");
		soGEER3.getAbstractSyntaxTrees()[0].showTree();
		System.out.println("----------------------------------------");

		System.out.println(soGEER1.getAbstractSyntaxTrees()[0].getRoot().jjtGetChild(0));
		System.out.println("----------------------------------------");
		
		System.out.println(soGEER2.getAbstractSyntaxTrees()[0].getRoot().jjtGetChild(0).jjtGetChild(2));
		System.out.println("----------------------------------------");
		
		soGEER2.getAbstractSyntaxTrees()[0].getRoot().jjtGetChild(0).jjtGetChild(2).jjtAddChild(soGEER1.getAbstractSyntaxTrees()[0].getRoot().jjtGetChild(0), 3);

		System.out.println("----------------------------------------");
		soGEER2.getAbstractSyntaxTrees()[0].showTree();
		System.out.println("----------------------------------------");
		
       return  ( IPLToJava.convertToInteger(this.oGEE2.eval(this.oContext))
        	   + IPLToJava.convertToInteger(this.oGEE3.eval(this.oContext))) / 2;
    }

    public void print()
    {
        System.out.println("N=" + N);
    }

    public static void main(String[] argv)
    {
        CopyOfGIPLtestVerbose oTest = new CopyOfGIPLtestVerbose();        
        oTest.N = oTest.computeLocalAverage(2); 
        oTest.print();
    }
}

