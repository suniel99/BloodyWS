package gipsy.GEE.IVW.Warehouse;

import java.util.*;

public class CacheSizes {
  public CacheSizes() {
  }

  public static int sizeOfObject() {
    return 4;
  }

  public static int sizeOfString(String string) {
    if (string == null) {
      return 0;
    }
    return 4 + string.length() * 2;
  }

  public static int sizeOfInt() {
    return 4;
  }

  public static int sizeOfDouble() {
    return 8;
  }

  public static int sizeOfMap(){
    return 36;
  }

  public static int sizeOfMap(Map map) {
    if (map == null) {
      return 0;
    }
    //Base map object -- should be something around this size.
    int size = 36;
    //Add in size of each value
    Iterator iter = map.values().iterator();
    while (iter.hasNext()) {
      String value = (String) iter.next();
      size += sizeOfString(value);
    }
    //Add in each key
    iter = map.keySet().iterator();
    while (iter.hasNext()) {
      String key = (String) iter.next();
      size += sizeOfString(key);
    }
    return size;
  }

}
