public class MyAtomicInteger {
    
        private int count = 0;
        
    
public MyAtomicInteger () {
       
        }
    
        public synchronized int addAndGet(int e){
           return count += e;

        }
    
        public synchronized int get(){
            return count;
        }
    
       

        public static void main(String[] args) {
            final int range = 50_000;
            var atomic = new MyAtomicInteger();
                
                Thread t1 = new Thread(() -> {
                    for (int i=0; i<range; i++){
                        System.out.println(atomic.addAndGet(1));
                }});
        
                Thread t2 = new Thread(() -> {
                    for (int i=0; i<range; i++){
                        System.out.println(atomic.addAndGet(1));
                }});
                t1.start(); t2.start();
                try { 
                    t1.join(); t2.join(); }
                catch (InterruptedException exn) { 
                    System.out.println("Some thread was interrupted");
                }
                System.out.println("Range should be " + range*2 + " and it is " + atomic.get());            
            
    
    
        }
}