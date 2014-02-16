import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * UserClient持续侦听Master的目标存储节点改变的消息
 */

public class ReceiveFromMaster {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			System.out.println("等待master消息...");
			ServerSocket serverSocket = new ServerSocket(5800);
			// 这里是否循环侦听
			Socket connectToMaster = serverSocket.accept();
			System.out.println("连接请求来自：" + connectToMaster.getInetAddress().getHostAddress());
			DataInputStream fromMaster = new DataInputStream(connectToMaster.getInputStream());
			DataOutputStream toMaster = new DataOutputStream(connectToMaster.getOutputStream());
			String targetIP;
			String ack = "OK";
			targetIP = fromMaster.readUTF();
			System.out.println("本机目标存储节点地址为：" + targetIP);
			toMaster.writeUTF(ack);
			toMaster.flush();
			fromMaster.close();
			toMaster.close();
			serverSocket.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
 
	}

}
