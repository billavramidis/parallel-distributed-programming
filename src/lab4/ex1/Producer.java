package lab4.ex1;

public class Producer extends Thread {
	private final Buffer buff;
	private final int reps;
	private final int scale;

	// Constructor
	public Producer(Buffer b, int r, int s) {
		this.buff = b;
		this.reps = r;
		this.scale = s;
	}

	// ex1.Producer runs for reps times with random(scale) intervals
	public void run() {
		for(int i = 0; i < reps; i++) {
			buff.put(i);
			try {
				sleep((int)(Math.random()*scale));
			} catch (InterruptedException e) { }
		}
	}
}
