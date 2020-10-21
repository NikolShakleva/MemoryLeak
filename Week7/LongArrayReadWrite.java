
// For week 7
// sestoft@itu.dk * 2015-10-29
// Changes kasper@itu.dk * 2020-10-05

import java.util.stream.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class LongArrayReadWrite {
    
    public static void main(String[] args) {
      int[] N = { 500_000, 1_000_000, 5_000_000, 10_000_000, 50_000_000, 100_000_000, 500_000_000, 1_000_000_000};

      for (int j = 0; j < N.length ; j++){
        LongArrayListRW dal1 = LongArrayListRW.withElements(42, 7, 9, 13);
        dal1.set(2, 11);
        int range = N[j];
        Timer tAdd = new Timer();
        //Timer tSet = new Timer();
        Timer tGet = new Timer();


        Thread t1 = new Thread(() -> {
            for (int i=0; i<range; i++){
                dal1.add(i);
            }
            tAdd.check();
        });

        Thread t2 = new Thread(() -> {
            for (int i=0; i<range; i++){
                dal1.get(0);
            }
            tGet.check();
        });

        // Thread t3 = new Thread(() -> {
        //     for (int i=0; i<range; i++){
        //         dal1.set(i, 11);
        //     }
        //     tSet.check();
        // });

        Thread t4 = new Thread(() -> {
            for (int i=0; i<range; i++){
                dal1.add(i*2);
        }});

        tAdd.play();
        tGet.play();
        //tSet.play();

        t1.start(); t2.start(); //t3.start(); 
        //t4.start();
        try { 
            t1.join(); t2.join();}//t3.join(); 
            //t4.join(); }
        catch (InterruptedException exn) { 
            System.out.println("Some thread was interrupted");    
        }

        // for (int i=0; i<dal1.size(); i++) {
        // System.out.println(dal1.get(i));
        // }
         // System.out.println("Using toString(): " + dal1);
          System.out.println("Count should be " + range * 2 + 4 + ", and it is: " + dal1.size());
          System.out.println("N: " + range);
          System.out.println("Running Time Add: " + tAdd.getTotal() + " s");
          System.out.println("Running Time Get: " + tGet.getTotal() + " s");
         // System.out.println("Running Time Set: " + tSet.getTotal() + " s");
        } 
      }
      }
      




class LongArrayListRW {
    // Invariant: 0 <= size <= items.length
    private long[] items;
    private AtomicInteger size = new AtomicInteger();
    private final ReadWriteLock lock = new ReentrantReadWriteLock(); 
    private final Lock r = lock.readLock();
    private final Lock w = lock.writeLock();
  
    public LongArrayListRW() {
      reset();
    }
    
    public static LongArrayListRW withElements(long... initialValues){
      LongArrayListRW list = new LongArrayListRW();
      for (long l : initialValues) list.add( l );
      return list;
    }
    
    // reset me to initial 
    public void reset(){
      lockIt(w, () -> {
        items = new long[2];
        size.set(0);
       });
    }
  
    // Number of items in the double list
    public int size() {
      return size.get();
    }
  
    // Return item number i
    public long get(int i) {
      if (0 <= i && i < size.get()) {
        r.lock();
        try{
        return items[i];
        } 
        finally{r.unlock();
        }
      }
      else 
        throw new IndexOutOfBoundsException(String.valueOf(i));
    }
  
      // Replace item number i, if any, with x
    public void set(int i, long x) {
        lockIt(w, () -> {
      if (0 <= i && i < size.get()) {
        items[i] = x;
      } else     throw new IndexOutOfBoundsException(String.valueOf(i));
    });
    }
  
    // Add item x to end of list
    public LongArrayListRW add(long x) {
      lockIt(w, () -> {
      if (size.get() == items.length) {
        long[] newItems = new long[items.length * 2];
        for (int i=0; i<items.length; i++) newItems[i] = items[i];
        items = newItems;
      }
      items[size.get()] = x;
      size.getAndIncrement();
      });
      return this;
    }
  
  
    // The long list formatted as eg "[3, 4]"
    // Just messing with stream joining - not part of solution
    public String toString() {
      return Arrays.stream(items, 0,size.get())
        .mapToObj( Long::toString )
        .collect(Collectors.joining(", ", "[", "]"));
    }

    public void lockIt(Lock l, Runnable action){
        l.lock();
        try{
            action.run();
        } finally {
            l.unlock();
        }
    }
  }
  
  

