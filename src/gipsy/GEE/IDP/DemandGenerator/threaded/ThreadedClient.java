package gipsy.GEE.IDP.DemandGenerator.threaded;

import java.util.*;

import gipsy.GEE.CONFIG;

/**
 * TODO: document and refactor
 * 
 * @author Paula
 */
public class ThreadedClient
{
	// TODO: shall it public??
	public static DemandHashtable ht = new DemandHashtable();

	public ThreadedClient()
	{
		int icx = 0;

		int contxt[] = new int[CONFIG.DIMENSION_MAX];

		for(int i = 0; i < CONFIG.DIMENSION_MAX; i++)
			contxt[i] = -1;

		contxt[0] = 4;

		int h1 = ht.demand(icx, contxt);

		/*    try {
		      Thread.sleep( 300 );
		    }
		    catch (InterruptedException e ) {
		      System.err.println( e.toString() );
		    }

		*/
		System.out.println(ht.getValue(h1));

		try
		{
			Thread.sleep(1000);
		}
		catch(InterruptedException e)
		{
			System.err.println(e);
		}

		for(int i = 0; i < 50; i++)
			if(ht.entries[i] != null)
				System.err.println
				(
					"code: "
					+ i
					+ ","
					+ ht.entries[i].getName()
					+ ","
					+ ht.entries[i].getCont()[0]
					+ " value:"
					+ ht.entries[i].getValue()
				);
	}

	public static void main(String[] args)
	{
		Date d = new Date();

		new ThreadedClient();

		Date d2 = new Date();
		System.err.println("Timing: " + Long.toString(d2.getTime() - d.getTime()));
	}
}

// EOF
