import javax.swing.*;
import java.util.concurrent.TimeUnit;

/* This example is inspired by the StopWatch app in Head First. Android Development
http://shop.oreilly.com/product/0636920029045.do

Modified to Java, October 2020 by Jørgen Staunstrup, ITU, jst@itu.dk */

public class Stopwatch2 {
	
	public static void main(String[] args) throws InterruptedException { 
        int N = 5;
        Thread[] watches = new Thread[N];
        for(int i = 0; i< watches.length; i++) {
            JFrame f = new JFrame("Stopwatch");  	
            f.setBounds(0, 0, 220, 220); 
            stopwatchUI   myUI = new stopwatchUI(0, f);
            f.setLayout(null);  
            f.setVisible(true);
            Thread t = new Thread(()-> { 
				try {
					while (true) {
						TimeUnit.MILLISECONDS.sleep(100);
						//TimeUnit.SECONDS.sleep(1);
						myUI.updateTime();
					}
				} catch (java.lang.InterruptedException e) {
					System.out.println(e.toString());
                }
            });

            watches[i] = t;
            t.start();
            // t.join();
        } 
       
	}
}

