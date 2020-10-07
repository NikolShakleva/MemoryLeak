// Week 3
// sestoft@itu.dk * 2015-09-09

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestWordStream {
  public static void main(String[] args) {
    String filename = "english-words.txt";
    System.out.println(readWords(filename).count());
    TestWordStream.printFirst10();
    TestWordStream.moreThan21Length();
    TestWordStream.findSomeWords();
    // System.out.println(TestWordStream.isPalindrome("String"));

    long start = System.nanoTime();
     TestWordStream.printPalindrome();
    System.out.println("Time for stream is: " + (System.nanoTime() - start));


    // Write threads for the parelle stream
    // Thread t1 = new Thread (() ->  {
    //   TestWordStream.parellelPrintPalindrome();
    // });

    // Thread t2 = new Thread (() ->  {
    //   TestWordStream.parellelPrintPalindrome();
    // });

    // // mesure the time when having two threads
    // long startParallel = System.nanoTime();
    // t1.start(); t2.start();
    // try {
    //   t1.join(); t2.join();
    // } catch (Exception exp) {
    //   System.out.println("Something went wrong");
    // }

     long startParallel = System.nanoTime();
     TestWordStream.parellelPrintPalindrome();
     System.out.println("Time for parellel stream is: " + (System.nanoTime() - startParallel));

      TestWordStream.aggregate();
      TestWordStream.grouping();
      //TestWordStream.multipleGrouing();
      System.out.print(TestWordStream.letters("Persistent").toString());
      TestWordStream.pipedTree();
      long startSumE = System.nanoTime();
      TestWordStream.sumE();
      System.out.println("Running time " + (System.nanoTime() - startSumE));

  }

  public static Stream<String> readWords(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      // TO DO: Implement properly
      Stream<String> stream = reader.lines();
      //return Stream.<String>empty(); 
      return stream;
    } catch (IOException exn) { 
      return Stream.<String>empty();
    }
  }

  public static void printFirst10 () {
    String filename = "english-words.txt";
    Stream<String> stream = readWords(filename);
    stream.limit(100)
    .forEach(System.out::println);
    //.forEach(e -> System.out.println(e));
  }

  public static void moreThan21Length () {
    String filename = "english-words.txt";
    Stream<String> stream = readWords(filename);
    stream.filter(s -> s.length()>=22)
    .forEach(System.out::println);
  }

  public static void findSomeWords () {
    String filename = "english-words.txt";
    Stream<String> stream = readWords(filename);
    stream.filter( s -> s.length()>=22).skip(50)
    .forEach(System.out::println);
  }

  public static boolean isPalindrome(String s) {
    String reverse = new StringBuffer(s).reverse().toString().toLowerCase();
    // TO DO: Implement properly
    return s.toLowerCase().equals(reverse); 
  }

  public static void printPalindrome() {
    String filename = "english-words.txt";
    Stream<String> stream = readWords(filename);
    stream.filter(e -> isPalindrome(e))
    .forEach(System.out::println);
  }

  public static void parellelPrintPalindrome() {
    String filename = "english-words.txt";
    Stream<String> stream = readWords(filename);
    // returns a paralell version of the already created stream 
    stream = stream.parallel();
    stream.filter(e -> isPalindrome(e))
    .forEach(System.out::println);
  }

  public static void aggregate () {
    String filename = "english-words.txt";
    Stream<String> stream = readWords(filename);
    var result = stream.mapToInt(s -> s.length())
    .summaryStatistics();
    System.out.println("Min: " + result.getMin() + " Max: " + result.getMax() + " Average:" + result.getAverage());
    //Stream<String> stream1 = readWords(filename);
    //Stream<String> stream2 = readWords(filename);
    // OptionalInt min = stream.mapToInt(s -> s.length()).min();
    // OptionalInt max = stream1.mapToInt(s -> s.length()).max();
    // OptionalDouble average = stream2.mapToInt(s -> s.length()).average();
    // System.out.println("Min: " + min.getAsInt() + " Max: " + max.getAsInt() + " Average:" + average.getAsDouble());
  }

  public static void grouping() {
    String filename = "english-words.txt";
    Stream<String> stream = readWords(filename);
    // group the words by length
    Map<Integer, List<String>>groups = stream.collect(Collectors.groupingBy(s -> s.length()));
    // print the geoups
    for(Integer key : groups.keySet()){
      System.out.print(key);
    }
  }

  public static void multipleGrouing() {
    String filename = "english-words.txt";
    Stream<String> stream = readWords(filename);
    var  elements = stream.collect(Collectors.groupingBy(s -> s.length()))
      .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey , Map.Entry::getValue, (a,b) -> a, TreeMap::new));
      
      elements.entrySet().forEach(entry->{
        System.out.println(entry.getKey() + " " + entry.getValue());  
     });
  }

  public static Map<Character,Integer> letters(String s) {
    Map<Character,Integer> res = new TreeMap<>();
    char [] array = s.toLowerCase().toCharArray();
    for(char key : array) {
      if(res.containsKey(key)) {
        int value  = res.get(key) +1;
        res.put(key,value);
      } else {
        res.put(key, 1);
      }
      }
      return res;
    }

    public static void  pipedTree () {
    String filename = "english-words.txt";
    Stream<String> stream = readWords(filename);
    stream.map(TestWordStream::letters).limit(100).forEach(e -> System.out.println(e.toString()));
   
    }
    public static void sumE () {
    String filename = "english-words.txt";
    Stream<String> stream = readWords(filename);
      int sum = stream.map(TestWordStream::letters).filter((tmap -> tmap.containsKey('e'))).map(tmap -> tmap.get('e')).reduce(0,(a,b) -> a + b);
      // int sum = stream.filter(s -> s.contains("e") || s.contains("E") ).map(TestWordStream::letters).map(tmap -> tmap.get('e')).reduce(0,(a,b) -> a + b);

      System.out.println(sum);
    }

    }
  