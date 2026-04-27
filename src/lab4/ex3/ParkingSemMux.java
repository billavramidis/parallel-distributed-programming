package lab4.ex3;

import java.util.concurrent.Semaphore;

public class ParkingSemMux implements Parking{
	private int spaces;
	private final int waitscale;
	private final int inscale;
    private final Semaphore spaceTracker;
    private final Semaphore mutex;
    
    public ParkingSemMux(int capacity) {
       this.spaces = capacity;
       this.waitscale = 10;
       this.inscale = 5;
       this.mutex = new Semaphore(1);
       this.spaceTracker = new Semaphore(spaces);
    }

    @Override
	public void arrive() {
		//Car arrival with random delay
		try {
		   Thread.sleep((int)(Math.random()*waitscale));
		} catch (InterruptedException e) { }

        try {
            spaceTracker.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Thread.currentThread().getName()+" arrival");
        //Car entering
        System.out.println(Thread.currentThread().getName()+"     parking");

        spaces--;

        System.out.println("free "+ spaces);

        mutex.release();
	}

    @Override
    public void depart(){
        //Increment capacity

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Car departure
        System.out.println(Thread.currentThread().getName()+"         departure");

        spaces++;

        System.out.println("free "+ spaces);

        spaceTracker.release();

        mutex.release();
    }            

    @Override
    public void park(){    
        try {
                Thread.sleep((int)(Math.random()*inscale));
            } catch (InterruptedException e) { }
    }
}
