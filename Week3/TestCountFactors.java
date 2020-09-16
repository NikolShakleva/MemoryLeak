
class TestCountFactors {
    
  
    public static int countFactors(int p) {
      if (p < 2) 
        return 0;
      int factorCount = 1, k = 2;
      while (p >= k * k) {
        if (p % k == 0) {
      factorCount++;
      p /= k;
        } else 
      k++;
      }
      return factorCount;
    }


    public static void main(String[] args) {
        final int range = 5_000_000;
        int count = 0;
        long start = System.nanoTime();
        for (int p=0; p<range; p++)
          count += countFactors(p);
       long end = System.nanoTime();
        System.out.printf("Total number of factors is %9d%n", count);
        // divide by 1 billion in order to get the number of seconds
        System.out.println("The total time is: " +  (end-start)/1_000_000_000);
      }
  }
  