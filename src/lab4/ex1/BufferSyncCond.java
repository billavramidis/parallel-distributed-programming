package lab4.ex1;

public class BufferSyncCond implements Buffer{
	private int contents;
	private boolean isFull;

	// Constructor
	public BufferSyncCond() {
		this.isFull = false;
	}

	// Put an item into buffer
	@Override
	public synchronized void put(int data)
	{
		while (isFull) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		contents = data;
		System.out.println("Prod " + Thread.currentThread().getName() + " No "+ data);
		System.out.println("The buffer is full");

		isFull = true;
		//buffer was empty
		notifyAll();
	}

	// Get an item from buffer
	@Override
	public synchronized int get()
	{
		while (!isFull) {
			try {
				wait();
			}
			catch (InterruptedException e) {}
		}
		int data = contents;
		System.out.println("  Cons " + Thread.currentThread().getName() + " No "+ data);
		System.out.println("   The buffer is empty");

		isFull = false;

		notifyAll();
		return data;
	}
}

	
			
	
