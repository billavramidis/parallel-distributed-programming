package lab3.Dining.sol1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Fork {
  private final Lock lock = new ReentrantLock();

  public Fork() {}

  public void get() {
  	lock.lock();
  }

  public void put() {
       lock.unlock();
  }
}
