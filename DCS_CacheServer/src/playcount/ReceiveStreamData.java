package playcount;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveStreamData {
	
	public static void main(String[] args) {
		RedisOperation.RedisCon();
		csReceiveStreamData();
	}

	private static void csReceiveStreamData() {
		try {
			System.out.println("Waiting for user client sending data...");
			ServerSocket serverSocket = new ServerSocket(5900);
			Socket connectToClient = null;
			
			while(true) {
				connectToClient = serverSocket.accept();
				new ReceiveDataThread(connectToClient);
			}
				
			} catch(IOException e) {
				e.printStackTrace();
			}
	
		
	}

}
