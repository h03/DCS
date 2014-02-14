package playcount;

import redis.clients.jedis.Jedis;

public class RedisOperation {
	public static Jedis jedis;
	
	
	
	// 测试主函数
	public static void main(String[] srgs){
		RedisCon();
	//	jedis = new Jedis("localhost",6379);
		redisPutLine("ello","123");
		System.out.println("End");
		
	}
	
	// 连接Redis
	public static void RedisCon() {
		jedis = new Jedis("localhost",6379); 
		System.out.println("Connected to redis !");
	}
	
	// 按key，value插入一个键值对数据
	public static void redisPutLine(String key , String value) {
		String getResult;
		jedis.set(key,value);
	//	getResult = jedis.get(key).toString();
	//	System.out.println("The value is " + getResult);
	}
	
	// 按key查找value
	public static String redisGetLine(String key) {
		String getResult;
		getResult = jedis.get(key).toString();
		return getResult;
	}

}
