package lab4.ex1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class BufferLockCond implements Buffer{
	private int contents;
	private boolean isFull;
	private final Lock lock;
	private final Condition isFullCondition;

	// Constructor
	public BufferLockCond() {
		this.lock = new ReentrantLock();
		this.isFull = false;

		this.isFullCondition = lock.newCondition();
	}

	// Put an item into buffer
	@Override
	public void put(int data) {
		lock.lock();
			try {
				while (isFull){
					try {
						isFullCondition.await();
					} catch (InterruptedException e) { }
				}
			contents = data;
			System.out.println("Prod " + Thread.currentThread().getName() + " No "+ data);
			System.out.println("The buffer is full");
			isFull = true;
			isFullCondition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	// Get an item from buffer
	@Override
	public int get() {
		int data = 0;

		lock.lock();
		try {
			while (!isFull) {
				try {
					isFullCondition.await();
				} catch (InterruptedException e) { }
			}
			data = contents;
			System.out.println("   Cons " + Thread.currentThread().getName() + " No "+ data);
			System.out.println("   The buffer is empty");
			isFull = false;
			//buffer was full
			isFullCondition.signalAll();
		} finally {
			lock.unlock() ;
		}
		return data;
	}
}

	
			
	
