package playcount;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ReceiveDataThread extends Thread {
	private Socket connectToClient;
	private DataInputStream fromUClient;
	
	public ReceiveDataThread(Socket socket) throws IOException {
		super();
		connectToClient = socket;
		fromUClient = new DataInputStream(connectToClient.getInputStream());
		start();
		
	}
	
	public void run() {
		try {
			String strData;
			boolean flag = true;
			String userID = null;
			String userKey = null;
			String temp = null;
			String timeStamp = null;
			while(flag) {
				strData = fromUClient.readUTF();
				if(!strData.equals("EndInput") && !strData.equals("NewStart")){
			//		System.out.println(strData);
					timeStamp = GetNowTime.getNowTime();
					userKey = userID + ":" + timeStamp;
					System.out.println("UserKey is " +  userKey);
					RedisOperation.redisPutLine(userKey,strData);
					System.out.println("Put successfully !");
					temp = RedisOperation.redisGetLine(userKey).toString();
					System.out.println(temp);
				} 
				else if (strData.equals("NewStart")){
					userID = fromUClient.readUTF();
				//	System.out.println("The userID is " + userID);
				}
				else {
					System.out.println("EndInput!");
					flag = false;
				}
			}
			fromUClient.close();
		//	connectToClient.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
