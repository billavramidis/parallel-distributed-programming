package lab9;

public class ServerProtocol {

	public String processRequest(ClientMessage clientMsg) {
		String theInput = clientMsg.getMessage();
		System.out.println("Received message from client: " + theInput);
		String theOutput = theInput;


		int option = clientMsg.getOption();

		switch(option){
			case 1:
				theOutput = toLowerCase(theInput);
				break;
			case 2:
				theOutput = toUpperCase(theInput);
				break;
			case 3:
				theOutput = ceasarCipher(theInput);
				break;
			case 4:
				theOutput = ceasarDecipher(theInput);
				break;
			case 5:
				theOutput = calculate(theInput);
				break;
			default: theOutput = theInput;
		}

		System.out.println("Send message to client: " + theOutput);

		return theOutput;
	}

	private String toLowerCase(String theInput){
		return theInput.toLowerCase();
	}

	private String toUpperCase(String theInput){
		return theInput.toUpperCase();
	}

	private String ceasarCipher(String theInput){
		String lowerCase = theInput.toLowerCase();
		StringBuilder result = new StringBuilder();
		for (char character : lowerCase.toCharArray()) {
			if (character != ' ') {
				int originalAlphabetPosition = character - 'a';
				int newAlphabetPosition = (originalAlphabetPosition + 3) % 26;
				char newCharacter = (char) ('a' + newAlphabetPosition);
				result.append(newCharacter);
			} else {
				result.append(character);
			}
		}
		return result.toString();
	}

	//Generated through AI.
	private String ceasarDecipher(String theInput) {
		int decryptShift = 26 - 3;
		StringBuilder result = new StringBuilder();

		for (char character : theInput.toLowerCase().toCharArray()) {
			if (character >= 'a' && character <= 'z') {
				int originalAlphabetPosition = character - 'a';
				int newAlphabetPosition = (originalAlphabetPosition + decryptShift) % 26;
				char newCharacter = (char) ('a' + newAlphabetPosition);
				result.append(newCharacter);
			} else {
				result.append(character);
			}
		}
		return result.toString();
	}

	private String calculate(String theInput) {
		String[] splitInput = theInput.split(" ");

		double firstNumber = Double.parseDouble(splitInput[0]);
		String operator = splitInput[1].toLowerCase();
		double secondNumber = Double.parseDouble(splitInput[2]);

		String result;

		switch (operator) {
			case  "+":
				result = String.valueOf(firstNumber + secondNumber);
				break;
            case  "-":
				result = String.valueOf(firstNumber - secondNumber);
				break;
			case  "*":
				result = String.valueOf(firstNumber * secondNumber);
				break;
			case  "/":
				if (secondNumber == 0) {
					result = "Cannot divide by zero";
				}else{
					result =  String.valueOf(firstNumber / secondNumber);
				}
				break;
			default:
				result = "Invalid Input";
		}

		return result;
		/*
			Δεν συμπεριλαμβάνω την περίπτωση "!" καθώς ο ήδη υπάρχων κώδικας έχει μηχανισμό τερματισμού
			και το θεώρησα πλεονασμό. Εαν ο χρήστης δώσει την επιλογή 5 τότε εκτελείται η πράξη και του
			επιστρέφεται ως συμβολοσειρά.
		 */
	}
}