package gipsy.GEE.IVW.Warehouse;

import java.util.*;
import java.util.regex.Pattern;
import ucar.nc2.*;
import ucar.ma2.*;
import java.io.IOException;

/**
 * It is the main storage that is used to store and manage values as the second layer in 
 * memory. We implement it using related java package of netCDF interface: ucar.ma2, known 
 * as "MultiArray version 2" package.
 */

public class NetCDFDataManager {

  Hashtable varArray = new Hashtable();
  Hashtable variables = new Hashtable();
  Hashtable varID = new Hashtable();
  HashSet keyset = new HashSet();

  public NetCDFDataManager() {
  }
  
  /**
   * Initialize the netCDF array in memory It load data from the netCDF file.
   *
   * @param filname String - the file name of program
   */
  int initDataSet(String filename) throws IOException {
    String ncfilename = filename + ".nc";
    NetcdfFile ncdf = new NetcdfFile(ncfilename);

    List varlist = ncdf.getVariables();
    Iterator vitr = varlist.iterator();
    while (vitr.hasNext()) {
      Variable var = (Variable) vitr.next();
      if (var.getDataType().toString().equals("int")) {
        ArrayInt tmpAI = new ArrayInt(var.getShape());
        varArray.put(var.getName(), tmpAI);
      }
      else if (var.getDataType().toString().equals("double")) {
        ArrayDouble tmpAD = new ArrayDouble(var.getShape());
        varArray.put(var.getName(), tmpAD);
      }
      variables.put(var.getName(), var);
      varID.put(var.findAttribute("id").getStringValue(), var.getName());
    }
    return 1;
  }

  /**
   * Set the pair, the key and the value, into the netCDF array.
   *
   * @param key String - IC string
   * @param obj object - value
   * @return int
   */
  protected int setValue(String key, Object obj) {
    Pattern pat = Pattern.compile(",");
    String varkey[] = pat.split(key);
    String varname = (String)varID.get(varkey[0]);
    int ranklen = ( (Variable) variables.get(varname)).getRank();
    int dimvalues[] = new int[ranklen];
    for (int i = 1; i <= ranklen; i++) {
      dimvalues[i - 1] = Integer.valueOf(varkey[i+1]).intValue();
    }
    String type = ( (Variable) variables.get(varname)).getDataType().toString();
    if (type.equals("int")) {
      Index imaInt = ( (ArrayInt) varArray.get(varname)).getIndex();
      ( (ArrayInt) varArray.get(varname)).setInt(imaInt.set(dimvalues),
          ( (Integer) obj).intValue());
    }
    else if (type.equals("double")) {
      Index imaDb = ( (ArrayDouble) varArray.get(varname)).getIndex();
      ( (ArrayDouble) varArray.get(varname)).setDouble(imaDb.set(dimvalues),
          ( (Double) obj).doubleValue());
    }
    else {
      System.out.println("Wrong data type!");
      return 0;
    }
    keyset.add(key);
    
    return 1;
  }

  /**
   * Set a batch of values into the netCDF array.
   *
   * @param all values in hashmap hashmap
   * @return int
   */
  protected int setValue(HashMap hm) {
    Set keyset = hm.keySet();
    Iterator itr = keyset.iterator();
    while (itr.hasNext()) {
      String keys = (String) itr.next();
      if ( ((CacheElement) hm.get(keys)).getValue() == null){
        continue;
      }
      setValue(keys, ( (CacheElement) hm.get(keys)).getValue());
    }
    return 1;
  }

  /**
   * Get the value from the netCDF array associated with the given key.
   *
   * @param key String - IC string
   * @return the value object
   */
  Object getValue(String key) {
    Object result = null;
    if (!(keyset.contains(key))) {
        return result;
    }
    Pattern pat = Pattern.compile(",");
    String varkey[] = pat.split(key);
    String varname = (String)varID.get(varkey[0]);
    int ranklen = ( (Variable) variables.get(varname)).getRank();
    int dimvalues[] = new int[ranklen];
    for (int i = 1; i <= ranklen; i++) {
      dimvalues[i - 1] = Integer.valueOf(varkey[i+1]).intValue();
    }
    String type = ( (Variable) variables.get(varname)).getDataType().toString();
    if (type.equals("int")) {
      Index imaInt = ( (ArrayInt) varArray.get(varname)).getIndex();
      result = new Integer(((ArrayInt) varArray.get(varname)).getInt(imaInt.set(dimvalues)));
    }
    else if (type.equals("double")) {
      Index imaDb = ( (ArrayDouble) varArray.get(varname)).getIndex();
      result = new Double(((ArrayDouble) varArray.get(varname)).getDouble(imaDb.set(dimvalues)));
    }
    else {
      System.out.println("Wrong data type!");
    }
    return result;
  }
  
  /**
   * Writes all the values in netCDF array into the netCDF file.
   *
   * @param cdffile NetCDFFileManager - reference of NetCDFFileManager 
   */
  int writeToFile(NetCDFFileManager cdffile) {
    try {
      cdffile.addDataToFile(variables, varArray);
    }
    catch (IOException e) {
      System.err.println("ERROR writing file");
    }

    return 1;
  }
}
