package lab9;

import java.net.*;
import java.io.*;

public class EchoServerTCP {
	private static final int PORT = 1234;

	public static void main(String[] args) throws IOException {

		ServerSocket connectionSocket = new ServerSocket(PORT);

		while (true) {

			System.out.println("Server is listening to port: " + PORT);
			Socket dataSocket = connectionSocket.accept();
			System.out.println("Received request from " + dataSocket.getInetAddress());

			ServerThread serverThread = new ServerThread(dataSocket);
			serverThread.start();
		}

	}

	public static class ServerThread extends Thread{
		private Socket dataSocket;
		private static final String EXIT = "CLOSE";

		public ServerThread(Socket dataSocket) {
			this.dataSocket = dataSocket;
		}

		@Override
		public void run() {
			try{
				InputStream is = dataSocket.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(is));
				OutputStream os = dataSocket.getOutputStream();
				PrintWriter out = new PrintWriter(os,true);

				String inmsg, outmsg;
				int option;

				inmsg = in.readLine();
				option = Integer.parseInt(in.readLine());

				ServerProtocol app = new ServerProtocol();
				outmsg = app.processRequest(new ClientMessage(inmsg, option));

				while(!outmsg.equals(EXIT)) {
					out.println(outmsg);
					inmsg = in.readLine();
					option = Integer.parseInt(in.readLine());
					outmsg = app.processRequest(new ClientMessage(inmsg, option));
				}
				dataSocket.close();
				System.out.println("Data socket closed");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}			

