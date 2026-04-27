package lab3.Dining.sol2;

public class Philosopher extends Thread {
    private final int identity;
    private final Fork left;
    private final Fork right;
    private final int scale;
    private final int next;
    private final Waiter waiter;

    public Philosopher(int id, int n, int s, Fork l, Fork r, Waiter waiter) {
        this.identity = id;
        this.next = n;
        this.scale = s;
        this.left = l;
        this.right = r;
        this.waiter = waiter;
    }

    public void run() {
        while (true) {
            //thinking
            System.out.println(" Philosopher "+ identity + " is thinking");
            delay(scale);
            //hungry
            System.out.println(" Philosopher "+ identity+ " is trying to get fork " + identity);
            try{
                waiter.askPermission();

                right.get();
                //gotright chopstick
                System.out.println(" Philosopher "+ identity+ " got fork " + identity + " and is trying to get fork " + next);
                left.get();
                System.out.println(" Philosopher "+ identity + " is eating");
                //eating
                System.out.println(" Philosopher "+ identity + " is releasing left fork " + next);
                //delay(scale);
                left.put();
                System.out.println(" Philosopher "+ identity + " is releasing fork " + identity);
                //delay(scale);
                right.put();

                waiter.releasePermission();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void delay(int scale) {
        try {
            sleep((int)(Math.random()*scale));
        } catch (InterruptedException e) { }
    }
}
