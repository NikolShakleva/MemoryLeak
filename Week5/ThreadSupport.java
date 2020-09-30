
public class ThreadSupport {
    public static void main(String[] args) {
        //int limit = 10_000;
        int tcounter = 0;
        while (true){

             new Thread(()->{ 
                 while (true){
                    int c = 0;
                    c++;
                 }

             }).start();
             tcounter++;
             System.out.println("Thread " + tcounter);
        }
    }
}
