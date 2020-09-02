public class Printer {

    public void print() {
        synchronized(this) {
            System.out.print("-");
            try { 
                Thread.sleep(50); 
            } catch (InterruptedException exn) {}
            System.out.print("|");
        }
   } 

   public static void printStatic() {
       synchronized(Printer.class) {
        System.out.print("-");
        try { 
            Thread.sleep(50); 
        } catch (InterruptedException exn) {}
        System.out.print("|"); 
       }
   }

    public static void main(String[] args) {
        var p = new Printer();

        Thread t1 = new Thread(() -> {
            while(true)
                //p.print();
                Printer.printStatic();
        });

        Thread t2 = new Thread(() -> {
            while(true) 
                //p.print();
                Printer.printStatic();
        });
            
        t1.start(); t2.start();
        try { t1.join(); t2.join(); }
        catch (InterruptedException exn) { 
            System.out.println("Some thread was interrupted");
        }
}
}