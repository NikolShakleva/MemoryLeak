import java.util.Arrays;

public class Histogram2 implements Histogram {

	private final int[] counts;
    private int total=0;
  
      public Histogram2(int span) {
          this.counts = new int[span];
      }
  
      public synchronized void increment(int bin) {
          counts[bin] = counts[bin] + 1;
          total++;
      }
  
      public synchronized int getCount(int bin) {
          return counts[bin];
      }
      
      public synchronized float getPercentage(int bin){
        return getCount(bin) / getTotal() * 100;
      }
  
      public int getSpan() {
          return counts.length;
      }
      
      public  synchronized int getTotal(){
        return total;
      }

      public void printArray() {
          System.out.println(Arrays.toString(counts));
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
      }

}
