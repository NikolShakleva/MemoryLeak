public class Person {
    private static long count=0;
    private final long id;
    private final String name;
    private int zip;
    private String address;

    public Person(){
    id = getCount()+1;
    incrementCount();
    name = "name" + id;
    zip= 1234;
    address= "ITU";
    }

    public synchronized void incrementCount(){
        count++;
    }

    public synchronized long getId(){
        return id;

    }

    public synchronized static long getCount(){
        return count;
    }

    public synchronized void setLocation(String address, int zip){
        this.address= address;
        this.zip= zip;
    }


    public static void main(String[] args) {
        final int range = 50000;
            
            Thread t1 = new Thread(() -> {
                for (int i=0; i<range; i++){
                    Person p = new Person();
                    System.out.println(p.getId());
            }});
    
            Thread t2 = new Thread(() -> {
                for (int i=0; i<range; i++){
                    Person p = new Person();
                    System.out.println(p.getId());
            }});
    
            t1.start(); t2.start();
            try { 
                t1.join(); t2.join(); }
            catch (InterruptedException exn) { 
                System.out.println("Some thread was interrupted");
            }
            System.out.println("Range should be " + range*2 + " and it is " + Person.getCount());            
        


    }


}
