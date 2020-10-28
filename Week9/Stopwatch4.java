import javax.swing.*;
import java.util.concurrent.TimeUnit;

/* This example is inspired by the StopWatch app in Head First. Android Development
http://shop.oreilly.com/product/0636920029045.do

Modified to Java, October 2020 by Jørgen Staunstrup, ITU, jst@itu.dk */

public class Stopwatch4 {
	
	public static void main(String[] args) throws InterruptedException { 
        int N = 6;
        Thread[] watches = new Thread[N];
        for(int i = 0; i< watches.length; i+=2) {
            JFrame f = new JFrame("Stopwatch");  	
            f.setBounds(0, 0, 220, 220); 
            stopwatchUI   myUI1 = new stopwatchUI(0, f);
            f.setLayout(null);  
            f.setVisible(true);
            var bf1 = new BoundedBuffer(10);
            Thread t1 = new Thread(()-> { 
				try {
					while (true) {
						TimeUnit.MILLISECONDS.sleep(100);
						//TimeUnit.SECONDS.sleep(1);
                        myUI1.updateTime();
                        String time = myUI1.getTime();
                       bf1.sendMessage(time);
					}
				} catch (java.lang.InterruptedException e) {
					System.out.println(e.toString());
                }
            });

            Thread t2 = new Thread(()-> {
                while(true) {
						try {
                            var display = bf1.receiveMessage();
                            myUI1.updateDisplay(display);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
            });

            t1.start();
            t2.start();
        } 
       
	}
}
