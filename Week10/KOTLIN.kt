// EXERCISE 10.2
fun main() {
    val list= listOf("aba","x", "xyz", "wasitacaroracatisaw","palindrome",null)
    list.filter(::palindrome).forEach(::println)
    }

    fun palindrome(s:String?):Boolean {
    var p = s ?: return false
    var first=0; var last = p.length-1
    while (first < last){
    if (p[first++] != p[last--]) return false 
    }
    return true
    }



// EXERCISE 10.3.1
fun ifNotEmpty (s: String?, lambdaFunction: (String) -> Unit) {
    if((s == null) || (s == "")) return
lambda is a function, I need to call the lambda function s1 with the string s i want to print
    return lambdaFunction(s);
}
// YOU CAN ALSO GIVE A PARAMETER TO THE LAMBDA FUNCTION(S1)
fun ifNotEmpty1 (s: String?, block: (S1:String)-> Unit) {
    if((s == null) || (s == "")) return
   return block(s)
}

fun main() {
   ifNotEmpty ("", {s -> println(s)} )



// EXERCISE 10.3.2
// EXTEND THE FUNCTION ifNotEmpty1 TO THE String class
fun String.ifNotEmpty1 (s: String?, block: (s1:String)-> Unit) {
    if((s == null) || (s == "")) return
   return block(s)
}

fun main() {
  String().ifNotEmpty1("Joe") { s-> println(s) }
}



// EXERCISE 10.4
// LEAP YEAR IN THE MAIN
fun main() {
    val seq = sequence {
        yieldAll(1940..2040 step 4)
    }
    
  println(seq.toList())
  }

// LEAP YEAR AS A FUNCTION
fun leapYear(): Sequence<Int> = sequence {
	yieldAll(1940..2040 step 4) }


fun main() {
  val list = leapYear().toList();
	println(list)
}


// EXERCISE 10.5
fun <T>Sequence<T>.dropEverySecond(): Sequence<T> = sequence {
    var iter = this@dropEverySecond.iterator()
    // TO DO
    while(iter.hasNext()) {
    println(iter.next())
    if(iter.hasNext()) iter.next()
    }

}

fun main() {
    val seq = (1..40).asSequence()
IF WE REMOVE THE FILTER OPERATION WE STILL GET THE SAME RESULT
    seq.dropEverySecond().filter { v -> v%3 == 0 }.forEach(::println)
    }



// EXERCISE 1.6 STATE MACHINE LEAP YEAR
class MySequence : Iterator<Int> {
    val itr = (1940..2040 step 4).iterator()
    var state : Int = 0; // state of state machine override 
    override fun hasNext(): Boolean { return state != -1 }
    override fun next() : Int {
    var ret = 0
        when(state) {
           0 -> if (itr.hasNext()) {
               ret = itr.next()
           } else { (!itr.hasNext()) 
                   state = -1
                   ret = -1
                 }
        }
    return ret
    }
}

fun main () {
   val seq = MySequence()
   while(seq.hasNext()) {
   println(seq.next())
   }
}



// EXERCISE 1.6 STATE MACHINE dropEverySecond
class MySequence : Iterator<Int> {
    val seq = (1..40).asSequence()
    val itr = seq.dropEverySecond().iterator()
    var state : Int = 0; // state of state machine override 
    override fun hasNext(): Boolean { return state != -1 }
    override fun next() : Int {
    var ret = 0
        when(state) {
           0 -> if (itr.hasNext()) {
               ret = itr.next()
           } else { (!itr.hasNext()) 
                   state = -1
                   ret = -1
                 }
        }
    return ret
    }
}

fun main () {
val obj = MySequence()
while(obj.hasNext()) {
    println(obj.next())
}
}

// EXERCISE 1.7 MERGING AND SORTING STREAMS 

fun merge(s1: Sequence<Int>, s2: Sequence<Int>): Sequence<Int> {
	var seq: Sequence<Int> = sequence {
        
        yieldAll(s1)
        yieldAll(s2)
    }    
// 	sequence.sorted() sort the sequence but in Kotlin when you call a sequence you alsywa get the originla sequence 
//  hence doing this will return the originla sequence not the sorted sequence 
//  seq.sorted()
//  return seq
//  therefore you need to return seq.sorted() 
    return seq.sorted()
}
    
fun main () {
    println(merge(sequenceOf(4,6,7,8), sequenceOf(1,3,5)).toList())
}