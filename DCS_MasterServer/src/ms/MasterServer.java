package ms;

import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

public class MasterServer {

	/*
	 * MasterServer的主函数入口
	 * 
	 */
	
	/*
	 * 系统整体运行时协同策略：
	 * (1)Master最先启动，等待cache server连接请求。
	 * (2)cache server依次启动，并第一次向master发送状态信息，之后每隔5分钟发送一次state信息（心跳包）。
	 * (3)Master接收cache state信息，同时更新集群信息表。初始化工作完成后，等待user client连接。
	 * (4)user clients启动，并首先向master询问目标存储节点地址。
	 * (5)master接收user client询问请求，并按策略返回给client指定的目标存储节点TargetIP。
	 * (6)user client收到目标存储节点后，保存节点IP，并持续向目标存储节点发送流式数据。同时每隔6分钟向master询问一次
	 * 其所属的目标存储节点是否正常，若正常则继续发送；若不正常或已失效，则重新询问目标存储节点，回到（4）。
	 * (7)cache server接收多个client发送的流式数据，先按key-value缓存在redis中，同时将key-timeStamp存入一个sorted set
	 * 中，当数据达到一定量或超过一定时间阀值时，则按sorted set中的key序将数据批量地存入HBase中，以保证时序数据的顺序性。
	 * 当一个client在6分钟都未向cache server发送数据，则关闭改client与cache server的连接。
	 * (8)master实时地接收cache server发送的状态信息并更新集群信息表，当某个cache server超负荷或失效时，则不会再给其分配
	 * 任务，直到cache server恢复正常或重新加入集群。
	 * 
	 */
	
	public static void main(String[] args) throws InterruptedException {
		// 连接redis
		RedisOperation masterRedis = new RedisOperation();
		masterRedis.RedisCon();
		// 开启侦听cache server的线程
		ReceiveFromCache reFromCache = new ReceiveFromCache();
		reFromCache.start(); 
		System.out.println("等待cache连接...");
		// 开启侦听user client的线程
		RespondToClient respondToClient = new RespondToClient();
		respondToClient.start();
		System.out.println("等待user client连接...");
		

	}

}
