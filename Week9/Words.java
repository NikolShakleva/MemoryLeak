import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Words {

    // Observable to read in the lines
    final static Observable<String> readWords = Observable.create(new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(ObservableEmitter<String> s) throws Exception {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        Scanner sc = new Scanner (new File ("english-words.txt"));
                        
                        //while (sc.hasNextLine()) {
                            for (int i = 0; i< 100; i++){
                                String line = sc.nextLine();
                                s.onNext(line);
                            }
                        //}
                        
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();;
                    }
                }
            };
            t.start();
        }
    });

    // Observer to print the words

    final static Observer<String> display= new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {  }
        @Override
        public void onNext(String line) {
          System.out.println(line);
        }
        @Override
        public void onError(Throwable e) {System.out.println("onError: "); }
        @Override
        public void onComplete() { System.out.println("onComplete: All Done!");   }
      };


    public static void main(String[] args) {
       
        readWords.subscribe(display);
    
   
    }
}
