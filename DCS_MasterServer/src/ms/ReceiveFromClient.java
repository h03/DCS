package ms;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/*
 * Master接收UserClient发来的目标存储节点询问请求
 * 线程由RespondToClient创建并启动
 */

public class ReceiveFromClient extends Thread {
	private Socket connectToClient;
	private DataInputStream fromUClient;
	private DataOutputStream toUClient;
	
	// 构造方法：为每个套接字连接输入和输出流
	public ReceiveFromClient(Socket socket) throws IOException {
		super();
		connectToClient = socket;
		fromUClient = new DataInputStream(connectToClient.getInputStream());
		toUClient = new DataOutputStream(connectToClient.getOutputStream());
		start();    // 启动run()方法
	}
	
	
	public static String msFindTargetIP(String ClientIP) {
		return "192.168.0.103";
	}
	
	
	// 在run()方法中与客户端通信
	public void run() {
		try {
			String targetIP;
			String clientIP = fromUClient.readUTF();
			targetIP = msFindTargetIP(clientIP);
			toUClient.writeUTF(targetIP);
			toUClient.flush();
			System.out.println("已返回地址为"+ clientIP +"的目标存储节点！");
			
			fromUClient.close();
			toUClient.close();
			connectToClient.close();
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	

}
