package lab9;

import java.net.*;
import java.io.*;

public class ClientProtocol {

	BufferedReader user = new BufferedReader(new InputStreamReader(System.in));

	public ClientMessage prepareRequest() throws IOException {
		System.out.print("Enter message to send to server: ");
		String theOutput = user.readLine();

		int option = 0;

		if (!theOutput.equals("CLOSE")) {
			System.out.print("Enter option to send to server: ");
			String optionLine = user.readLine();
			option = Integer.parseInt(optionLine);
		}

		return new ClientMessage(theOutput, option);
	}

	public void processReply(String theInput) throws IOException {
	
		System.out.println("Message received from server: " + theInput);
	}
}
