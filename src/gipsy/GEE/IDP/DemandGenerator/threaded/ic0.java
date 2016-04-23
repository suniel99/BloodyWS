package gipsy.GEE.IDP.DemandGenerator.threaded;

// the thread ic is executed due to its definition
// each identifier in a lucid program has its own definition, which should be convert into a separate class

public final class ic0 extends IdentifierContext {

  public ic0(int iccode, int icname, int[] k) {
    super(iccode, icname,k);
  }

/*
A = if #.d == 0 then 1 else
			(if (xx_tag1 <= yy_tag1) then xx_tag1 else yy_tag1 ) @.d (#.d-1);

 identifier(char)    id(int)
      A                 0
      xx_tag1           1
      w                 2
      yy_tag1           3
      w(the second)     4
*/
  public void run() {
    System.err.println( Thread.currentThread().getName()+",ic_code:"+hcode+", " + name + ", " + cont[0]);
    if ( cont[0] == 0 ) {
      value = 1;

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

      ThreadedClient.ht.setValue(hcode, value );
    }
    else {
      cont[0] = cont[0] - 1;

      int h0 = ThreadedClient.ht.demand(1,cont);
      int h1 = ThreadedClient.ht.demand(3,cont);

      int v1 = ThreadedClient.ht.getValue(h0);
      int v2 = ThreadedClient.ht.getValue(h1);

      if ( v1  <= v2 )
        value = v1;
      else
        value = v2;
    }
    ready = true;
 }
}