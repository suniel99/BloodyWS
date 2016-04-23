package gipsy.GIPC.DFG.DFGGenerator;

/**
 * This class is interface for the table
 *
 * @version 1.0, by GIPSY project team.
 * @author  <a href="mailto:dingyimin@zworg.com">Yimin Ding</a>
 */
public class DFGSubtreeContents 
{
  int nterm;
  int used;

  public DFGSubtreeContents() {
  }

  public DFGSubtreeContents(int nterm1, int used1) {
    nterm = nterm1;
    used = used1;
  }
}
