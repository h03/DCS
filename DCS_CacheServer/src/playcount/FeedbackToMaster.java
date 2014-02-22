package playcount;

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
			toMaster.writeUTF(state);
			toMaster.flush();
			ack = fromMaster.readUTF();
			if(ack.equalsIgnoreCase("OK")){
				System.out.println("已将状态发送给Master服务器！");
				fromMaster.close();
				toMaster.close();
				connectToMaster.close();
			} else if(ack.equals("clients")){
				System.out.println("需要将目前连接的用户表返回给Master！");
				Set<String> clientInfo = RedisOperation.redisSmembers("clientSet");
				Iterator<String> it = clientInfo.iterator();
				int count = clientInfo.size();
				toMaster.writeInt(count);
				while(it.hasNext()){
					toMaster.writeUTF(it.next());
				}
				
			} else if(ack.equals("save")){
				System.out.println("需要完成善后工作！将内存数据全部持久化！");
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	


}
