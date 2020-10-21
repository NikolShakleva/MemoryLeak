// Crude wall clock timing utility, measuring time in seconds
   
public class Timer {
  private long start, spent = 0;
  private double totalTime = 0;
  public Timer() { play(); }
  public void check() { totalTime = (System.nanoTime()-start+spent)/1e9; }
  public void pause() { spent += System.nanoTime()-start; }
  public void play() { start = System.nanoTime(); }
  public double getTotal(){return totalTime;}
}
