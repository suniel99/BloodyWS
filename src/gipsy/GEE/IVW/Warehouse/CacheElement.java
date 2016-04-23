package gipsy.GEE.IVW.Warehouse;

import gipsy.util.LinkedListNode;

/**
 * Store the object in the cache dataset. It has attributes: value, size and
 * linkedListNode. 
 */

public class CacheElement {

  private Object value = null;
  private int size = 0;
  public LinkedListNode accListNode = null;;
  public LinkedListNode ageListNode = null;

  public CacheElement() {
  }

  public CacheElement(Integer intg) {
    value = intg;
    size = CacheSizes.sizeOfObject() + CacheSizes.sizeOfInt();
  }

  public CacheElement(Double doub) {
    value = doub;
    size = CacheSizes.sizeOfObject() + CacheSizes.sizeOfDouble();
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object obj) {
    value = obj;
    if (obj.getClass().getName().equals("java.lang.Integer")) {
      size = CacheSizes.sizeOfObject() + CacheSizes.sizeOfInt();
    }else if (obj.getClass().getName().equals("java.lang.Double")) {
      size = CacheSizes.sizeOfObject() + CacheSizes.sizeOfDouble();
    }
  }

  public int getSize() {
    return size;
  }

}
