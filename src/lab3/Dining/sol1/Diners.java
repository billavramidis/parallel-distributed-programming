package lab3.Dining.sol1;

public class Diners {

    static final int numphils = 5;
    static final int sleeptime = 1;
    
    
     public static void main(String[] args) {
       Philosopher[] phil= new Philosopher[numphils];
       Fork[] fork = new Fork[numphils];

       for (int i =0; i<numphils; ++i)
            fork[i] = new Fork();

       for (int i =0; i<numphils-1; ++i){
            phil[i] = new Philosopher(i, i+1, sleeptime,
                        fork[i], fork[i+1]);
            phil[i].start();
       }

       phil[numphils-1] = new Philosopher(numphils-1, 0, sleeptime,
               fork[0], fork[numphils-1]);
       phil[numphils-1].start();
    }

}
