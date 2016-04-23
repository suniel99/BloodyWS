package gipsy.GIPC.DFG.DFGAnalyzer;

import java.util.Hashtable;

/**
 * This class is interface for table
 *
 * @version 1.0, by GIPSY project team.
 * @author  <a href="mailto:dingyimin@zworg.com">Yimin Ding</a>
 */
public class LucidNodeItem {
  public String ID;
  public String Label;
  public String Shape;
  public String Scope;
  public String Scope_label;
  public LucidNodeItem previous;
  public Hashtable ht=new Hashtable();
  public int processed=0;
  public int graphlevel=0;


  public LucidNodeItem() {
    this.ID=" ";
    this.Label=" ";
    this.Shape="box";
    this.Scope=" ";
    this.Scope_label=" ";
    this.previous=null;
    this.graphlevel=0;
//    this.ht=null;

  }

  public LucidNodeItem(String ID, String Label, String Shape, String Scope,
                     String Scope_label, LucidNodeItem previous, boolean child) {
    this.ID=ID;
    this.Label=Label;
    this.Shape=Shape;
    this.Scope=Scope;
    this.Scope_label=Scope_label;
    this.previous=previous;
    if(child) {
      ht=new Hashtable();
    }

  }

  public void setprocessed(int processed) {
    this.processed=processed;
  }

  public void setScope_label(String Scope_label) {
    this.Scope_label=Scope_label;
  }

  public void setprevious(LucidNodeItem previous) {
    this.previous=previous;
  }

  public void setht(String name, LucidNodeItem current) {
    this.ht.put(name, current);
  }

  public void setID(String ID) {
    this.ID=ID;
  }

  public void setLabel(String Label) {
    this.Label=Label;
  }

  public void setShape(String Shape) {
    this.Shape=Shape;
  }

  public void setScope(String Scope) {
    this.Scope=Scope;
  }

}
