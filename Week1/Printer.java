public class Printer {
        public void print() {
            synchronized(this) {
           System.out.print("-");
           try { Thread.sleep(50); 
           } catch (InterruptedException exn) {      
           }
            System.out.print("|");}
   } 

   public static void main(String[] args) {
       var p = new Printer();
       int i = 5;

       Thread t1 = new Thread(() -> {
        while(i < 10)
            p.print();
        
        });
         Thread t2 = new Thread(() -> {
             while(i < 10) 
                p.print();
         });
            t1.start(); t2.start();
            try { t1.join(); t2.join(); }
            catch (InterruptedException exn) { 
            System.out.println("Some thread was interrupted");
            }
}
}