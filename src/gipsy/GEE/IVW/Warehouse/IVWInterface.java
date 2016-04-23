package gipsy.GEE.IVW.Warehouse;

import gipsy.storage.Dictionary;

/**
 * Intensional Value House Interface.
 * 
 * @author Lei Tao
 * @author Refactoring, integration, and documentation by Serguei Mokhov
 */
public interface IVWInterface
{
	/**
	 * XXX: I don;t what
	 * @param pstrDictSemantic
	 * @param pstrFilename
	 */
	void initIVW(Dictionary pstrDictSemantic, String pstrFilename);
	
	void setupIVW(String pstrFilename);
	
	void stopIVW();
	
	Object getValue(String pstrDemand);
	
	int setValue(String pstrDemand, Object oValue);
	
	String getDataFile(String pstrFilename);
	
	void setGCAlgorithm(String a);
	
	int viewSet();
	
	int loadFile();
}

// EOF
