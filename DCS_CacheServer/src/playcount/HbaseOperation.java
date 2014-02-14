package playcount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;


public class HbaseOperation {
	
	// 初始化配置
	public static Configuration configuration;
	private static HBaseAdmin hbaseAdmin;
	static {
		configuration = HBaseConfiguration.create();
		configuration.set("hbase.zookeeper.property.clientPort", "2181");
		configuration.set("hbase.zookeeper.quorum", "localhost");
		configuration.set("hbase.master", "localhost:600000");
		System.out.println("连接HBase成功！");
		
	}
	
	
	// 主函数
	public static void main(String[] args) throws IOException {
		 hbaseAdmin = new HBaseAdmin(configuration);
	}
	
	// 创建表
	public static void createTable(String tableName, String[] cfs) throws IOException{
	//	hbaseAdmin = new HBaseAdmin(configuration);
		System.out.println("start create table......");
		if(hbaseAdmin.tableExists(tableName)){
			System.out.println("The tablename does exist!");
		}
		else {
			HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
			for(int i = 0; i<cfs.length; i++) {
				tableDescriptor.addFamily(new HColumnDescriptor(cfs[i]));
			}
			hbaseAdmin.createTable(tableDescriptor);
			System.out.println("表创建成功！");
		}
	}
	
	// 删除表
	public static void deleTable(String tableName) throws IOException {
		if(hbaseAdmin.tableExists(tableName)){
		hbaseAdmin.disableTable(tableName);
		hbaseAdmin.deleteTable(tableName);
		System.out.println("表删除成功！");}
		else {
			System.out.println("The tablename does not exist !");
		}
	}
	
	// 插入一行记录
	public void writeRow(String tableName, String[] cfs) {
		try{
			HTable table = new HTable(configuration,tableName);
			Put put = new Put(Bytes.toBytes("rows1"));
			for(int j=0; j<cfs.length; j++) {
				put.add(Bytes.toBytes(cfs[j]), null, null);
				// put.add(Bytes.toBytes(cfs[j]));
				Bytes.toBytes(String.valueOf(1));
				Bytes.toBytes("value_1");
				table.put(put);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// 删除一行记录
	public void deleteRow(String tableName, String rowkey) throws IOException {
		HTable table = new HTable(configuration,tableName);
		List list = new ArrayList();
		Delete d1 = new Delete(rowkey.getBytes());
		list.add(d1);
		table.delete(list);
		System.out.println("删除行成功！");
	}
	
	// 按row key查找一行记录
	public static void selectByRowKey(String tablename,String rowKey) throws IOException {
		System.out.println("进入查询函数！");
		HTable table = new HTable(configuration,tablename);
		System.out.println("table成功被new...");
		Get g = new Get(Bytes.toBytes(rowKey));
		System.out.println("查询中...");
		Result r =table.get(g);
		System.out.println("获取数据中...");
		for(KeyValue kv: r.raw()){
		System.out.println("column:"+new String(kv.getFamily())+"======value:"+new String(kv.getValue()));
		System.out.println("");
		}
		System.out.println("连接结束！");
		}
	
	// 查询表中所有行
	public void scaner(String tablename) {
	try {
	HTable table =new HTable(configuration, tablename);
	Scan s =new Scan();
	ResultScanner rs = table.getScanner(s);
	for (Result r : rs) {
	KeyValue[] kv = r.raw();
	for (int i =0; i < kv.length; i++) {
	System.out.print(new String(kv[i].getRow()) +"");
	System.out.print(new String(kv[i].getFamily()) +":");
	System.out.print(new String(kv[i].getQualifier()) +"");
	System.out.print(kv[i].getTimestamp() +"");
	System.out.println(new String(kv[i].getValue()));
	}
	}
	} catch (IOException e) {
	e.printStackTrace();
	}
	}

}
