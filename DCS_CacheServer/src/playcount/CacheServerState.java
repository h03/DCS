package playcount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;


/* 
 * 获取系统现在的时间戳
 * 获取cache服务器的各项运行状态
 */

public class CacheServerState {
	
	/*
	 * 标明服务器是否为新加入服务器或请求下线，
	 * 新加入：first；原有的：worked；请求下线：offline
	 */
	
	public static String howCome = "worked";  
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	//	String nowTime = getNowTime();
	//	System.out.println(nowTime);
		getSysLoadAvg();
		csCheckState();

	}
	
	// 获取系统时间戳
	public static String getNowTime() {
		//Date nowTime = new Date(System.currentTimeMillis());
		 // SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd");
		 // String retStrFormatNowDate = sdFormatter.format(nowTime);
		long ct = System.currentTimeMillis();
		String nt = String.valueOf(ct);
		//String now = String.valueOf(System.currentTimeMillis());
		 // return retStrFormatNowDate;
		return nt;
		
	}
	
	/*
	 * 检查本机的运行状态，并返回状态信息的计算结果。
	 * 0表示新加入服务器；1表示低负载；2表示中负载；3表示高负载；4表示超高负载；5表示请求下线服务器
	 * 获取系统各项运行状态信息［0］cpu平均负载，［1］内存使用率，［2］缺省值默认为0
	 * 一般 CPU 利用率小于75%，load 小于CPU核数，内存利用率小于80%，即可。
	 */
	public static String csCheckState() {
		String type = null;
		float c,m;
		int core = 2;

		c = getSysLoadAvg()/core;
		m = getSysMemUse();
		
		if(howCome.equals("first")) type = "0";
		else if(c < 0.25 && m < 0.25) type = "1";
		else if(c < 0.50 && m < 0.50) type = "2";
		else if(c < 0.75 && m < 0.80) type = "3";
		else if(c < 1 && m < 1) type = "4";
		else if (howCome.equals("offline")) type = "5";
		System.out.println(type);

		return type;
	}
	
	
	// 获取系统的内存使用率
	public static float getSysMemUse() {
		float ma,mb;
		float m;

		Runtime run = Runtime.getRuntime();
		ma = run.freeMemory();
		mb = run.totalMemory();
		String ms = String.format("%.3f", (mb-ma)/mb);
		m = Float.valueOf(ms);	
		return m;
	}
		
	/*
	 *  获取当前linux系统的平均负载，
	 *  Load Average 就是一段时间 (1 分钟、5分钟、15分钟) 内平均 Load。
	 *  
	 */
	
	public static float getSysLoadAvg()  {
	        OperatingSystemMXBean osmxb = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();

	        float systemLoadAvg = (float)osmxb.getSystemLoadAverage();

	        return systemLoadAvg;
		}
	
	// 获取当前系统的版本信息
	public static String getSysInfo()  {
       
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        String sysName = osmxb.getName();
        String sysVersion = osmxb.getVersion();
        String sysArch = osmxb.getArch();       
        String str = "sysName: " + sysName +  "\nsysVersion: " + sysVersion + "\nsysArch: " + sysArch;
        System.out.println(str);
        
        String name = System.getProperty("os.name");
        String version = System.getProperty("os.version");
        String arch = System.getProperty("os.arch");
        System.out.println(name);
        System.out.println(version);
        System.out.println(arch);
        return null;
              
	}
	
	// 执行linux系统的命令,并获取结果
	public static String execLinuxCommand() throws IOException {
        
        Process ob =Runtime.getRuntime().exec("free -m");
      //Process ob =Runtime.getRuntime().exec("uptime");

        InputStream input = ob.getInputStream();       
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String str = "";
        
        while((str = br.readLine())!=null) System.out.println(str);;

        return null;
	}


	
}
