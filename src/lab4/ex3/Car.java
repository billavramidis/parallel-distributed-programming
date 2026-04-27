package lab4.ex3;

public class Car extends Thread {
	private final Parking parking;

	
	public Car (Parking parking) {
		this.parking = parking;
	}

	public void run() {
		for (int i = 0; i < 100; i++) {
			parking.arrive();
			parking.park();
			parking.depart();
		}        
	}

}
