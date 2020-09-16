import java.util.LinkedList;

/**
 * BoundedBuffer2
 * 
 * https://www.geeksforgeeks.org/producer-consumer-solution-using-threads-java/
 * 
 */
public class BoundedBuffer2 {

      
        // Create a list shared by producer and consumer 
        // Size of list is 2. 
        LinkedList<Integer> list = new LinkedList<>(); 
        int capacity = 2; 

        public BoundedBuffer2(int x){
            capacity = x;
        }
  
        // Function called by producer thread 
        public void produce() throws InterruptedException { 
            int value = 0; 
            while (true) { 
                synchronized (this) { 
                    // producer thread waits while list 
                    // is full 
                    while (list.size() == capacity) 
                        wait(); 
                    System.out.println("Producer produced-"
                                       + value); 
                    // to insert the jobs in the list 
                    list.add(value++); 
                    // notifies the consumer thread that 
                    // now it can start consuming 
                    notify(); 
                    // makes the working of program easier 
                    // to  understand 
                    Thread.sleep(1000); 
                } 
            } 
        } 
  
        // Function called by consumer thread 
        public void consume() throws InterruptedException { 
            while (true) { 
                synchronized (this) 
                { 
                    // consumer thread waits while list 
                    // is empty 
                    while (list.size() == 0) 
                        wait(); 
  
                    // to retrive the ifrst job in the list 
                    int val = list.removeFirst(); 
  
                    System.out.println("Consumer consumed-"
                                       + val); 
  
                    // Wake up producer thread 
                    notify(); 
  
                    // and sleep 
                    Thread.sleep(1000); 
                }
            }
        }
        public int getSize(){
            return list.size();
        }
        public static void main(String[] args) throws InterruptedException{
                    // Object of a class that has both produce() 
        // and consume() methods 
        final BoundedBuffer2 bf = new BoundedBuffer2(50);
        int count = 100; 
  
        Thread t1 = new Thread(() -> {
            for (int i=0; i <count; i++) {
                try{
                    bf.produce();
                }catch (InterruptedException exn) { 
                    System.out.println("Some thread was interrupted");
                }
                
            }
                
        });
    
        Thread t2 = new Thread(() -> {
            for (int i=0; i<count; i++) {
                try{
                bf.consume();
                } catch (InterruptedException exn) { 
                    System.out.println("Some thread was interrupted");
                }
            }
                
        });
  
        // Start both threads 
        t1.start(); 
        t2.start(); 
  
        // t1 finishes before t2 
        t1.join(); 
        t2.join(); 
        System.out.println("The count should be 0 and it is: " + bf.getSize());
    } 
    
        
}           