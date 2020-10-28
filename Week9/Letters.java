
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class Letters {
      // Observable to read in the lines
      final static Observable<String> readWords = Observable.create(new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(ObservableEmitter<String> s) throws Exception {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        String filename = "english-words.txt";
                        BufferedReader reader = new BufferedReader(new FileReader(filename));
                        Stream<String> stream = reader.lines().filter(s -> s.length()>=22);
                        List<String> longerthan22 = stream.collect(Collectors.toList());
                        for (int i = 0; i< longerthan22.size(); i++){
                            String line = longerthan22.get(i);
                            s.onNext(line);
                        }
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
