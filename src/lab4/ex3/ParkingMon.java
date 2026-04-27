package lab4.ex3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ParkingMon implements Parking{
	private int spaces;
	private final int waitscale;
	private final int inscale;

    public ParkingMon(int capacity) {
       this.spaces = capacity;
       this.waitscale = 10;
       this.inscale = 5;
    }

    @Override
	public synchronized void arrive() {
    //Car arrival with random delay
        try {
            Thread.sleep((int)(Math.random()*waitscale));
        } catch (InterruptedException e) { }

        while(spaces == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(Thread.currentThread().getName()+" arrival");
        //Car entering
        System.out.println(Thread.currentThread().getName()+"     parking");

        spaces--;

        System.out.println("free "+ spaces);
	}

    @Override
    public synchronized void depart(){
        //Car departure
        System.out.println(Thread.currentThread().getName()+"         departure");

        spaces++;

        notifyAll();

        System.out.println("free "+ spaces);
    }            

    @Override
    public void park(){    
        try {
                Thread.sleep((int)(Math.random()*inscale));
            } catch (InterruptedException e) { }
    }
}
