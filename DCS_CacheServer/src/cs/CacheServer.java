package cs;

import java.util.Timer;
import java.util.TimerTask;

public class CacheServer {

	/*
	 * CacheServer的主函数入口
	 * 系统整体运行时实现
	 */
	
	/*
	 * (1)待master启动后，随即启动cache server进行初始化，将cache state信息发送给master
	 * (2)待master确认收到状态信息后，等待client连接请求。同时每隔5分钟向master报告自己的状态。
	 * (3)当收到client连接请求时，则新建一个连接线程，负责接收并缓存流式数据。
	 * (4)若某个client连续6分钟都未发送数据，则关闭与该client的连接。
	 * (5)当redis中缓存的流式数据到达一定数量阀值或超过预设的时间限制，则将数据按时序批量存储到HBase中。
	 * (6)当某个cache server请求下线时，则将内存数据库中的数据全部存到HBase中，即可下线。
	 * (7)
	 * (8)
	 */
	public static void main(String[] args) {
		// 连接redis
		RedisOperation cacheRedis = new RedisOperation();
		cacheRedis.RedisCon();
		// 开启侦听user client的线程
		ReceiveStreamData receData = new ReceiveStreamData();
		receData.start();
		System.out.println("开启侦听user client...");
	    final Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			int count = 1;		
			public void run() {
				FeedbackToMaster feedback = new FeedbackToMaster();
				feedback.csFeedbackToMaster("127.0.0.1");
				System.out.println("第 " + count + " 次发送cache状态信息！");
				count++;
				if(count > 10){
					System.out.println("timer canceled.");
					this.cancel();
					timer.cancel();
				}
			}
		};	
		// 设计定时器，100毫秒后启动计时器任务，每隔5000毫秒再启动一次
		long startTime = 100;
		long interval = 5000;
		timer.schedule(timerTask, startTime, interval);  // cache server每隔一定时间像master发送其状态信息
	}
	
	
	



}
