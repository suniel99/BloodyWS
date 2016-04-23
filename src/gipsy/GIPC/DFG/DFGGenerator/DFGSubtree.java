package gipsy.GIPC.DFG.DFGGenerator;

/*
 * DFGSubtree.java
 *
 */

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * This class is facet class called by DFGCodeGenerator
 *
 * @version 1.0, by GIPSY project team.
 * @author  <a href="mailto:dingyimin@zworg.com">Yimin Ding</a>
 */

public class DFGSubtree {
  private Hashtable<String, DFGSubtreeContents> SBT;
  private DFGSubtreeContents current;
  private int nterm, used;

  public DFGSubtree() {
    SBT = new Hashtable<String, DFGSubtreeContents>();
    nterm = 1;
    used = 0;
  }

  public int accVar(String lname, String assign) {
    int count = -1;
    String Aname = lname;
    String Sname = getSname(Aname);
    if (Sname == "~") { // is dim
      Sname = Aname;

    }
    if (!SBT.isEmpty()) {
      DFGSubtreeContents sbt2;

      do { // begin find
        if (!SBT.containsKey(Aname)) { // if not find
          if (assign == "L") { // assign left -> create a new one
            nterm++;
            count = nterm;
            used = 1; // has been used(be defined)
            current = new DFGSubtreeContents(count, used);
            SBT.put(lname, current);
          }
          else {
            if (getPname(getPname(Aname)) == "~") {
              Aname = Sname;
            }
            else {
              Aname = getPname(getPname(Aname)) + " " + Sname;

            }
          }
        }
        else { // item exist

          sbt2 = SBT.get(Aname);
          count = sbt2.nterm;
          if (assign == "L") {
            sbt2.used = 1;
          }
        }
      }
      while (count == -1 && Aname.charAt(0) != '$');

      if (count == -1) { // it is new one and not assign left
        nterm++;
        count = nterm;
        used = 0;
        current = new DFGSubtreeContents(count, used);
        SBT.put(lname, current);
      }
    }
    else { // ht is empty
      nterm++;
      count = nterm;

      if (assign == "L") {
        used = 1;
      }
      else {
        used = 0;
      }
      current = new DFGSubtreeContents(count, used);
      SBT.put(lname, current);
    }
    return count;
  }

  public boolean findVar(String lname) { // if not , return false
    return SBT.containsKey(lname);
  }

  public Enumeration<String> listKeys() {
    Enumeration<String> e = SBT.keys();
    return e;
  }

  public boolean IsUsed(String lname) {
    boolean isused = false;
    if (!SBT.isEmpty()) {
      DFGSubtreeContents sbt2 = SBT.get(lname);
      if (sbt2 == null) { //if new one
        isused = false;
      }
      else { // item exist
        if (sbt2.used == 1) { // has been used
          isused = true;
        }
        else {
          isused = false;
        }
      }
    }
    else { // ht is empty
      isused = false;
    }
    return isused;
  }

  public int reqN() {
    nterm = nterm + 1;
    return nterm;
  }

  public String getPname(String lname) {
    if (lname == "~") {
      return "$";
    }
    else {
      int ind = lname.lastIndexOf(' ');
      if (ind != -1) {
        return lname.substring(0, ind);
      }
      else {
        return "~";
      }
    }
  }

  public String getSname(String lname) {
    int ind = lname.lastIndexOf(' ');
    if (ind != -1) {
      return lname.substring(ind + 1);
    }
    else {
      return "~";
    }
  }
}
