import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/*
 * Master接收CacheServer发送的状态信息
 * 线程由ReceiveFromCache创建并启动
 */

public class ReFromCacheThread extends Thread {
	private Socket connectToCache;
	private DataInputStream fromCache;
	private DataOutputStream toCache;
	
	public ReFromCacheThread(Socket socket) throws IOException {
		super();
		connectToCache = socket;
		fromCache = new DataInputStream(connectToCache.getInputStream());
		toCache = new DataOutputStream(connectToCache.getOutputStream());
		start();
	}
	
	/*
	 * 0表示新加入服务器；1表示低负载；2表示中负载；3表示高负载；4表示超高负载；5表示请求下线服务器
	 */
	
	public void run() {
		try {
			String ack1 = "OK";
			String ack3 = "clients";
			String ack4 = "wait";
			String ack5 = "save";
			String cacheState = null;
			int count;

			cacheState = fromCache.readUTF();
			if(cacheState.equals("0") || cacheState.equals("1") || cacheState.equals("2")) {
				System.out.println("The cache server is OK !");
				toCache.writeUTF(ack1);
				toCache.flush();
			} else if(cacheState.equals("3")) {
				System.out.println("The cache server is not OK !");
				toCache.writeUTF(ack3);
				toCache.flush();
				String key = null;
				key = fromCache.readUTF();
				count = fromCache.readInt();   // 将cache server发来的当前连接的客户端信息存入redis的clientSet中
				String[] clients = new String[count];
				for(int i=0; i < count; i++){
					clients[i] = fromCache.readUTF();
					count--;
				}
				RedisOperation.redisSaddGroup(key, clients);
				
			} else if(cacheState.equals("4")){
				System.out.println("The cache server is so bad !");
				toCache.writeUTF(ack4);
				toCache.flush();
			} else if(cacheState.equals("5")){
				System.out.println("The cache server is going to be off !");
				toCache.writeUTF(ack5);
				toCache.flush();
				
			}
			fromCache.close();
			toCache.close();
			connectToCache.close();
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}

}
