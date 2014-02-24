package ms;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RespondToClient extends Thread{
	
/*
 * Master等待UserClient的询问请求
 * 为新的请求建立连接
 * 并返回计算结果给对应的UserClient
 */
	
	public static void main(String[] args) {
	//	msChangeTargetIPToUC("127.0.1.1");
	//	msRespondToClient();
	//	msPublishTargetIP("localhost");
		msPubTargetIP("localhost");
	}
	
	
	public void run(){
		try {
			msRespondToClient();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void msRespondToClient() throws InterruptedException {
		try {
			System.out.println("Waiting for user client connection...");
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
	
	
	// 向单个user client发送改变目标缓存节点地址的消息命令
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
	
	// 使用redis 发布/订阅 功能 群组发送目标地址改变命令
	public static void msPublishTargetIP(String targetIP) {
		try {
			Socket socket = new Socket("127.0.0.1",6379);
			System.out.println("向Master订阅目标节点相关消息...");
			BufferedReader buf = new BufferedReader (new InputStreamReader(System.in));
			String cmd = buf.readLine() + "\r\n";
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.write(cmd.getBytes());
			out.flush();
					 
			while (true) 
			{	 
				if((cmd = String.valueOf(in.readByte()))!=null)System.out.println(cmd);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	// 另一个使用redis 发布/订阅 功能 群组发送目标地址改变命令  ——————》接收到的消息总是乱序的，暂无解。。。
		public static void msPubTargetIP(String targetIP) {
			  String cmd = "subscribe share\r\n";  
		        SocketChannel client = null;  
		        try {  
		            SocketAddress address = new InetSocketAddress("localhost", 6379);  
		            client = SocketChannel.open(address);  
		          //  client.configureBlocking(false);// 设置为异步   
		            ByteBuffer buffer = ByteBuffer.allocate(1024);  
		            buffer.put(cmd.getBytes());  
		            buffer.clear();  
		            client.write(buffer);  
		            System.out.println(new String(buffer.array()));  
		            Pattern p = Pattern.compile("\r\n");
		  
		            while (true) {  
		                buffer.flip();  
		                int i = client.read(buffer);  
		                if (i > 0) {  
		                    byte[] b = buffer.array(); 
		                    String r = new String(b);
		                    System.out.println(r);  
		                 //   Matcher m = p.matcher(r);
		                    String[] str = p.split(r);
		                    System.out.println("end");

		                 //   break;
		                    continue;  
		                }  
		            }  
		        } catch (Exception e) {  
		            try {  
		                client.close();  
		            } catch (IOException e1) {  
		                e1.printStackTrace();  
		            }  
		            e.printStackTrace();  
		        }  
		}
	

}
