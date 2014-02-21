package playcount;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisOperation {
	public static Jedis jedis;
	
	/*
	 * 定义系统在Redis上进行数据操作的基本方法
	 */
	
	// 测试主函数
	public static void main(String[] srgs){
		RedisCon();
	//	jedis = new Jedis("localhost",6379);
		String key = "ello" ;
		String value = "123" ;
		String value1 = "1234" ;
		
	//	redisPutSetLine("ello","123");
	//	System.out.println(redisGetLine(key)+"");
	//	redisDeleOne(key);
	//	redisRpushLine(key,value);
	//	redisRpushLine(key,value1);
	//	redisRpopLine(key);
		redisZaddLine("soset", value, key);
		System.out.println("End");
		
	}
	
	// 连接Redis
	public static void RedisCon() {
		jedis = new Jedis("localhost",6379); 
		System.out.println("Connected to redis !");
	//	JedisPool pool = new JedisPool();
	}
	
	
	// 按key，value插入一个String类型的键值对数据
	public static void redisSetLine(String key , String value) {
		String getResult = null;
		jedis.set(key,value);
	//	getResult = jedis.get(key).toString();
	//	System.out.println("The value is " + getResult);
	}
	
	
	// 获取一个String类型的key-value
	   public static String redisGetLine(String key) {
		String getResult;
		if(jedis.get(key) != null){
			getResult = jedis.get(key).toString();
			return getResult;
		}
		else 
			return null;
	}
	
	
	// 从队列尾端插入一个list成员数据
	public static void redisRpushLine(String key, String member) {
		String getResult = null;
		jedis.rpush(key, member);
		getResult = jedis.lrange(key,0,0).toString();
		System.out.println("The value1 is " + getResult);
	
	}
	
	// 返回 key 对应 list 的长度,key 不存在返回 0,如果 key 对应类型不是 list 返回错误
	public static long redisListLen(String key) {
		if(jedis.type(key).equals("list")){
			return jedis.llen(key);
		}
		else {
			return -1;
		}
	}
	

	
	
	// 从队列头部插入一个list类型键值对数据
	public static void redisLpushLine (String key, String member) {
		String getResult = null;
		jedis.lpush(key, member);
		getResult = jedis.lrange(key,0,0).toString();
		System.out.println("The value1 is " + getResult);
	}
	
	// 从队列尾端删除一个list类型键值对数据
		public static void redisRpopLine(String key) {
			String getResult = null;
			if(jedis.exists(key) && jedis.type(key).equals("list")){
			getResult = jedis.rpop(key);
			System.out.println("The value1 is " + getResult);
			}
			else {
				System.out.println("The key is not exist or the key is not a list !");
			}	
		}
		
		
	// 从队列头部删除一个list类型键值对数据
		public static void redisLpopLine (String key) {
			if(jedis.exists(key) && jedis.type(key).equals("list")){
			String getResult = null;
			getResult = jedis.lpop(key);
			System.out.println("The value1 is " + getResult);
			}
			else {
				System.out.println("The key is not exist or not valid !");
			}
		}
		
		
	// 获取一个List中的所有元素或某一范围内的元素
		public static List<String> redisLrange(String key, long start, long end) {
			if(jedis.exists(key) && jedis.type(key).equals("list")){
			List<String> getList = jedis.lrange(key, start, end);
			return getList;
			}
			else {
				System.out.println("The key is not exist or not valid !");
				return null;
			}			
		}
			
		
		
	// 插入一个set类型键值对数据
	public static void redisSaddLine(String key, String member) {
		jedis.sadd(key, member);
		System.out.println("Insert a set member successfully ! ");
	}
	
	
	// 随机从set中选择一个元素，但不删除
	public static String redisRandMember(String key) {
		String getResult = null;
		if(jedis.exists(key) && jedis.type(key).equals("set")){
			getResult = jedis.srandmember(key);
			System.out.println("The value1 is " + getResult);
			return getResult;
			}
			else {
				System.out.println("The key is not exist or not valid!");
				return null;
			}
		}

	
	
	// 插入一个sorted set类型键值对数据
		public static void redisZaddLine(String key, String score, String member) {
			if(!Double.valueOf(score).isNaN()){
			jedis.zadd(key, Double.parseDouble(score), member);			
			System.out.println("Insert a sorted set member successfully !" );
			}
			else {
				System.out.println("Insert a sorted set member in fail !The score is not valid !" );
			}

		}
		
		
		
		
	// 删除一个sorted set类型键值对数据中的指定元素
		public static void redisZremLine(String key, String member) {
			if(jedis.exists(key) && jedis.type(key).equals("zset")){
				jedis.zrem(key, member);
				System.out.println("Delete a sorted set member successfully !");
				}
				else {
					System.out.println("The key is not exist or not valid !");
				}					
		}
		
		
	// 返回sorted set集合中 score 在给定区间的元素
		public static void redisZrangeByScore(String key, String min, String max ) {
			Set<String> getSet = new HashSet<>();
			if(jedis.exists(key) && jedis.type(key).equals("zset")){
				getSet = jedis.zrangeByScore(key, min, max);
				 for( Iterator it = getSet.iterator(); it.hasNext(); )
				    {             
				         System.out.println("value = "+ it.next().toString());            
				    }
			}
			else {
				System.out.println("The key is not exist or not valid !");
			}
		}
		
		
	// 返回sorted set集合中 排名 在给定区间的元素（按score从小到大排序）
		public static void redisZrange(String key, long start, long end ) {
		//	Set<String> getSet = new HashSet<String>();
			if(jedis.exists(key) && jedis.type(key).equals("zset")){
				 Set<String> getSet = jedis.zrange(key, start, end);
				 for( Iterator it = getSet.iterator(); it.hasNext(); )
				    {             
				         System.out.println("value = "+ it.next().toString());            
				    }
			}
			else {
				System.out.println("The key is not exist or not valid !");
			}					
		}
	
		
	// 返回sorted set中给定元素对应的 score
		public static String redisZscore(String key, String element) {
			String getResult = null;
			if(jedis.exists(key) && jedis.type(key).equals("zset")){
				getResult = jedis.zscore(key, element).toString();
				System.out.println("The score is "+ getResult ); 
				return getResult;
			}
			else {
				System.out.println("The key is not exist or not valid !");
				return null;
			}			
		}
		
	
	//  删除sorted set集合中 score 在给定区间的元素
		public static void redisZremRangeByScore(String key, String min, String max) {
			if(jedis.exists(key) && jedis.type(key).equals("zset")){
				jedis.zremrangeByScore(key, min, max);
				System.out.println("Delete the elements in sorted set successfully !" ); 
			}
			else {
				System.out.println("The key is not exist or not valid !");
			}		
		}
		
	
	// 删除sorted set集合中在给定 排名 区间内的元素
	   public static void redisZremRangeByRank(String key, long start, long end){
			if(jedis.exists(key) && jedis.type(key).equals("zset")){
				jedis.zremrangeByRank(key, start, end);
				System.out.println("Delete the elements in sorted set successfully !" ); 
			}
			else {
				System.out.println("The key is not exist or not valid !");
			}				   
	   }
	   
	 
	// 返回sorted set集合中元素的个数
	   public static long redisZcard(String key){
			if(jedis.exists(key) && jedis.type(key).equals("zset")){
				return jedis.zcard(key);
			}
			else {
				System.out.println("The key is not exist or not valid !");
				return -1;
			}				   
	   }
	   
		
	
	// 插入一个hash类型键值对数据，设置 hash field 为指定值,如果 key 不存在,则先创建
	   public static void redisHsetLine(String key, String field, String value) {
			jedis.hset(key, field, value);
			System.out.println("Insert a hash field successfully ! ");
	   }
	 
	
	// 插入一个hash类型键值对数据，设置多个 hash field 为指定值,如果 key 不存在,则先创建
	   public static void redisHmsetLine(String key, String fields[], String keys[]) {
			
	   }
	
	
	// 获取一个指定的 hash field
	   public static void redisHgetLine(String key, String field) {
		   if(jedis.exists(key) && jedis.type(key).equals("hash")){
			   String getResult = null;
			   getResult = jedis.hget(key, field);
				System.out.println("The hash filed value is : " + getResult ); 
			}
			else {
				System.out.println("The key is not exist or not valid !");
			}	
	   }
	

	
	// 获取多个指定的 hash field
	   public static void redisHmget (String key, String fields[]) {
		   if(jedis.exists(key) && jedis.type(key).equals("hash")){
			//   List<String> getResult = new ArrayList<String>();
			     List<String> getResult = jedis.hmget(key, fields);
				 for( Iterator it = getResult.iterator(); it.hasNext(); )
				    {             
				         System.out.println("field = "+ it.next().toString());            
				    }
			}
			else {
				System.out.println("The key is not exist or not valid !");
			}					
	}
	
	
	// 删除一个指定的hash field数据
	   public static void redisHdel(String key, String field) {
			if(jedis.exists(key) && jedis.type(key).equals("hash")){
				jedis.hdel(key, field);
				System.out.println("Delete a field in hash successfully !" ); 
			}
			else {
				System.out.println("The key is not exist or not valid !");
			}						
	}
	   
	  
		
	// 返回指定 hash 的 field 数量
	   public static long redisHlen(String key) {
			if(jedis.exists(key) && jedis.type(key).equals("hash")){
				Long num;
				num = jedis.hlen(key);
				return num; 
			}
			else {
				System.out.println("The key is not exist or not valid !");
				return -1;
			}		
	}	   
	   
		
    // 返回一个 hash 的所有 field
	   public static Set<String> redisHkeys(String key) {
			if(jedis.exists(key) && jedis.type(key).equals("hash")){
				Set<String> getSet = jedis.hkeys(key);
				return getSet; 
			}
			else {
				System.out.println("The key is not exist or not valid !");
				return null;
			}				
	}	   
	   
	// 返回一个 hash 的所有 value
	   public static List<String> redisHvals(String key) {
			if(jedis.exists(key) && jedis.type(key).equals("hash")){
				List<String> getList = jedis.hvals(key);
				return getList; 
			}
			else {
				System.out.println("The key is not exist or not valid !");
				return null;
			}							
	}	   
	
	// 返回一个 hash 的所有 filed 和 value   
	   public static Map<String,String> redisHgetAll(String key) {
			if(jedis.exists(key) && jedis.type(key).equals("hash")){
				Map<String,String> getMap = jedis.hgetAll(key);
				return getMap; 
			}
			else {
				System.out.println("The key is not exist or not valid !");
				return null;
			}					
	}	   
	

	
	// 删除某一个key
	public static void redisDeleOne(String key) {
		jedis.del(key);  // 任何类型的数据（String、List、Set、Sorted Set、Hash）都用这种方式删除
		System.out.println("Delete a key-value seccessfully ! ");
	}
	
		
	
	// 返回给定 key 的 value 类型
	public static String redisType(String key) {
		return jedis.type(key);
	}
	
	// 返回当前数据库的 key 数量
	public static long redisSizeDB() {
		return jedis.dbSize();
	}
	
	// 为 key 指定过期时间,单位是秒。返回 1 成功,返回 0 表示 key 已经设置过过期时间或者不存在
	public static void redisExpireKey(String key, int seconds) {
		jedis.expire(key, seconds);
		System.out.println("Set a TTL to a key ! ");
	}
	
	// ttl key 返回设置过过期时间的 key 的剩余过期秒数 -1 表示 key 不存在或者没有设置过过期时间
	public static long redisTTL(String key) {
		return jedis.ttl(key);
	}
	
	// select db-index 通过索引选择数据库,默认连接的数据库所有是 0,默认数据库数是 16 个。返回 1 表示成功,0 失败
	public static boolean redisSelectDB(int index) {
		if(index>=0 && index<16){
			jedis.select(index);
			return true;
		}
		else {
			System.out.println("The index can only be int from 0 to 15 !");
			return false;
		}
	}
	
	// move key db-index 将 key 从当前数据库移动到指定数据库。返回 1 成功，返回 0 则 key 不存在,或者已经在指定数据库中
	public static boolean redisMoveKey(String key, int index) {
		if(jedis.move(key, index)==1) return true;
		else return false;
	}
	
	
	// 删除当前数据库的所有key，此方法不会失败。慎用
	public static void redisDeleDB() {
		jedis.flushDB();
		System.out.println("Delete a db seccessfully ! ");
	}
	
	// 删除所有数据库中的所有 key,此方法不会失败。更加慎用
	public static void redisFlushAll() {
		jedis.flushAll();
		System.out.println("Delete all of DBs seccessfully ! ");
	}
	
	

}
