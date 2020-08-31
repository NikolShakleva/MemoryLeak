package Week1;

/**
 * Exercise1
 * 
 * 
 * 
 */
public class Exercise1 {
    public static void main(String[] args) {

        final Longcounter lc = new Longcounter();
        final int counts = 10_000_000;
        Thread t1 = new Thread(() -> {
        for (int i=0; i<counts; i++) 
            lc.increment();
        });
        Thread t2 = new Thread(() -> {
        for (int i=0; i<counts; i++) 
           lc.increment();
        });
        t1.start(); t2.start();
        try { t1.join(); t2.join(); }
        catch (InterruptedException exn) { 
        System.out.println("Some thread was interrupted");
        }
        System.out.println("Count is " + lc.get() + " and should be " + 2*counts);
    
    }  
}