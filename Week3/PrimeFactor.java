import java.util.concurrent.atomic.AtomicInteger;

public class PrimeFactor {
    
    private int count = 0;
        
    public PrimeFactor () {
           
            }
        
            public synchronized int addAndGet(int e){
               return count += e;
    
            }
        
            public synchronized int get(){
                return count;
            }


 public static void main(String[] args) {
     
 
    int range = 5_000_000;
    int threadsNumber = 10;
    final int perThread = range/threadsNumber;
    Thread [] threads = new Thread [threadsNumber];
    AtomicInteger total= new AtomicInteger();

    for(int i=0; i<threadsNumber; i++) {
      final int from = perThread * i, 
      to = (i+1==threadsNumber) ? range : perThread * (i+1);
    threads[i] = new Thread(() -> {
      var atomic = new MyAtomicInteger();
     for (int k=from; k< to; k++) {
        atomic.addAndGet( TestCountFactors.countFactors(k));
      }
    total.addAndGet(atomic.get());
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
    System.out.println(total.get());
    System.out.println("running time is: " + (end-start)/1_000_000_000);
  }
    
}

