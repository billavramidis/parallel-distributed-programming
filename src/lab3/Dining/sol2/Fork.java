package lab3.Dining.sol2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Fork {

  private int identity;
  private Lock lock = new ReentrantLock();

  public Fork(int id) {identity = id;}

  public void get() {
  	lock.lock();
  }

  public void put() {
       lock.unlock();
  }
}
