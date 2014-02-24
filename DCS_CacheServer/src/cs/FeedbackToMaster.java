package cs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;


/* CacheServer向Master发送本地服务器的运行状态
 * 端口号：5700
 * 对应Master程序中的ReceiveFromCache
 */

public class FeedbackToMaster {
	
public static void main(String[] srgs) {
	csFeedbackToMaster("127.0.0.1");
	}

    // 获取本机IP地址
	public static String csGetLocalIP() {
		try {
		InetAddress ind = InetAddress.getLocalHost();
		System.out.println("本机IP地址为：" + ind.getHostAddress().toString());
		return ind.getHostAddress().toString();
		} catch(IOException e) {
			System.out.println("获取本地IP地址发生异常！");
			return null;
		}	
	}
	
	
	// 缓存节点将自己的状态发送给Master
	public static void csFeedbackToMaster(String MasterIP) {
		try {
			String localIP = null;
			localIP = csGetLocalIP();
			String state = null;
			String ack = null;
			Socket connectToMaster = new Socket(localIP,5700);
			DataOutputStream toMaster = new DataOutputStream(connectToMaster.getOutputStream());
			DataInputStream fromMaster = new DataInputStream(connectToMaster.getInputStream());
			state = CacheServerState.csCheckState();
			toMaster.writeUTF(state);  // 发送cache server的状态信息
			toMaster.writeUTF(localIP);  // 发送cache server的本地IP地址
			System.out.println("两条均已发送");
			toMaster.flush();

			ack = fromMaster.readUTF();
			
			if(ack.equalsIgnoreCase("OK")){
				System.out.println("已将状态发送给Master服务器！");

			} else if(ack.equals("yes")){
				System.out.println("已将高负载信息更新至cache集群表中！");
				
		    // 将set的key发送给master	
		    // toMaster.writeUTF(localIP);  	
			// 无需保存cache和clients的对应信息表，所以取消以下功能	
			//	Set<String> clientInfo = RedisOperation.redisSmembers(localIP);
			//	Iterator<String> it = clientInfo.iterator();
			//	int count = clientInfo.size();
			//	toMaster.writeInt(count); // 将set的长度发送给master
			//	while(it.hasNext()){
			//		toMaster.writeUTF(it.next());
			//	}
				
			} else if(ack.equals("wait")) {
				System.out.println("cache服务器将不再接收新的请求，直到负载降级！");
				
			} else if(ack.equals("save")){
				System.out.println("需要完成善后工作！将内存数据全部持久化！");
			}	
			
			fromMaster.close();
			toMaster.close();
			connectToMaster.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	

}
