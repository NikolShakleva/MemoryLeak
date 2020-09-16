import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



class BoundedBuffer {
private final int limit;
private final AtomicInteger size = new AtomicInteger(0);
private final Lock lock = new ReentrantLock();
// private boolean full = false;
// private boolean empty = false;
private ConcurrentLinkedQueue<Integer>  buffer;
private final Condition full = lock.newCondition();
private final Condition empty = lock.newCondition();


public BoundedBuffer ( int limit) {
    this.limit = limit;
    buffer = new ConcurrentLinkedQueue<Integer>();
}

 public AtomicInteger getSize() {
    return size;
}

public void add(Integer e) {
    if(helperAdd()) {
        size.incrementAndGet();
        buffer.offer(e);
    } else {
        try {
            wait();
        add(e);
        } catch (InterruptedException exception) {
            System.out.println("Some thread was interrupted");
        }
    }
}
    

private synchronized boolean helperAdd() {
    if(limit> size.intValue()) {
        return true;
    }
        else {
            return false;
        }
}



public  void pop () {
    if((helperPop())) {
        size.decrementAndGet();
        buffer.poll();
    } else {
         try {
        wait();
        pop();
} catch (InterruptedException exception) {
    System.out.println("Some thread was interrupted");
}
}
}


private synchronized boolean helperPop() {
    if(size.intValue()> 0 ) {
        return true;
    } else {
        return false;
    }
}



public static void main(String[] args) {
    int count = 100;
    BoundedBuffer bf = new BoundedBuffer(50);
    Thread t1 = new Thread(() -> {
        for (int i=0; i<count; i++) {
            bf.add(i);
        }
            
    });

    Thread t2 = new Thread(() -> {
        for (int i=0; i<count; i++) {
            bf.pop();
        }
            
    });


    Thread t3 = new Thread(() -> {
        for (int i=0; i<count; i++) {
            bf.add(i);
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