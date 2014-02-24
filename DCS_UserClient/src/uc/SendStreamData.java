package uc;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;

/*
 * UserClient读取文本信息
 * 并将数据发送给对应的CacheServer
 * 对应CacheServer中的ReceiveDataThread和ReceiveStreamData
 */


public class SendStreamData {

	
	public static void main(String[] args) {
		String fileName = "/home/ello/Documents/play01";
		ucSendStreamData("127.0.0.1","ello",fileName);

	}
	
	public static void ucSendStreamData(String TargetIP, String UserID, String FileName) {
		
		File file = new File(FileName);
		BufferedReader reader = null;
		
		try {
			
			Socket connectToCache = new Socket(TargetIP,5900);
			DataOutputStream toCache = new DataOutputStream(connectToCache.getOutputStream());
			System.out.println("Starting send streaming data to cache server...");
			String tempData = UserID;
			String signal = "NewStart";
			toCache.writeUTF(signal);
			toCache.flush();
			toCache.writeUTF(tempData);
			toCache.flush();
			reader = new BufferedReader(new FileReader(file));
			int count = 1;
			 while(count < 30) {
				 if((tempData=reader.readLine())!=null) {
					 toCache.writeUTF(tempData);
					 toCache.flush();
					 System.out.println("Data was sent out. Count = " + count);
					 count++;
				 } else {
					 System.out.println("There is no data to send !");
					 
					 break;
				 }
			 }
			 toCache.writeUTF("EndInput");
			 toCache.flush();
			 toCache.close();
			 connectToCache.close();
			 
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
