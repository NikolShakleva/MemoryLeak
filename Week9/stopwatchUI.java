import java.awt.event.*;  
import javax.swing.*; 
import java.util.Locale;
import java.util.concurrent.TimeUnit;

class stopwatchUI {
		final private String allzero = "0:00:00:0";
		private int lx;
		private static JFrame lf;
		private SecCounter lC;
		
		private JButton startButton= new JButton("Start");	
		private JButton stopButton= new JButton("Stop");
		private JButton resetButton= new JButton("Reset");		
		private JTextField tf= new JTextField();
		
		public void updateTime(){
      if ( lC.incr() ) {
		int tenthSecond= lC.seconds;
		int seconds = tenthSecond/10;
        int hours= seconds/3600;
        int minutes= (seconds%3600)/60;
		int secs= seconds%60;
		tenthSecond = tenthSecond % 10;
        String time= String.format(Locale.getDefault(),	"%d:%02d:%02d:%d", hours, minutes, secs, tenthSecond);
        tf.setText(time);
      }
		};

		public String getTime () {
			String time = tf.getText();
			return time;
		}
	
		public void updateDisplay(String time) {
			tf.setText(time);
		}

		public SecCounter getlC() {
			return lC;
		}

		public int getLx() {
			return lx;
		}

		public void setlC(SecCounter lC) {
			this.lC = lC;
		}

    public boolean running() { return lC.running();  }
		
		public stopwatchUI(int x, JFrame jF){
			lx= x+50; lf= jF;	
			tf.setBounds(lx, 10, 60, 20); 
			tf.setText(allzero);

      lC= new SecCounter(0, false);
	
			startButton.setBounds(lx, 50, 95, 25); 
			startButton.addActionListener(new ActionListener(){  
				public void actionPerformed(ActionEvent e){  
								lC.setRunning(true);  
						}  
				});  
				
			stopButton.setBounds(lx, 90, 95, 25); 
			stopButton.addActionListener(new ActionListener(){  
				public void actionPerformed(ActionEvent e){  
								lC.setRunning(false); 
						}  
				});

			resetButton.setBounds(lx, 130, 95, 25); 				
			resetButton.addActionListener(new ActionListener(){  
				public void actionPerformed(ActionEvent e){  
          synchronized(this) {
            lC= new SecCounter(0, false);
            tf.setText(allzero);
          }} 
				}); 

			// set up user interface
			lf.add(tf);
			lf.add(startButton);  
			lf.add(stopButton);
			lf.add(resetButton);
		}		
	}