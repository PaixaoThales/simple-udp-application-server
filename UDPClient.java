import java.io.*;
import java.net.*;

class UDPClient {
	public static void main(String args[]) throws Exception {
		//Create datagram socket
		System.out.println("Client start... >");
		InetAddress IPAddress = InetAddress.getByName("localhost");
		//Read a sentence from the console
		try (DatagramSocket clientSocket = new DatagramSocket()) {
			while (true) {
				BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
				String sentence = inFromUser.readLine();
				//Allocate buffers
				byte[] sendData = new byte[sentence.length()];
				byte[] receiveData = new byte[sentence.length()];
				//Get the bytes of the sentence
				sendData = sentence.getBytes();
				//Send packet to the server
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
				clientSocket.send(sendPacket);
				//Get the response from the server
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				//Print the received response
				String modifiedSentence = new String(receivePacket.getData());
				System.out.println("RECEIVED FROM SERVER: " + modifiedSentence);
			}
		}
		//Close the socket
	}
}
