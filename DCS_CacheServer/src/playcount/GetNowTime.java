package playcount;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetNowTime {

	/* 
	 * 获取系统现在的时间戳
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String nowTime = getNowTime();
		System.out.println(nowTime);

	}
	
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

}
