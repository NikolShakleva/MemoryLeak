// For week 2
// sestoft@itu.dk * 2015-10-29

import java.util.HashSet;

public class TestLocking2 {
  public static void main(String[] args) {
    int count = 10000;
    DoubleArrayList2 dal1 = new DoubleArrayList2();
    dal1.add(42.1); dal1.add(7.2); dal1.add(9.3); dal1.add(13.4); 
    dal1.set(2, 11.3);
    for (int i=0; i<dal1.size(); i++)
      System.out.println(dal1.get(i));
    DoubleArrayList2 dal2 = new DoubleArrayList2();
    dal2.add(90.1); dal2.add(80.2); dal2.add(70.3); dal2.add(60.4); dal2.add(50.5); 
    DoubleArrayList2 dal3 = new DoubleArrayList2();
    System.out.printf("Total size = %d%n", DoubleArrayList2.totalSize());
    System.out.printf("All lists  = %s%n", DoubleArrayList2.allLists());

    Thread t1 = new Thread(() -> {
      for (int i = 0; i< count; i++){
      DoubleArrayList2 da = new DoubleArrayList2();
      da.add(i + count);
      dal1.add(i);
   
      }
  });

  Thread t2 = new Thread(() -> {
    for (int i= 0; i< count; i++){
      DoubleArrayList2 da = new DoubleArrayList2();
      da.add(i);
    }

  });

  t1.start(); t2.start();

  try { 
      t1.join(); t2.join(); }
      catch (InterruptedException exn) { 
        System.out.println("Some thread was interrupted");
    }

    System.out.println(DoubleArrayList2.totalSize());
    System.out.println(DoubleArrayList2.allListSize());
  }
}

class DoubleArrayList2 {
  private static int totalSize = 0;
  private static final HashSet<DoubleArrayList2> allLists = new HashSet<>();

  // Invariant: 0 <= size <= items.length
  private double[] items = new double[2];
  private int size = 0;

  public DoubleArrayList2() {
   allLists.add(this);
   //addtoAllList();
  
  }

  // public synchronized  void addtoAllList(){
  //   allLists.add(this);
  // }

  // Number of items in the double list
  public synchronized int size() {
    return size;
  }

  // Return item number i, if any
  public synchronized double get(int i) {
    if (0 <= i && i < size) 
      return items[i];
    else 
      throw new IndexOutOfBoundsException(String.valueOf(i));
  }

  // Add item x to end of list
  public synchronized boolean add(double x) {
    if (size == items.length) {
      double[] newItems = new double[items.length * 2];
      for (int i=0; i<items.length; i++)
	      newItems[i] = items[i];
      items = newItems;
    }
    items[size] = x;
    size++;
    incrementTotalSize();
    return true;
  }

  // Replace item number i, if any, with x
  public synchronized double set(int i, double x) {
    if (0 <= i && i < size) {
      double old = items[i];
      items[i] = x;
      return old;
    } else 
      throw new IndexOutOfBoundsException(String.valueOf(i));
  }

  // The double list formatted as eg "[3.2, 4.7]"
  public synchronized String toString() {
    StringBuilder sb = new StringBuilder("[");
    for (int i=0; i<size; i++)
      sb.append(i > 0 ? ", " : "").append(items[i]);
    return sb.append("]").toString();
  }

  public synchronized static int totalSize() {
    return totalSize;
  }

  public synchronized static HashSet<DoubleArrayList2> allLists() {
    return allLists;
  }

  public synchronized static  void incrementTotalSize(){
    totalSize++;

  }

  public synchronized static int allListSize(){
    return allLists.size();
  }
}
