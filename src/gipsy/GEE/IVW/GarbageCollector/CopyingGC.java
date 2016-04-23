package gipsy.GEE.IVW.GarbageCollector;

import java.util.HashMap;
import gipsy.GEE.IVW.Warehouse.Cache;

public class CopyingGC
    extends GarbageCollector {
  public CopyingGC() {
  }

  public HashMap doGarbageCollect(Cache cache) {
    HashMap hm = new HashMap();
    System.out.println("now it is copying.");
    return hm;
  }

}
