// import java.awt.event.*;  
// import javax.swing.*;
// import java.util.concurrent.TimeUnit;

// import io.reactivex.Observable;
// import io.reactivex.ObservableEmitter;
// import io.reactivex.ObservableOnSubscribe;
// import io.reactivex.Observer;
// import io.reactivex.disposables.Disposable;

// class rxButton {
//   private static  rxButton startButton= new rxButton("Start");	
//   private static JButton stopButton= new JButton("Stop");
//   private static JButton resetButton= new JButton("Reset");
//   private static JFrame lf;
//   // private static JButton nameButton;
//   private static stopwatchUI myUI;


//   final static Observable<Integer> rxStart
//       = Observable.create(new ObservableOnSubscribe<Integer>() {
//         @Override
//         public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//         	startButton.setBounds(myUI.getLx(), 50, 95, 25); 
// 			startButton.addActionListener(new ActionListener(){  
// 				public void actionPerformed(ActionEvent ee){  
//                 myUI.getlC().setRunning(true);  
//                 TimeUnit.MILLISECONDS.sleep(100);
//                 e.onNext(100);
// 						}  
//         });  
//         }
//   }); 

//   final static Observable<Integer> rxStop
//       = Observable.create(new ObservableOnSubscribe<Integer>() {
//         @Override
//         public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//           stopButton.setBounds(myUI.getLx(), 90, 95, 25); 
//           stopButton.addActionListener(new ActionListener(){  
//             public void actionPerformed(ActionEvent ee){  
//                     myUI.getlC().setRunning(false); 
//                     TimeUnit.MILLISECONDS.sleep(100);
//                     e.onNext(100);
//                 }  
//             });
//         }
//   }); 

//   final static Observable<Integer> rxReset
//       = Observable.create(new ObservableOnSubscribe<Integer>() {
//         @Override
//         public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//         	resetButton.setBounds(myUI.getLx(), 130, 95, 25); 				
// 			resetButton.addActionListener(new ActionListener(){  
// 				public void actionPerformed(ActionEvent ee){  
//           synchronized(myUI) {
//             myUI.setlC( new SecCounter(0, false));
//             myUI.updateDisplay("0:00:00:0");
//             TimeUnit.MILLISECONDS.sleep(100);
//             e.onNext(100);
//           }} 
// 				}); 
//         }
//   }); 

//   final static Observer<Integer> display= new Observer<Integer>() {
//     @Override
//     public void onSubscribe(Disposable d) {  }
//     @Override
//     public void onNext(Integer value) {
//       try {
//         // TimeUnit.SECONDS.sleep(1);
//         myUI.updateTime();
//       } catch (java.lang.InterruptedException e) {
//                 System.out.println(e.toString());
//       };
//       System.out.println();
//     }
//     @Override
//     public void onError(Throwable e) {System.out.println("onError: "); }
//     @Override
//     public void onComplete() { System.out.println("onComplete: All Done!");   }
//   };

//   // public rxButton(String s) {
//   //   nameButton = new JButton(s);
//   // }

//   public static void main(String[] args) { 
//     JFrame f=new JFrame("Stopwatch");  	
// 		f.setBounds(0, 0, 220, 220);
//     nameButton.setBounds(50, 50, 95, 25); 
//   	f.add(nameButton); 
//     f.setLayout(null);  
// 		f.setVisible(true);
 
//     rxPush.subscribe(display);

//   }		
// }