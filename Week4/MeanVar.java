/**
 * MeanVar
 */
public class MeanVar {

    public static void main(String[] args) {
        double[] a =
            //{ 30.7, 30.3, 30.1, 30.7, 50.2, 30.4, 30.9, 30.3, 30.5, 30.8 };
            {30.7, 100.2, 30.1, 30.7, 20.2, 30.4, 2, 30.3, 30.5, 5.4};
        double n = a.length;
        double sum = 0.0; 
        double sumSq = 0.0; 
        for (int i= 0; i < n; i++) { 
            sum += a[i];
        }
        double mean = sum / n;

        for (int i= 0; i < n; i++) { 
            sumSq += (a[i] - mean) * (a[i] - mean);
        }        
        double variance = sumSq / n;
        System.out.printf("%6.1f ns +/- %6.3f %n ", mean, variance); 
    }
}