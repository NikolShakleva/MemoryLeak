package Week1;

/**
 * InnerLongcounter
 */
public class Longcounter {
    private long count = 0; 
    
    public void increment() {
        count = count + 1; 
    }
        public synchronized long get() {
            return count;
        } 
}