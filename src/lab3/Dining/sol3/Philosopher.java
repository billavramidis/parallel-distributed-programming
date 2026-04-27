package lab3.Dining.sol3;

public class Philosopher extends Thread {
  private int identity;
  private Fork left;
  private Fork right;
  private int scale;
  private int next;

  public Philosopher(int id, int n, int s, Fork l, Fork r) {
    	this.identity = id;
        this.next = n;
        this.scale = s;
        this.left = l;
        this.right = r;
  }

  public void run() {
     while (true) {
        //thinking
		System.out.println(" Philosopher "+ identity + " is thinking");
        delay(scale);
        //hungry
       	System.out.println(" Philosopher "+ identity+ " is trying to get fork " + identity);
        if (right.tryGet()){
            //gotright chopstick
            System.out.println(" Philosopher "+ identity+ " got fork " + identity + " and is trying to get fork " + next);
            if (left.tryGet()){
                System.out.println(" Philosopher "+ identity + " is eating");
                //eating
                System.out.println(" Philosopher "+ identity + " is releasing left fork " + next);
                //delay(scale);
                left.put();
                System.out.println(" Philosopher "+ identity + " is releasing fork " + identity);
                //delay(scale);
                right.put();
            }
        }

     }    
  }

  public void delay(int scale) {
	try {
		 sleep((int)(Math.random()*scale));
	} catch (InterruptedException e) { }
  }
}
