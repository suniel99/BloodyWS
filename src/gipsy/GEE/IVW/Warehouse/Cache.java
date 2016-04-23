package gipsy.GEE.IVW.Warehouse;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

import gipsy.util.LinkedList;
import gipsy.util.LinkedListNode;

/**
 * It is a cache implementation for the value warehouse. It stores values 
 * associated with unique keys in memory for fast access.
 *
 * If the cache does grow too large, values will be removed such that those
 * that are accessed least frequently are removed first. The cache will return
 * null if the requested value is not found.
 */

public class Cache {
    public static final int DEFAULT_SIZE = 512 * 1024;
    public static final int SMALL_SIZE = 8 * 1024;

  protected HashMap hashmap;
  protected static long currentTime = System.currentTimeMillis();
  protected int maxSize = DEFAULT_SIZE;
  protected LinkedList accessList, emptyList, ageList;
  protected int size = 0;
  protected long maxLifetime = 0;
  protected long maxTimeStamp = 0;
  protected int cacheHits = 0;
  protected int cacheMisses = 0;

  public Cache() {
    hashmap = new HashMap(maxSize/4);
    accessList = new LinkedList();
    emptyList = new LinkedList();
    ageList = new LinkedList();
    size = CacheSizes.sizeOfMap();
  }
  
  public Cache(String size) {
    this();
    if (size.equals("small")) {
        maxSize = SMALL_SIZE;
    }
    else {
        System.out.println("Wrong cache size type! Now cache size is default.");
    }
  }

  public Cache(int customSize) {
    this();
    this.maxSize = customSize;
  }
  
  /**
   * Return the current size of the cache.
   *
   * @return int - size of cache
   */
  public int getSize() {
    return size;
  }

  /**
   * Return the max size of the cache.
   *
   * @return int - max size of cache
   */
  public int getMaxSize() {
    return maxSize;
  }

  /**
   * Set the hits of the cache.
   *
   * @param maxSize int - bytes of size
   */
  public void setMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }

  /**
   * Return the max size of the cache.
   *
   * @return hits of cache, int
   */
  public int getHits() {
    return cacheHits;
  }
  
  /**
   * Return the misses of the cache.
   *
   * @return int - misses of cache
   */
  public int getMisses() {
    return cacheMisses;
  }
  
   /**
   * Return the rate of the cache.
   *
   * @return int - rate of cache
   */
  public int getCacheRate() {
    return size * 100 / maxSize;
  }
  
  /**
   * Returns the number of objects in the cache.
   *
   * @return number of the objects in cache, int
   */
  public synchronized int getNumElements() {
    return hashmap.size();
  }

  /**
   * Get the value from the cache dataset associated with the given key.
   *
   * @param key String - IC string
   * @return object - the value
   */
  synchronized Object getValue(String key) {
    Object result = null;
    CacheElement temp = new CacheElement();
    if (hashmap.containsKey(key)) {
        temp = (CacheElement) hashmap.get(key);
        if (temp.getValue() == null) {
            temp.accListNode.remove();
            emptyList.addFirst(temp.accListNode);
            cacheMisses ++;
        }
        else {
            temp.accListNode.remove();
            accessList.addFirst(temp.accListNode);
            cacheHits ++;
        }
        result = temp.getValue();
    }
    else {
        LinkedListNode emptyNode = emptyList.addFirst(key);
        emptyNode.timestamp = System.currentTimeMillis();
        temp.accListNode = emptyNode;
        hashmap.put(key, temp);
        size += CacheSizes.sizeOfString(key);
        cacheMisses ++;
    }
    
/*    CacheElement temp = (CacheElement) hashmap.get(key);
    if (temp == null) {
      temp = new CacheElement();
      LinkedListNode emptyNode = emptyList.addFirst(key);
      emptyNode.timestamp = System.currentTimeMillis();
      temp.listNode = emptyNode;
      hashmap.put(key, temp);
      size += CacheSizes.sizeOfString(key);
      cacheMisses ++;
    }
    else {
      temp.listNode.remove();
      accessList.addFirst(temp.listNode);
      cacheHits ++;
      result = temp.getValue();
    } */
    //debug
    //System.out.println("after get value");
    //System.out.println("Cache size is : " + size + " Bytes.");
    //System.out.println("Max Cache size is : " + maxSize + " Bytes.");

    return result;
  }

  /**
   * Set the pair, the key and the value, to the cache dataset. Return the size
   * rate of the cache, then client can decide to collect cache dataset or not.
   *
   * @param key String - IC string
   * @param obj object - value 
   * @return int - size rate
   */
  synchronized int setValue(String key, Object obj) {
      
    CacheElement temp = new CacheElement();
    if (hashmap.containsKey(key)) {
      temp = (CacheElement) hashmap.get(key);
      if (temp.getSize() != 0){
          size -= temp.getSize();
      }
      temp.setValue(obj);
      //Remove the object from it's current place in the cache order list,
      //and re-insert it at the front of the list.
      temp.accListNode.remove();
      temp.accListNode.timestamp = System.currentTimeMillis() -
          temp.accListNode.timestamp;
      if (temp.accListNode.timestamp > maxTimeStamp) {
          maxTimeStamp = temp.accListNode.timestamp;
      }
      accessList.addFirst(temp.accListNode);
      
      if (temp.ageListNode != null) {
          temp.ageListNode.remove();
          temp.ageListNode.timestamp = System.currentTimeMillis();
          ageList.addFirst(temp.ageListNode);
      }
      else {
          LinkedListNode ageNode = ageList.addFirst(key);
          ageNode.timestamp = System.currentTimeMillis();
          temp.ageListNode = ageNode;
      }
      hashmap.put(key, temp);
      size += temp.getSize();
    }
    else {
      temp.setValue(obj);
      LinkedListNode accessedNode = accessList.addFirst(key);
      accessedNode.timestamp = maxTimeStamp;
      temp.accListNode = accessedNode;
      
      LinkedListNode ageNode = ageList.addFirst(key);
      ageNode.timestamp = System.currentTimeMillis();
      temp.ageListNode = ageNode;
      hashmap.put(key, temp);
      size += CacheSizes.sizeOfString(key) + temp.getSize();
    }
    
    //debug
    //System.out.println("after setvalue");
    //System.out.println("Cache size is : " + size + " Bytes.");
    //System.out.println("Max Cache size is : " + maxSize + " Bytes.");

    return 1;
  }

  /**
   * Remove some value from the cache dataset associate to the given key.
   *
   * @param key String - IC string
   */
  public synchronized void remove(String key) {
    CacheElement temp = (CacheElement) hashmap.get(key);
    //remove from the hash map
    hashmap.remove(key);
    //remove from the cache order list
    temp.accListNode.remove();
    if (temp.ageListNode != null) {
        temp.ageListNode.remove();
    }
    //remove references to linked list nodes
    temp.accListNode = null;
    temp.ageListNode = null;
    //removed the object, so subtract its size from the total.
    size -= CacheSizes.sizeOfString(key);
    size -= temp.getSize();
    
    //for debug
    //System.out.println("after remove");
    //System.out.println("Cache size is : " + size + " Bytes.");
    //System.out.println("Max Cache size is : " + maxSize + " Bytes.");
  }

  public synchronized void removeAccessListlast() {
    remove( (String) accessList.getLast().object);
  }
  
  /**
   * Clears all entries out of cache where the entries are older than the     
   * maximum defined age.
   */
  private final void deleteExpiredElements() {
      if (maxLifetime <= 0) {
          return;
      }
      LinkedListNode agenode = ageList.getLast();
      if (agenode == null) {
          return;
      }
      long expireTime = currentTime - maxLifetime;
      
      while(expireTime > agenode.timestamp) {
          //DEBUG
          //System.err.println("Object with key " + node.object + " expired.");
          
          //Remove the object
          remove((String)agenode.object);
          agenode = ageList.getLast();
          //If there are no more entries in the age list, return.
          if (agenode == null) {
              return;
          }
      }
  }

  public final HashMap collectCachebyTime(int rate) {
      HashMap resultmap = new HashMap();
      int expectSize = maxSize * rate / 100;
      while (size > expectSize) {
          String key = null;
          if (ageList.getLast() != null) {
              key = (String)ageList.getLast().object;
          }
          if (key != null) {
              resultmap.put(key, hashmap.get(key));
              remove((String)ageList.getLast().object);
          }
          else {
              remove((String)emptyList.getLast().object);
          }
      }
      System.out.println("rate is : " + size * 100 / maxSize);
      
      return resultmap;    
  }


  /**
   * Removes values from cache dataset if the cache is too full. For default,
   * "Too full" is defined as within 5% of the maximum cache size. 
   */
  public final HashMap collectCachebyAccess(int rate) {
    HashMap resultmap = new HashMap();
    int expectSize = maxSize * rate / 100;
    while (size > expectSize) {
      //Get the key and invoke the remove method on it.
        String key = null;
        if (accessList.getLast() != null) {
            key = (String)accessList.getLast().object;
        }
      if (key != null) {
          resultmap.put(key, hashmap.get(key));
          remove((String)accessList.getLast().object);
      }
      else {
          remove((String)emptyList.getLast().object);
      }
    }
       
    System.out.println("rate is : " + size * 100 / maxSize);
    return resultmap;
  }

  HashMap getMap() {
    return hashmap;
  }
  
  int getAccListNum() {
      return accessList.getElementNumber();
  }
  
  int getEmpListNum() {
      return emptyList.getElementNumber();
  }
  
  int getAgeListNum() {
      return ageList.getElementNumber();
  }

  int viewset() {
    HashMap mapview = new HashMap();
    Set keyset;
    String keys;
    keyset = hashmap.keySet();
    Iterator itr = keyset.iterator();
    while (itr.hasNext()) {
      keys = (String) itr.next();
      mapview.put(keys, ( (CacheElement) hashmap.get(keys)).getValue());
    }
    System.out.println("-----------------------------------");
    System.out.println("Number of elements is : " + mapview.size() + "; Cache size is : " +
                       size);
//    System.out.println("collection view " + mapview.entrySet());
//    System.out.println(accessList.toString());
//    System.out.println(emptyList.toString());
    System.out.println("accessList elements number : " + accessList.getElementNumber());
    System.out.println("emptyList elements number : " + emptyList.getElementNumber());
    System.out.println("Cache hits is : " + cacheHits + "; Cache misses is : " + cacheMisses);

    return 1;
  }

}
