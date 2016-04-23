package gipsy.GEE.IDP.DemandGenerator.threaded;

public final class ic1 extends IdentifierContext {

   public ic1(int iccode, int icname, int[] k) {
    super(iccode, icname,k);
  }

// the thread ic is executed due to its definition
// each identifier in a lucid program has its own definition, which should be convert into a separate class

/*
  xx_tag1 = 2*A @.d W

 identifier(char)    id(int)
      A                 0
      xx_tag1           1
      w                 2
      yy_tag1           3
      w(the second)     4
*/

  public void run() {
    System.err.println( Thread.currentThread().getName()+",ic_code:" + hcode + ", " + name + ", " + cont[0]);
    int h0 = ThreadedClient.ht.demand(2, cont);
    cont[0] = ThreadedClient.ht.getValue(h0) ;
    System.err.println( Thread.currentThread().getName()+", icx: " + name + " value_cont: " + cont[0]);
    int h1 = ThreadedClient.ht.demand(0, cont);
    value = ThreadedClient.ht.getValue(h1) *2 ;
    ready = true;
 }
}