import java.util.Random;
public class ExecutorAccountExperiments {

  static final int N = 10; // Number of accounts
  static final int NO_TRANSACTION=5;
  
  static final Account[] accounts = new Account[N];
  static final Random rnd = new Random();
  
  public static void main(String[] args){
    // Create empty accounts
    long startprog = System.nanoTime();
    for( int i = 0; i < N; i++){
      accounts[i] = new Account(i);
    }

    Thread t1 = new Thread(() -> {
      doNTransactions(NO_TRANSACTION);
      
    });

    Thread t2 = new Thread(() -> {
      doNTransactions(NO_TRANSACTION);
      
    });

    t1.start(); t2.start();
    try { 
        t1.join(); t2.join(); }
    catch (InterruptedException exn) { 
        System.out.println("Some thread was interrupted");
    }
     //long start = System.nanoTime();
    doNTransactions(NO_TRANSACTION);
    //long end = System.nanoTime()- start; 
    long endprog = System.nanoTime()- startprog;
    //System.out.println("transaction time: " + end);
    System.out.println("program time : " + endprog);


  }
  
  private static void doNTransactions(int noTransactions){
    for(int i = 0; i<noTransactions; i++){
      long amount = rnd.nextInt(5000)+100; // Just a random possitive amount
      int source = rnd.nextInt(N);
      int target = (source + rnd.nextInt(N-2)+1) % N; // make sure target <> source
      Transaction t =  new Transaction( amount, accounts[source], accounts[target]);
      t.transferMoney(t.source, t.target, t.amount);
    }
  }
  
  private static void doTransaction(Transaction t){
    
    System.out.println(t);
    //long transstart = System.nanoTime();
    t.transfer();
   // long transend = System.nanoTime()- transstart;
    //System.out.println("single transaction time: " + transend);
  }
  
  static class Transaction {
    final Account source, target;
    final long amount;
    
    Transaction(long amount, Account source, Account target){
      this.amount = amount;
      this.source = source;
      this.target = target;
    }
    
    public void transfer(){
      source.withdraw(amount);
      try{Thread.sleep(50);} catch(Exception e){}; // Simulate transaction time
      target.deposit(amount);
    }

   
public void transferMoney(final Account source, final Account target,final long amount){

class Helper {
      
public void transfer() {
    source.withdraw(amount);
    try{Thread.sleep(50);} 
    catch(Exception e){}; // Simulate transaction time
    target.deposit(amount);
}
}
int sourceid = source.id;
int targetid = target.id;

System.out.println(source.id + " " + target.id + " " + amount + " source balance: " + source.getBalance() + " target balance: " + target.getBalance());

if (sourceid < targetid) {
    synchronized (source) {
      synchronized (target) {
        new Helper().transfer();
      }
    }
}
else if (sourceid > targetid) {
    synchronized (source) {
      synchronized (target) {
        new Helper().transfer();
      }
    }
} 
System.out.println(source.id + " " + target.id + " " + amount + " source balance: " + source.getBalance() + " target balance: " + target.getBalance());

}


  public String toString(){
      return "Transfer " + amount + " from " + source.id + " to " + target.id;
    }
  }

  static class Account{
    // should have transaction history, owners, account-type, and 100 other real things
    public final int id;
    private long balance = 0;
    Account( int id ){ this.id = id;}
    public void deposit(long sum){ balance += sum; } 
    public void withdraw(long sum){ balance -= sum; }
    public long getBalance(){ return balance; }
  }

}