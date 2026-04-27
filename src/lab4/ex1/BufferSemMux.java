package lab4.ex1;

import java.util.concurrent.Semaphore;
public class BufferSemMux implements Buffer {
	private int contents;
	private final Semaphore bufferFull;
	private final Semaphore bufferEmpty;

	// Constructor
	public BufferSemMux() {
		this.bufferFull = new Semaphore(0);
		this.bufferEmpty = new Semaphore(1);
	}

	// Put an item into buffer
	@Override
	public void put(int data) {
		try {
			bufferEmpty.acquire();
		} catch (InterruptedException e) { }

		contents = data;
		System.out.println("Prod " + Thread.currentThread().getName() + " No "+ data);
		System.out.println("The buffer is full");
		bufferFull.release(); 
	}

	// Get an item from buffer
	@Override
	public int get() {
		int data;

		try {
			bufferFull.acquire();
		} catch (InterruptedException e) { }

		data = contents;
		System.out.println("  Cons " + Thread.currentThread().getName() + " No "+ data);
		System.out.println("  The buffer is empty");
		bufferEmpty.release();
		return data;
	}
}

	
			
	
