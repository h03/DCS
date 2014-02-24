package ms;
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
			String ack0= "OK";
			String ack3 = "yes";
			String ack4 = "wait";
			String ack5 = "save";
			String cacheState = null;
			String cacheIP = null;
			String lastType = null;
			
			cacheState = fromCache.readUTF();
			System.out.println(cacheState);	
			cacheIP = fromCache.readUTF();
			System.out.println(cacheIP);
			
			if(RedisOperation.redisExists(cacheIP))
			lastType = RedisOperation.redisGetLine(cacheIP);
			
			if(cacheState.equals("0")) {
				System.out.println("The cache server is new !");	

				if( lastType == null ){
					RedisOperation.redisSetLine( cacheIP,"newCache"); // 保存为string类型的key-value供client询问
					RedisOperation.redisRpushLine("newCache", cacheIP);// 保存在newcache的list中供出队服务
				} else if (lastType.equals("newCache")){
					
				} else {
					RedisOperation.redisSetLine(cacheIP,"newCache"); 
					RedisOperation.redisRpushLine("newCache", cacheIP);
					RedisOperation.redisLrem(lastType, 1, cacheIP);
				}

				toCache.writeUTF(ack0);
				toCache.flush();
				
			} else if (cacheState.equals("1")){
				System.out.println("The cache server load is low !");	

				if( lastType == null ){
					RedisOperation.redisSetLine( cacheIP,"lowCache"); // 保存为string类型的key-value供client询问
					RedisOperation.redisRpushLine("lowCache", cacheIP);// 保存在newcache的list中供出队服务
				} else if (lastType.equals("lowCache")){
					
				} else {
					RedisOperation.redisSetLine(cacheIP,"lowCache"); 
					RedisOperation.redisRpushLine("lowCache", cacheIP);
					RedisOperation.redisLrem(lastType, 1, cacheIP);
				}
				
				toCache.writeUTF(ack0);
				toCache.flush();
				
			} else if (cacheState.equals("2")){
				System.out.println("The cache server load is medium !");	
				
				if( lastType == null ){
					RedisOperation.redisSetLine( cacheIP,"mediumCache"); // 保存为string类型的key-value供client询问
					RedisOperation.redisRpushLine("mediumCache", cacheIP);// 保存在newcache的list中供出队服务
				} else if (lastType.equals("mediumCache")){
					
				} else {
					RedisOperation.redisSetLine(cacheIP,"mediumCache"); 
					RedisOperation.redisRpushLine("mediumCache", cacheIP);
					RedisOperation.redisLrem(lastType, 1, cacheIP);
				}
				
				toCache.writeUTF(ack0);			
				toCache.flush();
				
			} else if(cacheState.equals("3")) {
				System.out.println("The cache server is not OK !");

				if( lastType == null ){
					RedisOperation.redisSetLine( cacheIP,"highCache"); // 保存为string类型的key-value供client询问
					RedisOperation.redisRpushLine("highCache", cacheIP);// 保存在newcache的list中供出队服务
				} else if (lastType.equals("highCache")){
					
				} else {
					RedisOperation.redisSetLine(cacheIP,"highCache"); 
					RedisOperation.redisRpushLine("highCache", cacheIP);
					RedisOperation.redisLrem(lastType, 1, cacheIP);
				}
				
				
				toCache.writeUTF(ack3);
				toCache.flush();

				
		// 由于策略改变，无需保存cache和client的对应信息表，所以以下功能被取消。
		//		String key = fromCache.readUTF();
		//		count = fromCache.readInt();   // 将cache server发来的当前连接的客户端信息存入redis的clientSet中
		//		String[] clients = new String[count];
		//		for(int i=0; i < count; i++){
		//			clients[i] = fromCache.readUTF();
		//			count--;
		//		}
		//		RedisOperation.redisSaddGroup(key, clients);
				
			} else if(cacheState.equals("4")){
				System.out.println("The cache server is so bad !");

				if( lastType == null ){
					RedisOperation.redisSetLine( cacheIP,"highCache"); // 保存为string类型的key-value供client询问
					RedisOperation.redisRpushLine("highCache", cacheIP);// 保存在newcache的list中供出队服务
				} else if (lastType.equals("highCache")){
					
				} else {
					RedisOperation.redisSetLine(cacheIP,"highCache"); 
					RedisOperation.redisRpushLine("highCache", cacheIP);
					RedisOperation.redisLrem(lastType, 1, cacheIP);
				}
				RedisOperation.redisLpushLine("overCache", cacheIP);
				toCache.writeUTF(ack4);
				toCache.flush();
				
			} else if(cacheState.equals("5")){
				System.out.println("The cache server is going to be off !");

				if( lastType == null ){
					RedisOperation.redisSetLine( cacheIP,"notAvailCache"); // 保存为string类型的key-value供client询问
					RedisOperation.redisRpushLine("notAvailCache", cacheIP);// 保存在newcache的list中供出队服务
				} else if (lastType.equals("notAvailCache")){
					
				} else {
					RedisOperation.redisSetLine(cacheIP,"notAvailCache"); 
					RedisOperation.redisRpushLine("notAvailCache", cacheIP);
					RedisOperation.redisLrem(lastType, 1, cacheIP);
				}

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
