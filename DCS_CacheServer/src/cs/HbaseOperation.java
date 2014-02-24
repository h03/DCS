package cs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

/*
 * HBase数据操作类
 * 定义了系统在HBase上进行数据操作的基本方法
 */

public class HbaseOperation {
	
	// 初始化配置
	static Configuration configuration;
	public static HBaseAdmin hbaseAdmin;
	private static HTable table;
	private static String  tableName;


	static {
		configuration = HBaseConfiguration.create();
		configuration.set("hbase.zookeeper.property.clientPort", "2181");
		configuration.set("hbase.zookeeper.quorum", "localhost");
		configuration.set("hbase.master", "localhost:600000");
		try {
			hbaseAdmin = new HBaseAdmin(configuration);
		} catch (MasterNotRunningException | ZooKeeperConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HbaseOperation(String tableName) throws IOException {
		hbaseAdmin = new HBaseAdmin(configuration);
		table = new HTable(configuration,tableName);
		this.tableName = tableName;
	}
	
	// 主函数
	public static void main(String[] args) throws IOException {
	//	 hbaseAdmin = new HBaseAdmin(configuration);
		 System.out.println("连接HBase成功！");
		 String[] cfs = {"action"};
		 
		 int i;
		 String tableName = "test";
		// String userRowkey = "bob-20140215";	 
		 int key = 1;
		 String columnFamily = "action";
		 String userRowkey = null;
		 List<Put> batchPut = new ArrayList<Put>();

		 String[] column = {"eve1","eve2","eve3","eve4","eve5","eve6","eve7","eve8","eve9"};
		 String[] value = {"hbase03","hbase06","hbase09","hbase12","hbase15","hbase18","hbase21","hbase23","hbase25"};
		 for(i=0; i < column.length; i++ ){
		 userRowkey = String.valueOf(key);
		 key++;
		 Put put = new Put(userRowkey.getBytes());
		 put.add(columnFamily.getBytes(), column[i].getBytes(), value[i].getBytes());
		 batchPut.add(put);
		 }
		 

		// hbaseCreateTable(tableName,cfs );
		// hbaseDeleTable(tableName);
		 new HbaseOperation(tableName);
		// hbaseInsertRow(tableName, userRowkey, columnFamily, column, value);
		// hbaseDeleteRow(tableName,userRowkey);
		// hbaseSelectByRowKey(tableName,userRowkey);
		 hbaseScaner(tableName);
		//   hbaseBatchPut(tableName, batchPut);
	}
	
	// 创建表
	public static void hbaseCreateTable(String[] cfs) throws IOException{
		if(hbaseAdmin.tableExists(tableName)){
			System.out.println("The tablename does exist!");
		}
		else {
			HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
			for(int i = 0; i<cfs.length; i++) {
				tableDescriptor.addFamily(new HColumnDescriptor(cfs[i]));
			}
			hbaseAdmin.createTable(tableDescriptor);
			System.out.println("Create table "+ tableName +"successfully ！");
		}
	}
	
	// 删除表
	public static void hbaseDeleTable(String tableName) throws IOException {
		if(hbaseAdmin.tableExists(tableName)){
		hbaseAdmin.disableTable(tableName);
		hbaseAdmin.deleteTable(tableName);
		System.out.println("Dalete table "+ tableName +" successfully ！");}
		else {
			System.out.println("The table "+ tableName + " does not exist !");
		}
	}
	
	// 插入一行记录
	public static void hbaseInsertRow(String tableName, String userRowkey, String columnFamily, String[] column, String[] value) {
		try{
			if(hbaseAdmin.tableExists(tableName)){
		//	table = new HTable(configuration,tableName);
			Put put = new Put(Bytes.toBytes(userRowkey));
			for(int i=0; i<column.length; i++) {
				put.add(Bytes.toBytes(columnFamily),Bytes.toBytes(String.valueOf(column[i])),Bytes.toBytes(value[i]));
				table.put(put);
				}
			System.out.println("Insert a row successfully !");
			}
			else {
				System.out.println("The table " + tableName + " does not exist !");
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// 批量插入数据，关闭AutoFlush，并设置WriteBufferSize
	public static void hbaseBatchPut(String tableName, List<Put> batchList) {
		try{
			if(hbaseAdmin.tableExists(tableName)){
				 table.setAutoFlush(false);
		         table.setWriteBufferSize(1*1024*1024);
				 table.put(batchList);
				 batchList.clear();    
	             table.flushCommits();
			System.out.println("Insert rows in batch successfully !");
			}
			else {
				System.out.println("The table " + tableName + " does not exist !");
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// 删除一行记录
	public static void hbaseDeleteRow(String tableName, String userRowkey) throws IOException {
		if(hbaseAdmin.tableExists(tableName)){
	//	HTable table = new HTable(configuration,tableName);
		List list = new ArrayList();
		Delete d1 = new Delete(userRowkey.getBytes());
		list.add(d1);
		table.delete(list);
		System.out.println("Delete a row successfully ！");
		}
		else {
			System.out.println("The table " + tableName + " does not exist !");
		}
	}
	
	// 按row key查找一行记录
	public static void hbaseSelectByRowKey(String tableName,String userRowKey) throws IOException {
	//	HTable table = new HTable(configuration,tableName);
		Get g = new Get(Bytes.toBytes(userRowKey));
		Result r =table.get(g);
		for(KeyValue kv: r.raw()){
		System.out.println(userRowKey + " " + new String(kv.getQualifier()) + ": " + new String(kv.getValue()));
		}
		System.out.println("Get the result data successfully ！");
		}
	
	// 查询表中所有行
	public static void hbaseScaner(String tableName) {
	try {
	// HTable table =new HTable(configuration, tableName);
	Scan s =new Scan();
	ResultScanner rs = table.getScanner(s);
	for (Result r : rs) {
	KeyValue[] kv = r.raw();
	for (int i =0; i < kv.length; i++) {
	System.out.print(new String(kv[i].getRow()) +" ");
	System.out.print(new String(kv[i].getFamily()) +":");
	System.out.print(new String(kv[i].getQualifier()) +" ");
	System.out.print(kv[i].getTimestamp() +" ");
	System.out.println(new String(kv[i].getValue()));
	}
	}
	} catch (IOException e) {
	e.printStackTrace();
	}
	}
}
