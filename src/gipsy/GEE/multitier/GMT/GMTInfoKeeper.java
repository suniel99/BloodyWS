/**
 * 
 */
package gipsy.GEE.multitier.GMT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import gipsy.GEE.multitier.TierIdentity;
import gipsy.GEE.multitier.GMT.demands.DGTRegistration;
import gipsy.GEE.multitier.GMT.demands.DWTRegistration;
import gipsy.GEE.multitier.GMT.demands.NodeRegistration;
import gipsy.GEE.multitier.GMT.demands.DSTRegistration;
import gipsy.GEE.multitier.GMT.demands.TierRegistration;

/**
 * Stores all the information that is need to be stored by the GMT.
 * 
 * @author Yi Ji
 * @version $Id: GMTInfoKeeper.java,v 1.9 2011/01/26 05:11:33 ji_yi Exp $
 */
public class GMTInfoKeeper 
{
	List<NodeRegistration> oNodeRegistrations = new ArrayList<NodeRegistration>();
	List<DSTRegistration> oDSTRegistrations = new ArrayList<DSTRegistration>();
	Map<String, DSTRegistration> oNodeSysTARelation = new HashMap<String, DSTRegistration>();
	Map<TierRegistration, DSTRegistration> oDGTDWTRegistration = new HashMap<TierRegistration, DSTRegistration>();
	
	public synchronized void saveNodeRegistration(NodeRegistration poRegistration, DSTRegistration poSysDST)
	{
		String strNodeID = poRegistration.getNodeID();
		
		boolean bIsNodeUpdated = false;
		
		for(int i = 0; i<this.oNodeRegistrations.size(); i++)
		{
			NodeRegistration oNodeReg = this.oNodeRegistrations.get(i);
			if(oNodeReg.getNodeID().equals(strNodeID))
			{
				this.oNodeRegistrations.set(i, poRegistration);
				bIsNodeUpdated = true;
				break;
			}
		}
		
		if(!bIsNodeUpdated)
		{
			this.oNodeRegistrations.add(poRegistration);
		}
		
		if(poSysDST != null)
		{
			synchronized(poSysDST)
			{
				int iActiveConnectCount = poSysDST.getActiveConnectionCount() + 1;
				poSysDST.setActiveConnectionCount(iActiveConnectCount);
			}
		}
		this.oNodeSysTARelation.put(poRegistration.getNodeID(), poSysDST);
	}
	
	public void saveTierRegistration(TierRegistration poRegistration, DSTRegistration poDataDST)
	{
		if(poRegistration instanceof DSTRegistration)
		{
			DSTRegistration oNewDSTReg = (DSTRegistration) poRegistration;
			synchronized(this.oDSTRegistrations)
			{
				for(int i = 0; i<this.oDSTRegistrations.size(); i++)
				{
					DSTRegistration oDSTReg = this.oDSTRegistrations.get(i);
					if(oDSTReg.getNodeID().equals(oNewDSTReg.getNodeID()) 
							&& oDSTReg.getTierID().equals(oNewDSTReg.getTierID()))
					{
						this.oDSTRegistrations.set(i, oNewDSTReg);
						return;
					}
				}
				this.oDSTRegistrations.add((DSTRegistration) poRegistration);
			}
		}
		else if(poRegistration instanceof DGTRegistration ||
				poRegistration instanceof DWTRegistration)
		{
			if(poDataDST != null)
			{
				synchronized(poDataDST)
				{
					int iActiveConnectCount = poDataDST.getActiveConnectionCount() + 1;
					poDataDST.setActiveConnectionCount(iActiveConnectCount);
				}
				
			}
			synchronized(this.oDGTDWTRegistration)
			{
				
				this.oDGTDWTRegistration.put(poRegistration, poDataDST);
			}
		}
	}
	
	
	public synchronized void updateSysDSTRegistration(DSTRegistration poDSTReg)
	{
		synchronized(this.oDSTRegistrations)
		{
			for(int i = 0; i < this.oDSTRegistrations.size(); i++)
			{
				DSTRegistration oDSTReg = this.oDSTRegistrations.get(i);
				
				if(oDSTReg.getNodeID().equals(poDSTReg.getNodeID()) 
						&& oDSTReg.getTierID().equals(poDSTReg.getTierID()))
				{
					this.oDSTRegistrations.set(i, poDSTReg);
				}
			}
		}
		
		Set<Entry<String, DSTRegistration>> oEntrySet = 
			this.oNodeSysTARelation.entrySet();
		
		Iterator<Entry<String, DSTRegistration>> oIterator = 
			oEntrySet.iterator();
		
		while(oIterator.hasNext())
		{
			Entry<String, DSTRegistration> oEntry = oIterator.next();
			if(oEntry.getValue().equals(poDSTReg))
			{
				System.out.println("sysDST updated");
				oEntry.setValue(poDSTReg);
			}
		}
		
	}
	
	public synchronized void removeRegistration(String pstrNodeID, String pstrTierID, TierIdentity poIdentity)
	{
		if(pstrTierID == null)
		{
			// Remove the entire node
		}
		else if(pstrNodeID != null && pstrTierID != null && poIdentity != null)
		{
			// Remove the tier registration
			switch(poIdentity)
			{
			case DST:
				synchronized(this.oDSTRegistrations)
				{
					for(DSTRegistration oRegistration : this.oDSTRegistrations)
					{
						if(oRegistration.getNodeID().equals(pstrNodeID) 
								&& oRegistration.getTierID().equals(pstrTierID))
						{
							boolean bRemoved = this.oDSTRegistrations.remove(oRegistration);
							
							while(bRemoved)
							{
								bRemoved = this.oDSTRegistrations.remove(oRegistration);
							}
							
							synchronized(this.oDGTDWTRegistration)
							{
								Set<Entry<TierRegistration, DSTRegistration>> oEntrySet = 
									this.oDGTDWTRegistration.entrySet();
								
								Iterator<Entry<TierRegistration, DSTRegistration>> oIterator = 
									oEntrySet.iterator();
								
								while(oIterator.hasNext())
								{
									Entry<TierRegistration, DSTRegistration> oEntry = oIterator.next();
									if(oEntry.getValue().equals(oRegistration))
									{
										oEntry.setValue(null);
									}
								}
							}
							
							Set<Entry<String, DSTRegistration>> oEntrySet = 
								this.oNodeSysTARelation.entrySet();
							
							Iterator<Entry<String, DSTRegistration>> oIterator = 
								oEntrySet.iterator();
							
							while(oIterator.hasNext())
							{
								Entry<String, DSTRegistration> oEntry = oIterator.next();
								if(oEntry.getValue().equals(oRegistration))
								{
									oEntry.setValue(null);
								}
							}
							break;
						}
					}
				}
				break;
			default: // DGT or DWT
				synchronized(this.oDGTDWTRegistration)
				{
					Set<Entry<TierRegistration, DSTRegistration>> oEntrySet = 
						this.oDGTDWTRegistration.entrySet();
					
					Iterator<Entry<TierRegistration, DSTRegistration>> oIterator = 
						oEntrySet.iterator();
					
					while(oIterator.hasNext())
					{
						Entry<TierRegistration, DSTRegistration> oEntry = oIterator.next();
						
						TierRegistration oReg = oEntry.getKey();
						
						if(oReg.getNodeID().equals(pstrNodeID) && oReg.getTierID().equals(pstrTierID))
						{
							if((oReg instanceof DGTRegistration && poIdentity == TierIdentity.DGT) || 
									(oReg instanceof DWTRegistration && poIdentity == TierIdentity.DWT))
							{
								DSTRegistration oDataDSTReg = oEntry.getValue();
								
								if(oDataDSTReg != null)
								{
									synchronized(oDataDSTReg)
									{
										int iActiveConnectCount = oDataDSTReg.getActiveConnectionCount() - 1;
										oDataDSTReg.setActiveConnectionCount(iActiveConnectCount);
									}
								}
								oIterator.remove();
								break;
							}
						}
					}
				}
			}
		}
	}
	
	public List<DSTRegistration> getDSTRegistrations(int piFromIndex, int piToIndex)
	{
		if(piToIndex >= this.oDSTRegistrations.size())
		{
			return this.oDSTRegistrations.subList(piFromIndex, this.oDSTRegistrations.size());
		}
		else
		{
			return this.oDSTRegistrations.subList(piFromIndex, piToIndex);
		}
	}
	
	public List<DSTRegistration> getDSTRegistrations()
	{
		return this.oDSTRegistrations;
	}
	
	public DSTRegistration getNodeSysDST(String pstrNodeID)
	{
		return this.oNodeSysTARelation.get(pstrNodeID);
	}
	
	public List<NodeRegistration> getNodeRegistrations(int piFromIndex, int piToIndex)
	{
		if(piToIndex >= this.oNodeRegistrations.size())
		{
			return this.oNodeRegistrations.subList(piFromIndex, this.oNodeRegistrations.size());
		}
		else
		{
			return this.oNodeRegistrations.subList(piFromIndex, piToIndex);
		}
	}
	
	public int getNodeRegistrationsSize()
	{
		return this.oNodeRegistrations.size();
	}
	
	public synchronized String getNodeIDfromRegistration(NodeRegistration poNodeReg)
	{
		String strNodeID = null;
		
		for(int i = 0; i<this.oNodeRegistrations.size(); i++)
		{
			NodeRegistration oNodeReg = this.oNodeRegistrations.get(i);
			if(oNodeReg.getHostName().equals(poNodeReg.getHostName()))
			{
				strNodeID = oNodeReg.getNodeID();
				break;
			}
		}
		
		return strNodeID;
	}
	
	public void removeDGTDWTRegistration(String pstrNodeID)
	{
		synchronized(this.oDGTDWTRegistration)
		{
			Set<Entry<TierRegistration, DSTRegistration>> oEntrySet = 
				this.oDGTDWTRegistration.entrySet();
			
			Iterator<Entry<TierRegistration, DSTRegistration>> oIterator = 
				oEntrySet.iterator();
			
			while(oIterator.hasNext())
			{
				Entry<TierRegistration, DSTRegistration> oEntry = oIterator.next();
				
				TierRegistration oReg = oEntry.getKey();
				
				if(oReg.getNodeID().equals(pstrNodeID))
				{
					DSTRegistration oDataDSTReg = oEntry.getValue();
					
					if(oDataDSTReg != null)
					{
						synchronized(oDataDSTReg)
						{
							int iActiveConnectCount = oDataDSTReg.getActiveConnectionCount() - 1;
							oDataDSTReg.setActiveConnectionCount(iActiveConnectCount);
						}
					}
					
					oIterator.remove();
				}
			}
		}
	}
	
	public DSTRegistration getDSTRegistration(String pstrNodeID, String pstrTierID)
	{
		synchronized(this.oDSTRegistrations)
		{
			for(int i = 0; i<this.oDSTRegistrations.size(); i++)
			{
				DSTRegistration oDSTReg = this.oDSTRegistrations.get(i);
				if(oDSTReg.getNodeID().equals(pstrNodeID) 
						&& oDSTReg.getTierID().equals(pstrTierID))
				{
					return oDSTReg;
				}
			}
			return null;
		}
	}
}
