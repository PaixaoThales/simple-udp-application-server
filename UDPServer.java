// created on 29/09/2010 at 22:33
import java.net.*;
import java.util.Arrays;

class UDPServer {
	public static void main(String[] args) throws Exception {
		//Create server socket
		DatagramSocket serverSocket = new DatagramSocket(9876);
		//InetAddress.getByName("127.0.0.1")
		InetAddress[] blackList = new InetAddress[]{InetAddress.getByName("127.0.0.1")};

		System.out.println("Server start...");
		while(true) {
			System.out.println("Listen new connection...");
			byte[] receiveData = new byte[1024];
			//block until packet is sent by client
			DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivedPacket);
			//Get the information about the datagram of the client
			InetAddress IPAddress = receivedPacket.getAddress();

			if (Arrays.stream(blackList).anyMatch(inetAddress -> inetAddress.equals(IPAddress))) {
				System.out.printf("Message from address %s blocked by Black List%n", IPAddress.getHostAddress());
				continue;
			}

			int port = receivedPacket.getPort();
			//Get the data of the packet
			String sentence = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
			System.out.printf("RECEIVED FROM CLIENT %s: %s%n", IPAddress.getHostAddress(), sentence);
			//Change the data to capital letters
			String capitalizedSentence = sentence.toUpperCase();
			byte[] sendData = new byte[sentence.length()];
			sendData = capitalizedSentence.getBytes();
			//Send back the response to the client
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
	}
}
