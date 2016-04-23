package gipsy.tests.GEE.simulator;

import marf.util.BaseThread;


/**
 * This is the thread, which keeps the GUI running. But as it runs too
 * frequently keeping the CPU busy, it tends to slow down the entire
 * GUI. So it is revised to be a simple statistics info updater for the 
 * GUI and is started whenever the statistics info is changed.
 * 
 * @author Emil Vassev
 * @author Yi Ji
 * @version $Id: GUIThread.java,v 1.8 2011/01/10 16:51:10 ji_yi Exp $
 * @since
 */
public class GUIThread
extends BaseThread
{	
	private DGTDialog oParentDlg;
	
	/**
	 * Create a GUI statistics updater
	 * @param poParentDlg
	 */
	public GUIThread(DGTDialog poParentDlg)
	{
		this.oParentDlg = poParentDlg;
	}
	
	/**
	 * XXX
	 */
	private void updateGUI()
	{
		synchronized(this.oParentDlg.getTAStatistics())
		{
			String strStatisticsInfo;
			String strProfiles = "";
			String strCurrentProfile = "";
			
			for(int i = 0; i < GlobalDef.soRunProfiles.size(); ++i)
			{
				strProfiles += GlobalDef.soRunProfiles.get(i) + ", ";
				
				if(i == (GlobalDef.soRunProfiles.size() - 1))
				{
					strCurrentProfile = GlobalDef.soRunProfiles.get(i);
				}
			}

			if(!strProfiles.equals(""))
			{
				strProfiles = strProfiles.substring(0, strProfiles.lastIndexOf(", "));
			}
			
			// XXX: use StringBuffer or StringBuilder
			strStatisticsInfo
				= "Pending Demands: " + GlobalDef.slNumPendingDemands + GlobalDef.CR + GlobalDef.LF
				+ "Computed Demands: " + GlobalDef.slNumComputedDemands + GlobalDef.CR + GlobalDef.LF
				+ "Processed Demands: " + GlobalDef.slNumProcessedDemands + GlobalDef.CR + GlobalDef.LF 
				+ "Current Profile: " + strCurrentProfile + GlobalDef.CR + GlobalDef.LF
				+ "Run Profiles: " + strProfiles;

			this.oParentDlg.getTAStatistics().setText(strStatisticsInfo);
		}
	}
	
	/**
	 * Update the statistics info
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{
		
		while(true)
		{
			synchronized(this)
			{
				try 
				{
					this.wait(5000);
				}
				catch (InterruptedException oInterrupted)
				{
					oInterrupted.printStackTrace();
				}
				// Update statistics info
				updateGUI();
				
				//**** refreshes the dialog window
				this.oParentDlg.repaint();
			}
		}
	}
	
	public void updateStatisticsInfo()
	{
		synchronized(this)
		{
			this.notify();
		}
	}
}

// EOF
