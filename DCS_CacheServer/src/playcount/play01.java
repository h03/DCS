package playcount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import redis.clients.jedis.Jedis;

public class play01 {

	/*
	 * 一个单独运行的测试程序
	 * 可忽略
	 */
	public static void readByLines(String FileName){
		File file = new File(FileName);
		BufferedReader reader = null;
		Jedis jedis = new Jedis("localhost",6379); 
		try {
			System.out.println("以行为单位读取文件内容：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			
			while (line<3){
				tempString = reader.readLine();
			//	System.out.println("line "+line+": "+ tempString);
				int count = tempString.length();
				int i = 0;
				int key = 0;
				String value = "";
				while(count>0){
					if(tempString.charAt(i)=='|'){
						key++;	
						jedis.set(String.valueOf(key), value);
						jedis.get(String.valueOf(key));
						value = "";
					}
					else{
						value+=tempString.charAt(i);
					}
					i++;
					count--;
				}
				line++;
				System.out.println(key);
			}
			reader.close();
		}catch (IOException e){
			e.printStackTrace();
		}finally {
			if (reader!=null){
				try{
					reader.close();
				}catch(IOException e1){
					
				}
			}
		}
	}
	

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//File file = new File("/home/ello/Documents/play01");
		/* System.out.println("Absolute Path is : "+ file.getAbsolutePath());
		System.out.println("File Path is : "+ file.getPath());
		System.out.println("Dic of disk is: "+ file.getParent());
		System.out.println("Can be read or not :"+ file.canRead());
		System.out.println("Can be writen or not :"+ file.canWrite());
		System.out.println("File length :"+ file.length()); */
		//String fileName = "/home/ello/Documents/play01";
		//readByLines(fileName);
		try {
			Jedis jr = new Jedis("localhost",6379); //redis 服务地址和端口号
			jr.select(1);
			String key = "mKey";
			jr.set(key, "hello,redis!");
			String v = new String(jr.get(key));
			String k2 = "count";
			System.out.println(new String(jr.get(k2)));
			jr.incr(k2);
			System.out.println(new String(jr.get(k2)));
			jr.incr(k2);
			System.out.println(v);
			System.out.println(new String(jr.get(k2)));
			} catch (Exception e) {
			     e.printStackTrace();
			}}
		
		

	}
