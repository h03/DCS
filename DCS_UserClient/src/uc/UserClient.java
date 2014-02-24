package uc;


public class UserClient {

	/**
	 * (1)待master和cache都准备就绪后，即可启动。
	 * (2)初次启动后，首先向master询问给自己分配的目标存储节点地址。
	 * (3)收到目标存储节点后，即向cache server发送流式数据。
	 * (4)从第一次向master询问之后，每6分钟和master确认一次目标存储节点。
	 * (5)若目标存储节点正常，则可继续发送数据；若目标节点不正常，则重新请求新的目标存储节点，回到（2）。
	 * (6)
	 */
	public static void main(String[] args) {
		TargetNode target = new TargetNode();
		target.ucTargetNode("localhost");
	}

}
