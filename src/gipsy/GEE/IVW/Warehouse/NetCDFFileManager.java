package gipsy.GEE.IVW.Warehouse;

import gipsy.lang.GIPSYType;
import gipsy.storage.Dictionary;
import gipsy.storage.DictionaryItem;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayInt;
import ucar.nc2.Dimension;
import ucar.nc2.NCdump;
import ucar.nc2.NetcdfFileWriteable;
import ucar.nc2.Variable;


/**
 * It is responsible of dealing with the netCDF file. It creates the netCDF file 
 * when the IVW is initialized. In the execution step, all of them will be loaded 
 * to the memory set when the IVW is set up. 
 *
 * Once create() is called, the dataset structure is immutable. After create() has 
 * been called we can then write the data values. In addition, after executing the 
 * whole program, it will write all the data values in memory set to the netCDF file. 
 */
public class NetCDFFileManager
{
    public static final int DIMLENGTH = 10;

  String ncfilename;
  Vector dimensions = new Vector();
  Vector variables = new Vector();
  //need to be given in different lucid program
  int dimLength = DIMLENGTH;

  public NetCDFFileManager(String filename) {
    ncfilename = filename + ".nc";
  }
  
  protected int getDimLength() {
    return dimLength;
  }
  
  protected void setDimLength(int length) {
    dimLength = length;
  }
  
  /**
   * Reads the data dictionary from front-end of system. Initializes the netCDF file, 
   * add dimensions, variables and attributes into it.
   * 
   * @param dictSemantic Dictionary - reference of data dictionary
   * @param filename String - name of netCDF file
   */

  protected int initNetCDF(Dictionary dictSemantic, String filename) {
    DictionaryItem dictItem = new DictionaryItem();
    for (int i = 0; i < dictSemantic.size(); i++) {
      dictItem = (DictionaryItem) dictSemantic.elementAt(i);
      if (dictItem.getKind().equals("dimension")) {
        dimensions.add(dictItem.getName());
      }
      else if (dictItem.getKind().equals("identifier")) {
        variables.add(dictItem);
      }
    }

    NetcdfFileWriteable ncfile = new NetcdfFileWriteable();
    ncfilename = filename + ".nc";
    ncfile.setName(ncfilename);
    Dimension[] dimNumber = new Dimension[dimensions.size()];

    // define dimensions
    for (int d = 0; d < dimensions.size(); d++) {
      String dimName = (String) dimensions.elementAt(d);
      dimNumber[d] = ncfile.addDimension(dimName, dimLength);
    }

    // define Variables
    for (int j = 0; j < variables.size(); j++) {
      DictionaryItem item = (DictionaryItem) variables.elementAt(j);
      switch (item.getTypeEnumeration()) {
        case GIPSYType.TYPE_INT:
            //add variables to netcdf file
          ncfile.addVariable(item.getName(), int.class, dimNumber);
          ncfile.addVariableAttribute(item.getName(), "id", String.valueOf(item.getID()));
          break;
        case GIPSYType.TYPE_DOUBLE:
            //add variables to netcdf file
          ncfile.addVariable(item.getName(), double.class, dimNumber);
          ncfile.addVariableAttribute(item.getName(), "id", String.valueOf(item.getID()));
          break;
        default:
      }
    }

    try {
      ncfile.create();
    }
    catch (IOException e) {
      System.err.println("ERROR creating file");
    }
    System.out.println("-------------------");
    System.out.println("ncfile = " + ncfile);

    try {
      ncfile.close();
    }
    catch (IOException e) {
      System.err.println("ERROR writing file");
    }

    return 1;
  }
  
  /**
   * Write some data to the netCDF file.
   *
   * @param var hashtable - variables hashtable
   * @param varArray hashtable - array that stores data of variables
   */
  protected int addDataToFile(Hashtable var, Hashtable varArray) throws IOException {
    NetcdfFileWriteable nf = new NetcdfFileWriteable(ncfilename);
    Set keyset = varArray.keySet();
    Iterator itr = keyset.iterator();
    while (itr.hasNext()) {
      String keys = (String) itr.next();
      String type = ( (Variable) var.get(keys)).getDataType().toString();
      if (type.equals("int")) {
        try {
          nf.write(keys, ( (ArrayInt) varArray.get(keys)));
        }
        catch (IOException e) {
          System.err.println("ERROR writing file");
        }
      }
      else if (type.equals("double")) {
        try {
          nf.write(keys, ( (ArrayDouble) varArray.get(keys)));
        }
        catch (IOException e) {
          System.err.println("ERROR writing file ..");
        }
      }
      else {
        System.out.println("Wrong data type!");
        return 0;
      }
    }

    try {
      nf.close();
    }
    catch (IOException e) {
      System.err.println("ERROR writing file!!!");
    }
    return 1;
  }

  protected int viewNcFile(){
    String temp = ncfilename+" -vall";
    OutputStream out = System.out;
    try {
      System.out.println("--------------------");
      NCdump.print(temp, out);
    }
    catch (IOException e) {
      System.err.println("ERROR writing the file ....");
    }
    return 1;
  }

}
