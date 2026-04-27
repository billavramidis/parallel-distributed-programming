package lab4.ex3;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ParkingLockCond implements Parking{
	private int spaces;
	private final int waitscale;
	private final int inscale;
    private final Lock lock;
    private final Condition capacityIndicator;

    public ParkingLockCond(int capacity) {
       this.spaces = capacity;
       this.waitscale = 10;
       this.inscale = 5;
       this.lock = new ReentrantLock();
       this.capacityIndicator = this.lock.newCondition();
    }

    @Override
	public void arrive() {
		//Car arrival with random delay
        lock.lock();
        try{
            try {
                Thread.sleep((int)(Math.random()*waitscale));
            } catch (InterruptedException e) { }

            while(spaces == 0){
                try {
                    capacityIndicator.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println(Thread.currentThread().getName()+" arrival");
            //Car entering
            System.out.println(Thread.currentThread().getName()+"     parking");

            spaces--;

            System.out.println("free "+ spaces);
        }finally {
            lock.unlock();
        }
	}

    @Override
    public void depart(){
        lock.lock();

        try{
            //Car departure
            System.out.println(Thread.currentThread().getName()+"         departure");

            spaces++;

            capacityIndicator.signalAll();

            System.out.println("free "+ spaces);
        }finally {
            lock.unlock();
        }

    }            

    @Override
    public void park(){    
        try {
                Thread.sleep((int)(Math.random()*inscale));
            } catch (InterruptedException e) { }
    }
}
