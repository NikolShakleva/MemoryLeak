import java.util.Arrays;

public class Histogram2 implements Histogram {
  // the count is final because its mutable. Has fixed lenght
  private final int[] counts;
  // this filed doesnt need to automic long because the methods that access the field are synchronized
    private int total=0;
  
      public Histogram2(int span) {
          this.counts = new int[span];
      }
      // we need to synchronize because the counts is immutable but the elements inside the count(bins) 
      //are mutabe
      //Also interrelation between counts and total
      public synchronized void increment(int bin) {
          counts[bin] = counts[bin] + 1;
          total++;
      }
      // we need to synchronize because the count is immutable but the elements inside the counts(bins) 
      //are mutabe
      public synchronized int getCount(int bin) {
          return counts[bin];
      }
      
      // we need to synchronize because the counts is immutable but the elements inside the count(bins) 
      //are mutabe
      //Also interrelation between counts and total
      public synchronized float getPercentage(int bin){
        return getCount(bin) / getTotal() * 100;
      }
  // this doesnt need to be synchronized because the count legth is fixed
      public int getSpan() {
          return counts.length;
      }
      
      // stays synchronized because the total is not atomic
      public  synchronized int getTotal(){
        return total;
      }

      public void printArray() {
          System.out.println(Arrays.toString(counts));
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
        var hist = new Histogram2(30);
    
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
        hist.printArray();
        System.out.println("running time is: " + (end-start)/1_000_000_000);
        Histogram2.dump(hist);
      }

}
