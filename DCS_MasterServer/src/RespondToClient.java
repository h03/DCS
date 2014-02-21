import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class RespondToClient {
	
/*
 * Master等待UserClient的询问请求
 * 为新的请求建立连接
 * 并返回计算结果给对应的UserClient
 */
	
	public static void main(String[] args) {
	//	msChangeTargetIPToUC("127.0.1.1");
		msRespondToClient();
	}

	public static void msRespondToClient() {
		try {
			System.out.println("正在连接新的客户端...");
			ServerSocket serverSocket = new ServerSocket(5600);
			Socket connectToClient = null;
			while (true) {
				connectToClient = serverSocket.accept();
				new ReceiveFromClient(connectToClient);
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void msChangeTargetIPToUC(String UClientIP) {
		try {
			System.out.println("正在连接新的客户端...");
			Socket connectToClient = new Socket(UClientIP,5800);
			DataOutputStream toUClient = new DataOutputStream(connectToClient.getOutputStream());
			DataInputStream fromUClient = new DataInputStream(connectToClient.getInputStream());
			String targetIP = null;
			String fromUC = null;
			targetIP = ReceiveFromClient.msFindTargetIP(UClientIP);
			toUClient.writeUTF(targetIP);
			toUClient.flush();
			fromUC = fromUClient.readUTF();
			if(fromUC.equals("OK")) {
				System.out.println("客户端已接收到目标存储节点地址！");
				fromUClient.close();
				toUClient.close();
				connectToClient.close();
			}
			
			
			} catch(IOException e) {
				e.printStackTrace();
			}
	}

}
