////////////////////
//PicoBank interface
////////////////////

public interface PicoBank  {
    void transfer(long amount, int source, int target);
    long balance(int accountNr);
  }
