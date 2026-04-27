package lab3.Dining.sol3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Fork {
  private Lock lock = new ReentrantLock();

  public Fork(){};

  public boolean tryGet() {
  	return lock.tryLock();
  }

  void put() {
       lock.unlock();
  }
}
