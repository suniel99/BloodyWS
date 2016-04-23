package gipsy.GEE.IVW.GarbageCollector;

import gipsy.GEE.IVW.Warehouse.Cache;
import java.util.HashMap;

public class MarkSweepGC
    extends GarbageCollector {

  int desiredRate = 50;

  public MarkSweepGC() {
  }

  public HashMap doGarbageCollect(Cache cache) {
    HashMap hm = new HashMap();
    hm = cache.collectCachebyAccess(desiredRate);

    System.out.println("now it is marksweep.");
    return hm;
  }

}
