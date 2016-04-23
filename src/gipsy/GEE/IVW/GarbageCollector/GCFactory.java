package gipsy.GEE.IVW.GarbageCollector;

public class GCFactory {
  public GCFactory() {
  }
  public static GarbageCollector creator(String s){
    if (s.equals("MarkSweep")){
      return new MarkSweepGC();
    }else if (s.equals("Copying")){
      return new CopyingGC();
    }else {
      System.out.println("Wrong GC Algorithm type.");
      return null;
    }
  }
}
