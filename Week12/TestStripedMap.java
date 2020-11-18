// For week 7 -- four incomplete implementations of concurrent hash maps
// sestoft@itu.dk * 2014-10-07, 2015-09-25

// Parts of the code are missing.  Your task in the exercises is to
// write the missing parts.

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class TestStripedMap {
  static int sumStream = 0;
  public static void main(String[] args) {
    SystemInfo();
    for (int i = 0; i < 10; i++)
    testAllMaps();    // Must be run with: java -ea TestStripedMap 
  }

  

  private static void testMapParellel(final OurMap<Integer, String> map) throws InterruptedException, BrokenBarrierException {
    final int noThreads = 16;
    final int repetition = 10_000;
    final CyclicBarrier barrier = new CyclicBarrier(noThreads+1);
     AtomicInteger totalSum = new AtomicInteger();
     Runnable thread = () -> {
      try {
        final Random rnd = new Random();
        int sum = 0;
        barrier.await();
        for (int i=0; i< repetition; i++) {
          int randomPut = rnd.nextInt(99);
            var keyPut = map.put(randomPut,Integer.toString(rnd.nextInt(99)))  == null ?  sum+= randomPut : sum;

          int randomAbsent = rnd.nextInt(99);
            var keyAbsent = map.putIfAbsent(randomAbsent, Integer.toString(rnd.nextInt(99)))  == null ?  sum+=randomAbsent : sum;

          int randomRemove = rnd.nextInt(99);
            var keyRemove = map.remove(randomRemove) != null ? sum-=randomRemove : sum;
        }
        totalSum.addAndGet(sum);
        barrier.await();
       } catch (Exception ie) {
        System.out.println("Something went wrong");
      }
    };

    for(int j = 0; j < noThreads; j++) {
      new Thread (thread).start();
    }
    barrier.await();
    long start = System.nanoTime();
    barrier.await();

    long time = System.nanoTime() - start;
    int elementsSum = 0;
    for(int i = 0 ; i < 99; i++) {
      if(map.containsKey(Integer.valueOf(i))) {
        elementsSum +=i; 
      }
    }

    // count the elements with a stram
    map.forEach((k,v) -> sumStream+=k);

    // Print statements
    System.out.println("Map size: " + map.size());
    System.out.println("totalSum: " + totalSum.get());
    System.out.println("elementsSum: " + elementsSum);
    System.out.println("sumStream: " + sumStream);
    System.out.println("Multiple threads test: " + (elementsSum == totalSum.get() ? "SUCCESS" : "FAILURE"));
    System.out.printf("Multiple threads time: %,dns\n", time );
  }

  

  // private static void testMap(final OurMap<Integer, String> map) {
  //   System.out.printf("%n%s%n", map.getClass());
  //   assert map.size() == 0;
  //   assert !map.containsKey(117);
  //   assert !map.containsKey(-2);
  //   assert map.get(117) == null;
  //   assert map.put(117, "A") == null;
  //   assert map.containsKey(117);
  //   assert map.get(117).equals("A");
  //   assert map.put(17, "B") == null;
  //   assert map.size() == 2;
  //   assert map.containsKey(17);
  //   assert map.get(117).equals("A");
  //   assert map.get(17).equals("B");
  //   assert map.put(117, "C").equals("A");
  //   assert map.containsKey(117);
  //   assert map.get(117).equals("C");
  //   assert map.size() == 2;
  //   map.forEach((k, v) -> System.out.printf("%10d maps to %s%n", k, v));
  //   assert map.remove(117).equals("C");
  //   assert !map.containsKey(117);
  //   assert map.get(117) == null;
  //   assert map.size() == 1;
  //   assert map.putIfAbsent(17, "D").equals("B");
  //   assert map.get(17).equals("B");
  //   assert map.size() == 1;
  //   assert map.containsKey(17);
  //   assert map.putIfAbsent(217, "E") == null;
  //   assert map.get(217).equals("E");
  //   assert map.size() == 2;
  //   assert map.containsKey(217);
  //   assert map.putIfAbsent(34, "F") == null;
  //   map.forEach((k, v) -> System.out.printf("%10d maps to %s%n", k, v));
  //   map.reallocateBuckets();
  //   assert map.size() == 3;
  //   assert map.get(17).equals("B") && map.containsKey(17);
  //   assert map.get(217).equals("E") && map.containsKey(217);
  //   assert map.get(34).equals("F") && map.containsKey(34);
  //   map.forEach((k, v) -> System.out.printf("%10d maps to %s%n", k, v));    
  //   map.reallocateBuckets();
  //   assert map.size() == 3;
  //   assert map.get(17).equals("B") && map.containsKey(17);
  //   assert map.get(217).equals("E") && map.containsKey(217);
  //   assert map.get(34).equals("F") && map.containsKey(34);
  //   map.forEach((k, v) -> System.out.printf("%10d maps to %s%n", k, v));    
  // }

  private static void testAllMaps() {
        try {
    testMapParellel(new StripedMap<Integer,String>(77, 7));
    } catch (Exception e) {}
  }

  // --- Benchmarking infrastructure ---

  private static class Timer {
    private long start, spent = 0;
    public Timer() { play(); }
    public double check() { return (System.nanoTime()-start+spent)/1e9; }
    public void pause() { spent += System.nanoTime()-start; }
    public void play() { start = System.nanoTime(); }
  }

  public static void SystemInfo() {
    System.out.printf("# OS:   %s; %s; %s%n", 
                      System.getProperty("os.name"), 
                      System.getProperty("os.version"), 
                      System.getProperty("os.arch"));
    System.out.printf("# JVM:  %s; %s%n", 
                      System.getProperty("java.vendor"), 
                      System.getProperty("java.version"));
    // The processor identifier works only on MS Windows:
    System.out.printf("# CPU:  %s; %d \"cores\"%n", 
                      System.getenv("PROCESSOR_IDENTIFIER"),
                      Runtime.getRuntime().availableProcessors());
    java.util.Date now = new java.util.Date();
    System.out.printf("# Date: %s%n", 
      new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(now));
  }
}

interface Consumer<K,V> {
  void accept(K k, V v);
}

interface OurMap<K,V> {
  boolean containsKey(K k);
  V get(K k);
  V put(K k, V v);
  V putIfAbsent(K k, V v);
  V remove(K k);
  int size();
  void forEach(Consumer<K,V> consumer);
  void reallocateBuckets();
}

// ----------------------------------------------------------------------
// A hashmap that permits thread-safe concurrent operations, similar
// to a synchronized version of HashMap<K,V>.



// ----------------------------------------------------------------------
// A hash map that permits thread-safe concurrent operations, using
// lock striping (intrinsic locks on Objects created for the purpose).

// NOT IMPLEMENTED: get, putIfAbsent, size, remove and forEach.

// The bucketCount must be a multiple of the number lockCount of
// stripes, so that h % lockCount == (h % bucketCount) % lockCount and
// so that h % lockCount is invariant under doubling the number of
// buckets in method reallocateBuckets.  Otherwise there is a risk of
// locking a stripe, only to have the relevant entry moved to a
// different stripe by an intervening call to reallocateBuckets.

class StripedMap<K,V> implements OurMap<K,V> {
  // Synchronization policy: 
  //   buckets[hash] is guarded by locks[hash%lockCount]
  //   sizes[stripe] is guarded by locks[stripe]
  private volatile ItemNode<K,V>[] buckets;
  private final int lockCount;
  private final Object[] locks;
  private final int[] sizes;

  public StripedMap(int bucketCount, int lockCount) {
    if (bucketCount % lockCount != 0)
      throw new RuntimeException("bucket count must be a multiple of stripe count");
    this.lockCount = lockCount;
    this.buckets = makeBuckets(bucketCount);
    this.locks = new Object[lockCount];
    this.sizes = new int[lockCount];
    for (int stripe=0; stripe<lockCount; stripe++) 
      this.locks[stripe] = new Object();
  }

  @SuppressWarnings("unchecked") 
  private static <K,V> ItemNode<K,V>[] makeBuckets(int size) {
    // Java's @$#@?!! type system requires this unsafe cast    
    return (ItemNode<K,V>[])new ItemNode[size];
  }

  // Protect against poor hash functions and make non-negative
  private static <K> int getHash(K k) {
    final int kh = k.hashCode();
    return (kh ^ (kh >>> 16)) & 0x7FFFFFFF;  
  }

  // Return true if key k is in map, else false
  public boolean containsKey(K k) {
    final int h = getHash(k), stripe = h % lockCount;
    synchronized (locks[stripe]) {
      final int hash = h % buckets.length;
      return ItemNode.search(buckets[hash], k) != null;
    }
  }

  // Return value v associated with key k, or null
  public V get(K k) {
    final int h = getHash(k), stripe = h % lockCount;
    synchronized (locks[stripe]) {
      final int hash = h % buckets.length;
      ItemNode<K,V> node = ItemNode.search(buckets[hash], k);
      if (node != null) 
         return node.v;
      else
        return null;
    }
  }

  public int size() {
    int sum = 0;
    for(int stripe =0; stripe< locks.length; stripe++) {
      synchronized (locks[stripe]) {
        sum = sum + sizes[stripe];
      }
    }
    return sum;
  }

  // Put v at key k, or update if already present 
  public V put(K k, V v) {
    final int h = getHash(k), stripe = h % lockCount;
    synchronized (locks[stripe]) {
      final int hash = h % buckets.length;
      final ItemNode<K,V> node = ItemNode.search(buckets[hash], k);
      if (node != null) {
        V old = node.v;
        node.v = v;
        return old;
      } else {
        buckets[hash] = new ItemNode<K,V>(k, v, buckets[hash]);
        sizes[stripe]++;
        return null;
      }
    }
  }

  // Put v at key k only if absent
  public V putIfAbsent(K k, V v) {
    final int h = getHash(k), stripe = h % lockCount;
    synchronized(locks[stripe]) {
      final int hash = h% buckets.length;
      ItemNode<K,V> node = ItemNode.search(buckets[hash], k);
      if (node != null)
        return node.v;
      else {
        buckets[hash] = new ItemNode<K,V>(k, v, buckets[hash]);
        sizes[stripe] = sizes[stripe] + 1;
        if(sizes.length > (buckets.length/ locks.length)) this.reallocateBuckets();
        return null;
      }
    }
  }

  // Remove and return the value at key k if any, else return null
  public V remove(K k) {
    final int h = getHash(k), stripe = h % lockCount;
      synchronized(locks[stripe]) {
      final int hash = h % buckets.length;
      ItemNode<K,V> prev = buckets[hash];
      if (prev == null) 
        return null;
      else if (k.equals(prev.k)) {        // Delete first ItemNode
        V old = prev.v;
        sizes[stripe]--;
        buckets[hash] = prev.next;
        return old;
      } else {                            // Search later ItemNodes
        while (prev.next != null && !k.equals(prev.next.k))
          prev = prev.next;
        // Now prev.next == null || k.equals(prev.next.k)
        if (prev.next != null) {  // Delete ItemNode prev.next
          V old = prev.next.v;
          sizes[stripe]--; 
          prev.next = prev.next.next;
          return old;
        } else
          return null;
      }
    }
  }

  // Iterate over the hashmap's entries one stripe at a time;
  // stripewise less locking and more concurrency.  An intervening
  // reallocateBuckets (cannot happen while holding the lock on a
  // stripe so no need to take a local copy bs of the buckets field)
  // may redistribute items between buckets but each item stays in the
  // same stripe.
  public void forEach(Consumer<K,V> consumer) {
    for(int stripe = 0; stripe < locks.length; stripe ++) {
      synchronized(locks[stripe]) {
        for(int i=0; i< buckets.length; i++) {
          ItemNode<K,V> node = buckets[i];
          while (node != null) {
            consumer.accept(node.k, node.v);
            node = node.next;
          }
        }
      }
    }
  }

  // First lock all stripes.  Then double bucket table size, rehash,
  // and redistribute entries.  Since the number of stripes does not
  // change, and since buckets.length is a multiple of lockCount, a
  // key that belongs to stripe s because (getHash(k) % N) %
  // lockCount == s will continue to belong to stripe s.  Hence the
  // sizes array need not be recomputed.

  public void reallocateBuckets() {
    lockAllAndThen(() -> {
        final ItemNode<K,V>[] newBuckets = makeBuckets(2 * buckets.length);
        for (int hash=0; hash<buckets.length; hash++) {
          ItemNode<K,V> node = buckets[hash];
          while (node != null) {
            final int newHash = getHash(node.k) % newBuckets.length;
            ItemNode<K,V> next = node.next;
            node.next = newBuckets[newHash];
            newBuckets[newHash] = node;
            node = next;
          }
        }
        buckets = newBuckets;
      });
  }
  
  // Lock all stripes, perform the action, then unlock all stripes
  private void lockAllAndThen(Runnable action) {
    lockAllAndThen(0, action);
  }

  private void lockAllAndThen(int nextStripe, Runnable action) {
    if (nextStripe >= lockCount)
      action.run();
    else 
      synchronized (locks[nextStripe]) {
        lockAllAndThen(nextStripe + 1, action);
      }
  }

  static class ItemNode<K,V> {
    private final K k;
    private V v;
    private ItemNode<K,V> next;
    
    public ItemNode(K k, V v, ItemNode<K,V> next) {
      this.k = k;
      this.v = v;
      this.next = next;
    }

    // Assumes locks[getHash(k) % lockCount] is held by the thread
    public static <K,V> ItemNode<K,V> search(ItemNode<K,V> node, K k) {
      while (node != null && !k.equals(node.k))
        node = node.next;
      return node;
    }
  }
}

