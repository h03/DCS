

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


/*
 * UserClient向Master询问目标存储节点地址
 * 端口号为：5600
 * 与MasterServer中的RespondToClient对应
 */

public class TargetNode {

	private static Socket connectToMaster;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ucTargetNode("localhost");

	}
	
	// 获取本机IP地址
	public static String ucGetLocalIP() {
		try {
		InetAddress ind = InetAddress.getLocalHost();
		System.out.println("本机IP地址为：" + ind.getHostAddress().toString());
		return ind.getHostAddress().toString();
		} catch(IOException e) {
			System.out.println("获取本地IP地址发生异常！");
			return null;
		}	
	}
	
	// 给Master发送获取目标存储节点的请求
	public static void ucTargetNode(String MasterIP) {
		String localIP = ucGetLocalIP();
		try {
			connectToMaster = new Socket(localIP,5600);
			DataInputStream fromMaster = new DataInputStream(connectToMaster.getInputStream());
			DataOutputStream outToMaster = new DataOutputStream(connectToMaster.getOutputStream());
			outToMaster.writeUTF(localIP);
			outToMaster.flush();
			System.out.println("已向Master发送查询目标存储节点请求！");
			System.out.println("连接请求来自：" + connectToMaster.getInetAddress().getHostAddress());
			String targetIP = null;
			targetIP = fromMaster.readUTF();
			System.out.println("本机目标存储节点地址为：" + targetIP);
			fromMaster.close();
			outToMaster.close();
			connectToMaster.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	

}
