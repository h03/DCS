package playcount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;

/* 
 * 持久化存储类
 * 将Redis中的数据批量存入HBase中
 */

public class PersistStoreData {
	
	public static void main(String[] args) throws IOException {
		RedisOperation.RedisCon();
		int index = 0;
		String tableName = "test";
	//	csPersistStoreData(index,tableName);
		
		csPersistDataInOrder(0,"soset",6,tableName);
	}
	
	// 将指定的一个redis数据库中的数据批量存入hbase中
	public static void csPersistStoreData(int index, String tableName) throws IOException {		
		RedisOperation.jedis.select(index);
		Set keys = RedisOperation.jedis.keys("*");   //列出所有的key，查找特定的key如：redis.keys("foo")   
        Iterator t1=keys.iterator();
        HbaseOperation hbaseOpt = new HbaseOperation(tableName);
        List<Put> batchPut = new ArrayList<Put>();
        String userRowKey = null;
        String columnFamily = "action";
        String column = "try";
        String value = null;
        int i = 0;
        while(t1.hasNext()){   
            Object obj1 = t1.next();
            userRowKey = obj1.toString();
            value = RedisOperation.redisGetLine(obj1.toString());
    		System.out.println(obj1.toString());
    		System.out.println(value);
    			 Put put = new Put(userRowKey.getBytes());
    			 put.add(columnFamily.getBytes(), column.getBytes(), value.getBytes());
    			 batchPut.add(put);                     
            System.out.println(obj1.toString() + ":" + RedisOperation.redisGetLine(obj1.toString())); 
            i++;
            
            if(i%1000 == 0){
            	hbaseOpt.hbaseBatchPut(tableName, batchPut);
            	System.out.println("Insert " + i + " data !");
            	i = 0;
            }
        }   
        if(i!=0){
        hbaseOpt.hbaseBatchPut(tableName, batchPut);
        System.out.println("OK! Insert " + i + " data !");
        }       
	}

	
	// 按keySet中的时序顺序将redis数据库中的一组指定key-value数据批量存入hbase中
	public static void csPersistDataInOrder(int index, String keySet, int number, String tableName) throws IOException {
		RedisOperation.jedis.select(index);
		Set<String> set = RedisOperation.jedis.zrange(keySet, 0, number-1);
		
		HbaseOperation hbaseOpt = new HbaseOperation(tableName);
        List<Put> batchPut = new ArrayList<Put>();
        String userRowKey = null;
        String columnFamily = "action";
        String column = "try";
        String value = null;
        
		for(Iterator<String> it = set.iterator(); it.hasNext(); ) {
			 userRowKey = it.next();
			 value = RedisOperation.redisGetLine(userRowKey);
			 Put put = new Put(userRowKey.getBytes());
			 put.add(columnFamily.getBytes(), column.getBytes(), value.getBytes());
			 batchPut.add(put);    
			 System.out.println(userRowKey + ":" + value); 
		}
		
        hbaseOpt.hbaseBatchPut(tableName, batchPut);
        System.out.println("Insert " + number + " data !");
     
		
	}
	
	

}
