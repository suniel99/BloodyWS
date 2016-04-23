package gipsy.GEE.IVW.GarbageCollector;

import gipsy.GEE.IVW.Warehouse.Cache;
import java.util.HashMap;

public abstract class GarbageCollector {

  public GarbageCollector() {
  }

  public abstract HashMap doGarbageCollect(Cache cache);

}
