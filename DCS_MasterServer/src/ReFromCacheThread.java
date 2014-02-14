import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


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
	
	public void run() {
		try {
			String ack = "OK";
			String cacheState = null;
			cacheState = fromCache.readUTF();
			if(cacheState.equals("1")) {
				System.out.println("The cache server is OK !");
				toCache.writeUTF(ack);
				toCache.flush();
			} else {
				System.out.println("The cache server is not OK !");
			}
		} catch(IOException e){
			e.printStackTrace();
		}
	}

}
