import java.util.concurrent.atomic.*;

public class Histogram4 implements Histogram {
    private final AtomicIntegerArray counts;
    private AtomicInteger total;
  
      public Histogram4(int span) {
          this.counts = new AtomicIntegerArray (span);
          total = new AtomicInteger();
      }
      // we still need to synchronize here becase the method uses both total and cunt which are interrelatrd
      // when a method has 2 variables which are noth thread-safe but interrelated we still need to use synchronized
      public synchronized void increment(int bin) {
          counts.getAndIncrement(bin);
          total.incrementAndGet();
      }
  
      public int getCount(int bin) {
          return counts.get(bin);
      }
      
       // we still need to synchronize here becase the method uses both total and cunt which are interrelatrd
      // when a method has 2 variables which are noth thread-safe but interrelated we still need to use synchronized
      public synchronized float getPercentage(int bin){
        return getCount(bin) / getTotal() * 100;
      }
  
      public int getSpan() {
          return counts.length();
      }
      
      
      public int getTotal(){
        return total.get();
      }

      public static void dump(Histogram histogram) {
        for (int bin = 0; bin < histogram.getSpan(); bin++) {
            System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
        }
        System.out.printf("      %9d%n", histogram.getTotal() );
    }

      public static void main(String[] args) {
     
 
        int range = 5_000_000;
        int threadsNumber = 10;
        final int perThread = range/threadsNumber;
        Thread [] threads = new Thread [threadsNumber];
        var hist = new Histogram4(30);
    
        for(int i=0; i<threadsNumber; i++) {
          final int from = perThread * i, 
          to = (i+1==threadsNumber) ? range : perThread * (i+1);
        threads[i] = new Thread(() -> {
         for (int k=from; k< to; k++) {
            hist.increment( TestCountFactors.countFactors(k));
          }
         });
        }
        long start = System.nanoTime();
        for (int t=0; t<threadsNumber; t++) {
          threads[t].start();
        }
        try {
          for (int t=0; t<threadsNumber; t++) {
            threads[t].join();
          }
    
        } catch (InterruptedException exn) { }
        long end = System.nanoTime();
        System.out.println("running time is: " + (end-start)/1_000_000_000);
        System.out.println("The total is: " + hist.getTotal());
        Histogram4.dump(hist);
      }

}


