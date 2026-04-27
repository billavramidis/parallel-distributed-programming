package lab4.ex3;

public class CarParkMain{
	public static void main(String[] args) {
	    int capacity = 4;

		/* Για εξοικονόμηση χώρου τα έχω βάλει στο ίδιο αρχείο, για να δοκιμάσετε τις τρεις λύσεις αρκεί
		να βάλετε τις άλλες δύο σε σχόλια. Για να αναγνωρίζονται όλα ως Parking έχω δημιουργήσει κατάλληλο
		interface.
	 	*/

//		ParkingSemMux parking = new ParkingSemMux(capacity); // Solution 1
//		ParkingLockCond parking = new ParkingLockCond(capacity);  // Solution 2
		ParkingMon parking = new ParkingMon(capacity); // Solution 3

		int cars = 20;
		Car[] p = new Car[cars];

		for (int i=0; i<cars; i++) {
			p[i] = new Car(parking);
			p[i].start(); 
		}
		for (int i=0; i<cars; i++) 
			try { 
				 p[i].join(); 
			} catch (InterruptedException e) { }
    }
}
