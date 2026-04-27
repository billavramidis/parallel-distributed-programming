package lab4.ex1;

public class ProducerConsumer {
	public static void main(String[] args) {
		int noIterations = 20;
		int producerDelay = 1;
		int consumerDelay = 100;

	/* Για εξοικονόμηση χώρου τα έχω βάλει στο ίδιο αρχείο, για να δοκιμάσετε τις τρεις λύσεις αρκεί
		να βάλετε τις άλλες δύο σε σχόλια. Για να αναγνωρίζονται όλα ως Buffer έχω δημιουργήσει κατάλληλο
		interface.
	 */


//		BufferSemMux buff = new BufferSemMux(); // Solution 1
		BufferLockCond buff = new BufferLockCond(); // Solution 2
//		BufferSyncCond buff = new BufferSyncCond(); // Solution 3

		Producer prod = new Producer(buff, noIterations, producerDelay);
		Consumer cons = new Consumer(buff, consumerDelay);

		prod.start();
		cons.start();
	}
}
