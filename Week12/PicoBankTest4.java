import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

class PicoBankTest4 {
  private static final int N = 10; // Number of bank accounts in bank
  public static void main(String[] args) throws InterruptedException, BrokenBarrierException{
     for(int i = 0; i < 10; i++) {
      // testBankSequential(new PicoBankBasic1( N ));
      testBankParellel(new PicoBankBasic4 (N));
    }
 }
  
  private static void testBankSequential(PicoBank bank){
    long start = System.nanoTime();
    for (int i=0; i< 10_000; i++)
      doRandomTransfer(bank);
    long time = System.nanoTime() - start;
    long sum = 0L;
    for (int i=0; i<N; i++) sum += bank.balance(i);
    System.out.println("Single thread test: " + (sum == 0 ? "SUCCESS" : "FAILURE"));
    System.out.printf("Single thread time: %,dns\n", time );
  }

  private static void testBankParellel(PicoBank bank) throws InterruptedException, BrokenBarrierException {
    final int noCores = 4;
    final int noThreads = noCores * 10;
    final int repetition = 10_000;
    final CyclicBarrier barrier = new CyclicBarrier(noThreads+1);
    Runnable thread = () -> {
      try {
        barrier.await();
        for (int i=0; i< repetition; i++) {
          doRandomTransfer(bank);
        }
        barrier.await();
    } catch (Exception ie) {
        System.out.println("Something went wrong");
      }
    };

    for(int j = 0; j < noThreads; j++) {
      new Thread (thread).start();
    }
    barrier.await();
    long start = System.nanoTime();
    barrier.await();

    long time = System.nanoTime() - start;
    long sum = 0L;
    for (int i=0; i<N; i++) sum += bank.balance(i);
    System.out.println("Multiple threads test: " + (sum == 0 ? "SUCCESS" : "FAILURE"));
    System.out.printf("Multiple threads time: %,dns\n", time );
  }


  static final Random rnd = new Random(); // replace this with efficient Random
  public static void doRandomTransfer(PicoBank bank){
    long amount = rnd.nextInt(5000)+100; // Just a random possitive amount
    int source = rnd.nextInt(N);
    int target = (source + rnd.nextInt(N-2)+1) % N; // make sure target <> source
    bank.transfer(amount, source, target);
  }
}

/////////////////////
//PicoBankBasic4 class
/////////////////////

class PicoBankBasic4 implements PicoBank{
  final int N; // Number of accounts
  final Account1[] accounts ;
  
  PicoBankBasic4(int noAccounts){
    N = noAccounts;
    accounts = new Account1[N];
    for( int i = 0; i < N; i++){
      accounts[i] = new Account1(i);
    }
  }
  
  public void transfer(long amount, int source, int target){
      accounts[source].withdraw(amount);
      accounts[target].deposit(amount);
  }
  public long balance(int accountNr){
    return accounts[accountNr].getBalance();
  } 

  static class Account1 {
    // should have transaction history, owners, account-type, and 100 other real things
    public final int id;
    private AtomicLong balance = new AtomicLong();


    Account1( int id ){ 
        this.id = id;
    }

    public void deposit(long sum) {
        long current = balance.get(); 
        while(!balance.compareAndSet(current, current + sum)) {
            current = balance.get();
        }
        } 

    public void withdraw(long sum) {
        long current = balance.get(); 
        while(!balance.compareAndSet(current, current - sum)) {
            current = balance.get();
        }
        } 
    
    public  long getBalance(){ 
        return balance.get(); 
    }
  }

}

