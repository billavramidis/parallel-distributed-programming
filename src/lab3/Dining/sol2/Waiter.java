package lab3.Dining.sol2;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Waiter {
    public final Semaphore permits;

    public Waiter(int numPhils){
        this.permits = new Semaphore(numPhils - 1);
    }

    public void askPermission() throws InterruptedException {
        permits.acquire();
    }

    public void releasePermission(){
        permits.release();
    }
}
