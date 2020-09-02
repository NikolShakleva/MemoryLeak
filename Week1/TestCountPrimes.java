// Week 1
// Counting primes, using multiple threads for better performance.
// (Much simplified from CountprimesMany.java)
// sestoft@itu.dk * 2014-08-27

public class TestCountPrimes {
  public static void main(String[] args) {
    final int range = 10_000_000;
		long start = System.nanoTime();
    /*System.out.printf("Sequential result: %10d in: %10d%n", countSequential(range), (System.nanoTime()-start)/1_000_000);
		start = System.nanoTime();
    System.out.printf("Parallel2  result: %10d in: %10d%n", countParallel2(range/2), (System.nanoTime()-start)/1_000_000);*/
		start = System.nanoTime();
    System.out.printf("Parallel20 result: %10d in: %10d%n", countParallelN(range, 20), (System.nanoTime()-start)/1_000_000);
		start = System.nanoTime();
    System.out.printf("Parallel12 result: %10d in: %10d%n", countParallelN(range, 12), (System.nanoTime()-start)/1_000_000);
		start = System.nanoTime();
		System.out.printf("Parallel6  result: %10d in: %10d%n", countParallelN(range, 6), (System.nanoTime()-start)/1_000_000);
	}

  private static boolean isPrime(int n) {
    int k = 2;
    while (k * k <= n && n % k != 0)
      k++;
    return n >= 2 && k * k > n;
  }

  // Sequential solution
  private static long countSequential(int range) {
    long count = 0;
    final int from = 0, to = range;
    for (int i=from; i<to; i++)
      if (isPrime(i)) 
        count++;
    return count;
  }

  // Basic parallel solution, using 2 threads
  private static long countParallel2(int perThread) {
    final Longcounter lc = new Longcounter();
    final int from1 = 0, to1 = perThread;
    Thread t1 = new Thread(() -> {
      for (int i=from1; i<to1; i++)
        if (isPrime(i))
          lc.increment();
    });
    final int from2 = perThread, to2 = perThread * 2;
    Thread t2 = new Thread(() -> {
      for (int i=from2; i<to2; i++)
        if (isPrime(i))
          lc.increment();
    });
    t1.start(); t2.start();
    try { t1.join(); t2.join(); } catch (InterruptedException exn) { }
    return lc.get();
  }

  // General parallel solution, using multiple threads
  private static long countParallelN(int range, int threadCount) {
    final int perThread = range / threadCount;
    final Longcounter lc = new Longcounter();
    Thread[] threads = new Thread[threadCount];
    for (int t=0; t<threadCount; t++) {
      final int from = perThread * t, 
        to = (t+1==threadCount) ? range : perThread * (t+1); 
      threads[t] = new Thread(() -> {
        for (int i=from; i<to; i++)
          if (isPrime(i))
            lc.increment();
      });
    }
    for (int t=0; t<threadCount; t++) 
      threads[t].start();
    try {
      for (int t=0; t<threadCount; t++) 
        threads[t].join();
    } catch (InterruptedException exn) { }
    return lc.get();
  }
}


