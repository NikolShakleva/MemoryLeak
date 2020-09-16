import java.util.*;
import java.util.concurrent.*;

public class BoundedBuffer2 {
    
        private final Set<Integer> set;
        private final Semaphore sem;


        public BoundedBuffer2(int limit) {
        this.set = Collections.synchronizedSet(new HashSet<Integer>());
        sem = new Semaphore(limit);
        }

        public boolean add(Integer o) throws InterruptedException {
        sem.acquire();
        boolean wasAdded = false;
        try {
        wasAdded = set.add(o);
        return wasAdded;
        }
        finally {
        if (!wasAdded)
        sem.release();
        }
        }
        public boolean remove(Integer o) {
        boolean wasRemoved = set.remove(o);
        if (wasRemoved)
        sem.release();
        return wasRemoved;
        }

        public int getSize() {
           return set.size();
        }


        public static void main(String[] args) {
            int count = 100;
            BoundedBuffer2 bf = new BoundedBuffer2(50);
            Thread t1 = new Thread(() -> {
                for (int i=0; i<count; i++) {
                   try { bf.add(i);
                } catch ( InterruptedException e)
                {
                    System.out.println("Some thread was interrupted");
                }
            }
                    
            });
        
            Thread t2 = new Thread(() -> {
                for (int i=0; i<count; i++) {
                    bf.remove(i);
                }
                    
            });
        
        
            Thread t3 = new Thread(() -> {
                for (int i=0; i<count; i++) {
                   try { bf.add(i);
                } catch ( InterruptedException e)
                {
                    System.out.println("Some thread was interrupted");
                }
            }
                    
            });
        
            // Thread t4 = new Thread(() -> {
            //     for (int i=0; i<count; i++) {
            //         bf.pop();
            //     }
                    
            // });
        
            t1.start(); t2.start(); t3.start(); //t4.start();
            try { 
                t1.join(); t2.join(); t3.join();  }
            catch (InterruptedException exn) { 
                System.out.println("Some thread was interrupted");
            }
        
        System.out.println("The count should be 0 and it is: " + bf.getSize());
        }
        }

