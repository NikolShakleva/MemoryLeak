import java.util.Arrays;
import java.util.List;

class Summa {
// this is a class variable therefore it can be used in the forEach lambda expression. It can be used 
 private static Integer sum = 0;

 public static void main(String[] args){
final List<Integer> a = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
// This is a local variable and it is not recognisable in the labda expression
// Integer sum;
a.forEach( x -> sum += x );
System.out.println("Sum: " + sum);
}
}