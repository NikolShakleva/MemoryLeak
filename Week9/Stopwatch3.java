import javax.swing.*; 
import java.util.concurrent.TimeUnit;

/* This example is inspired by the StopWatch app in Head First. Android Development
http://shop.oreilly.com/product/0636920029045.do

Modified to Java, October 2020 by Jørgen Staunstrup, ITU, jst@itu.dk */

public class Stopwatch3 {

  private static stopwatchUI myUI;
	
	public static void main(String[] args) { 
		JFrame f = new JFrame("Stopwatch");  	
		f.setBounds(0, 0, 220, 220); 
    myUI= new stopwatchUI(0, f);
 
    f.setLayout(null);  
        f.setVisible(true);
		BoundedBuffer bf = new BoundedBuffer(10); 
		// stopwatchUI myUiThread = new stopwatchUI(0,f);
		
		Thread t= new Thread() {
			private int seconds= 0;
			// Background Thread simulation a clock ticking every 1 seconde
			@Override
			public void run() {
				int temp= 0;
				try {
					while ( true ) {
						TimeUnit.MILLISECONDS.sleep(100);
						//TimeUnit.SECONDS.sleep(1);
						// String time = myUiThread.updateTime();
						 myUI.updateTime();
						 String time = myUI.getTime();
						bf.sendMessage(time);
					}
				} catch (java.lang.InterruptedException e) {
					System.out.println(e.toString());
				}
			} 
		}; 
    t.start();
    try {
		while(true) {
		var display = bf.receiveMessage();
		myUI.updateDisplay(display);
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}


