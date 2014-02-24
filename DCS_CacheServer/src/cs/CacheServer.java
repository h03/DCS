package cs;

public class CacheServer {

	/*
	 * CacheServer的主函数入口
	 * 系统整体运行时实现
	 */
	
	/*
	 * (1)待master启动后，随即启动cache server进行初始化，将cache state信息发送给master
	 * (2)待master确认收到状态信息后，等待client连接请求。同时每隔5分钟向master报告自己的状态。
	 * (3)当收到client连接请求时，则新建一个连接线程，负责接收并存储流式数据。
	 * (4)若某个client连续6分钟都未发送数据，则关闭与该client的连接。
	 * (5)
	 * (6)
	 * (7)
	 * (8)
	 */
	public static void main(String[] args) {
		FeedbackToMaster feedback = new FeedbackToMaster();
		feedback.csFeedbackToMaster("127.0.0.1");

	}

}
