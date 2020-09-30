import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExecutorAccountExperiments3 implements ExecutorService {
  
  static final int N = 10; // Number of accounts
  static final int NO_TRANSACTION = 5;

  static final Account[] accounts = new Account[N];
  static final Random rnd = new Random();

  public static void main(String[] args) {
    // Create empty accounts
    for (int i = 0; i < N; i++) {
      accounts[i] = new Account(i);
    }
    Runnable task = new Runnable() {
      public void run() {
        doNTransactions(NO_TRANSACTION);
      }
    };
    //exec.execute(task);
      var exec = Executors.newFixedThreadPool(4);
      //var a = new ExecutorAccountExperiments2(i);
      long start = System.nanoTime();
      exec.execute(task);
      exec.shutdown();
      long end = System.nanoTime()-start;
      System.out.println("number of threads: " + 4 +" time: "+ end);


    
  }

  private static void doNTransactions(int noTransactions) {
    for (int i = 0; i < noTransactions; i++) {
      long amount = rnd.nextInt(5000) + 100; // Just a random possitive amount
      int source = rnd.nextInt(N);
      int target = (source + rnd.nextInt(N - 2) + 1) % N; // make sure target <> source
      doTransaction(new Transaction(amount, accounts[source], accounts[target]));
    }
  }

  private static void doTransaction(Transaction t) {
    //System.out.println(t);
    t.transfer();
    //System.out.println(t);
  }

  static class Transaction {
    final Account source, target;
    final long amount;

    Transaction(long amount, Account source, Account target) {
      this.amount = amount;
      this.source = source;
      this.target = target;
    }

    public void transfer() {
      source.withdraw(amount);
      try {
        Thread.sleep(40);
      } catch (Exception e) {
      }
      ; // Simulate transaction time
      target.deposit(amount);

    }

    public String toString() {
      return "Transfer " + amount + " from " + source.id + " to " + target.id + "\n" + source.getBalance() + " "
          + target.getBalance();
    }
  }

  static class Account {
    // should have transaction history, owners, account-type, and 100 other real
    // things
    public final int id;
    private long balance = 0;

    Account(int id) {
      this.id = id;
    }

    public void deposit(long sum) {
      balance += sum;
    }

    public void withdraw(long sum) {
      balance -= sum;
    }

    public long getBalance() {
      return balance;
    }
  }

  @Override
  public void execute(Runnable command) {
    // TODO Auto-generated method stub

  }

  @Override
  public void shutdown() {
    // TODO Auto-generated method stub

  }

  @Override
  public List<Runnable> shutdownNow() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isShutdown() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isTerminated() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public <T> Future<T> submit(Callable<T> task) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> Future<T> submit(Runnable task, T result) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Future<?> submit(Runnable task) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
      throws InterruptedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    // TODO Auto-generated method stub
    return null;
  }

  // @Override
  // public void execute(Runnable command) {
  //   // TODO Auto-generated method stub

  // }

}

