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
		// 开启定时向master发送状态信息的任务
	    final Timer timer1 = new Timer();
		TimerTask timerTask1 = new TimerTask() {
			int count = 1;		
			public void run() {
				FeedbackToMaster feedback = new FeedbackToMaster();
				feedback.csFeedbackToMaster("127.0.0.1");
				System.out.println("第 " + count + " 次发送cache状态信息！");
				count++;
				if(count > 10){
					System.out.println("timer canceled.");
					this.cancel();
					timer1.cancel();
				}
			}
		};	
		// 设计定时器，100毫秒后启动计时器任务，每隔5000毫秒再启动一次
		long startTime1 = 100;
		long interval1 = 5000;
		timer1.schedule(timerTask1, startTime1, interval1);  // cache server每隔一定时间向master发送其状态信息

	// 开启定时检查redis数据库中的数据量，一旦到达指定阀值则向hbase批量写入一次数据
    final Timer timer2 = new Timer();
	TimerTask timerTask2 = new TimerTask() {
		int count = 1;		
		PersistStoreData persistData = new PersistStoreData();
		public void run() {
			
			persistData.run();
			System.out.println("第 " + count + " 次检查redis数据库中的kugou数据量！");
			count++;
			if(count > 10){
				System.out.println("timer canceled.");
				this.cancel();
				timer2.cancel();
			}
		}
	};	
	// 设计定时器，100毫秒后启动计时器任务，每隔5000毫秒再启动一次
	long startTime2 = 1000;
	long interval2 = 60000;
	timer2.schedule(timerTask2, startTime2, interval2);  // cache server每隔一定时间向master发送其状态信息
	
}
	



}
