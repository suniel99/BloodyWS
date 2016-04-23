package gipsy.GEE.IVW.Warehouse;

import gipsy.GEE.IVW.GarbageCollector.*;
import gipsy.storage.Dictionary;

import java.io.IOException;
import java.util.HashMap;

/**
 * The controller of the value warehouse. It provides all the services to  
 * other components of the system.
 */
public class IVWControl
implements IVWInterface
{
	private GarbageCollector gc;
	private Cache cache;
	private NetCDFDataManager cdfdata;
	private NetCDFFileManager cdffile;
	private String gcAlgorithm = "MarkSweep";
	private int gctimes = 0;

	public IVWControl()
	{
	}

  /**
   * Initialize the Value Warehouse netcdf file. Get the name of lucid program
   * and read the data from data dictionary.
   *
   * @param dictSemantic Dictionary - which contain the dimensions and variables
   * @param filename String - the name of the lucid program file
   */
  public void initIVW(Dictionary dictSemantic, String filename) {
    cdffile = new NetCDFFileManager(filename);
    cdffile.initNetCDF(dictSemantic, filename);
  }

  /**
   * Set up the Value Warehouse. Load the netcdf file to memory, then initialize
   * cache and netcdf data set.
   *
   * @param filename String - netcdf file that contain the definition
   * @return int
   */
  public void setupIVW(String filename) {
    cache = new Cache();
    cdffile = new NetCDFFileManager(filename);
    cdfdata = new NetCDFDataManager();
    try {
      cdfdata.initDataSet(filename);
    }
    catch (IOException e) {
      System.err.println("ERROR opening file");
    }
  }

  /**
   * Stop Value Warehouse. Write the data in cache to netcdf dataset, then write whole netcdf 
   * data set to netcdf file.
   */
  public void stopIVW()
  {
      cdfdata.setValue(cache.getMap());
      cdfdata.writeToFile(cdffile);
  }

  /**
   * Get the value from Warehouse associated the icString.
   *
   * @param icString String - the key whose associated value is to be returned.
   * @return Object - return the value for the icString, or null if Warehouse
   * does not contain this key
   */
  public Object getValue(String icString)
  {
      Object result = null;
      result = cache.getValue(icString);
      
      if (cache.getCacheRate() > 95)
      {
          gc = GCFactory.creator(gcAlgorithm);
          HashMap hm = new HashMap(gc.doGarbageCollect(cache));
          cdfdata.setValue(hm);
          gctimes ++;
      }
      
      if(result == null)
      {
          result = cdfdata.getValue(icString);
      }
      
      return result;
  }

  /**
   * Put the value with the specified key in Warehouse, If Warehouse previously
   * contained a value for this key, the old value is replaced. If the rate of
   * cache is more than the limit, the Garbage Collector will be triggered.
   *
   * @param icString String - the key whose associated value is to be set.
   * @param obj Object - the value for the key
   */
  public int setValue(String icString, Object obj)
  {
    cache.setValue(icString, obj);
    
    if(cache.getCacheRate() > 95)
    {
//      System.out.println("now rate is " + rate);
      gc = GCFactory.creator(gcAlgorithm);
      HashMap hm = new HashMap(gc.doGarbageCollect(cache));
      cdfdata.setValue(hm);
      gctimes++;
    }
    
    return 1;
  }

  /**
   * getDataFile
   *
   * @param s String
   * @return String
   */
  public String getDataFile(String s)
  {
    cdffile.viewNcFile();
    return "";
  }

  /**
   * Set the garbage collection algorithm to deside which algorithm will be used
   * on next garbage collecting. Default is "MarkSweep".
   *
   * @param gcString String - description of type
   */
  public void setGCAlgorithm(String gcString) {
    if ( (gcString.equals("MarkSweep")) || (gcString.equals("Copying"))) {
      gcAlgorithm = gcString;
    }
    else {
      System.err.println("Wrong GC Algorithm type.");
    }
  }

  /**
   * View data in cache.
   *
   * @return int
   */
  public int viewSet()
  {
    cache.viewset();
    return 0;
  }

  /**
   * loadFile
   *
   * @return int
   */
  public int loadFile() {
    return 0;
  }
  
/*  int view(HashMap hashmap) {
    HashMap mapview = new HashMap();
    Set keyset;
    String keys;
    keyset = hashmap.keySet();
    Iterator itr = keyset.iterator();
    while (itr.hasNext()) {
      keys = (String) itr.next();
      mapview.put(keys, ( (CacheElement) hashmap.get(keys)).getValue());
    }
    System.out.println("size is : " + mapview.size());
    System.out.println("collection view " + mapview.entrySet());

    return 1;
  }
 */
}
