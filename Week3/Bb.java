public class Bb {
    /** 
 * A class to implement a bounded buffer.  
 * A thread trying to put to a full buffer or take from an empty one will block.
 * 
 * Taken from _Concepts in Programming Languages_ by John Mitchell
 * Comments and modifications by Scot Drysdale
 * @author John Mitchell
 */
    protected int numSlots; 
    private int[] buffer;
    private int takeOut = 0, putIn = 0; 
    private int count=0;
 
    public Bb(int numSlots) { 
       if(numSlots <= 0) {
          throw new IllegalArgumentException(
                                     "numSlots <= 0");
       } 
       this.numSlots = numSlots; 
       buffer = new int[numSlots];
    }
  
      /**
       * Put an item in the bounded buffer.  Block if full.
       * @param value the thing to add to the rear of the buffer
       * @throws InterruptedException
       */
    public synchronized void put(int value) throws InterruptedException {
       while (count == numSlots) 
           wait();
       
       buffer[putIn] = value;
       putIn = (putIn + 1) % numSlots;
       count++;
       notifyAll();
    }
 
      /** 
       * Remove an item from a bounded buffer.  Block if empty
       * @return the item removed
       * @throws InterruptedException
       */
    public synchronized int get() throws InterruptedException {
       while (count == 0) 
           wait();
       
       int value = buffer[takeOut];
       takeOut = (takeOut + 1) % numSlots;
       count--;
       notifyAll();
       return value;
    }

    public int getCount(){
        return count;
    }
    public int getIn(){
        return putIn;
    }
    public int getOut(){
        return takeOut;
    }

    public static void main(String[] args) throws InterruptedException{
        final Bb bf = new Bb(50);
        int count = 100; 
  
        Thread t1 = new Thread(() -> {
            for (int i=0; i <count; i++) {
                try{
                    bf.put(i);
                    System.out.println(bf.getCount());
                }catch (InterruptedException exn) { 
                    System.out.println("Some thread was interrupted");
                }
                
            }
                
        });
        
    
        Thread t2 = new Thread(() -> {
            for (int i=0; i<count; i++) {
                try{
                bf.get();
                } catch (InterruptedException exn) { 
                    System.out.println("Some thread was interrupted");
                }
            }
                
        });
        Thread t3 = new Thread(() -> {
            for (int i=0; i <count/2; i++) {
                try{
                    bf.put(i);
                }catch (InterruptedException exn) { 
                    System.out.println("Some thread was interrupted");
                }
                
            }
                
        });
  
        // Start both threads 
        t1.start(); 
        t2.start(); 
        t3.start();
  
        // t1 finishes before t2 
        t1.join(); 
        t2.join(); 
        t3.join();
        System.out.println("Putin should be 0 and it is: " + bf.getIn());
        System.out.println("The count should be 0 and it is: " + bf.getOut());
        System.out.println("The count should be 0 and it is: " + bf.getCount());

    }
 
}
