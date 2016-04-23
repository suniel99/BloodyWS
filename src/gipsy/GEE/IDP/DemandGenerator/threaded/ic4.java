package gipsy.GEE.IDP.DemandGenerator.threaded;

public final class ic4 extends IdentifierContext{

  public ic4 ( int iccode, int icname, int[] k) {
    super ( iccode, icname, k);
  }
  /*
  W = if #.d == 0 then 0 else
	( if (yy_tag1 <= xx_tag1) then W+1 else W) @.d (#.d-1);

  identifier(char)    id(int)
      A                 0
      xx_tag1           1
      w                 2
      yy_tag1           3
      w(the second)     4

  */

  public void run() {
    System.err.println( Thread.currentThread().getName() + " run,ic_code:" + hcode + ", " + name + ", " + cont[0]);

    if ( cont[0] == 0 ) {
      value = 0;
// here is the similar to the base case of a recursive call
// after the execution reach this point, it could awaken the other suspending ics along the chain (may be a net)
// call the ht's setValue to store the value and notify related threads
// setValue( int hcode, int value )

// ???it happened that it tries to setaValue before the entry is created.

      while ( ThreadedClient.ht.entries [ hcode ] == null )
      {
        System.err.println("???? situation");
        try {
          Thread.sleep(100);
        }
        catch (InterruptedException e ) {
          System.err.println( e.toString() );
        }
      }

      ThreadedClient.ht.setValue( hcode, value );
    }
    else {
      cont[0] = cont[0] - 1;

      System.err.println( Thread.currentThread().getName()+", ic_ht_demand_context "+cont);

      int h0 = ThreadedClient.ht.demand(3,cont);
      int h1 = ThreadedClient.ht.demand(1,cont);

      int v1 = ThreadedClient.ht.getValue(h0);
      int v2 = ThreadedClient.ht.getValue(h1);

      if ( v1  <= v2 ) {
        int h2 = ThreadedClient.ht.demand(4,cont);
        value = ThreadedClient.ht.getValue(h2) + 1;
      }
      else {
        int h3 = ThreadedClient.ht.demand(4,cont);
        value = ThreadedClient.ht.getValue(h3) ;
      }
    }
    ready = true;
 }
}