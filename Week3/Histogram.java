   // For week 3
// sestoft@itu.dk * 2014-09-04
// thdy@itu.dk * 2019
// kasper@itu.dk * 2020

public interface Histogram {
        public void increment(int bin);
        public int getCount(int bin);
        public float getPercentage(int bin);
        public int getSpan();
        public int getTotal();
}