package cs;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableFactory;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.generated.master.table_jsp;

// HBase多线程并行插入数据

public class HbaseBatchPutData {
    static Configuration hbaseConfig = null;
   public static HTablePool pool = null;
   public static HTableFactory factory;   //  @@@@@@
   public static String tableName = "T_TEST_1";

   static{
         //conf = HBaseConfiguration.create();
         Configuration HBASE_CONFIG = new Configuration();
         HBASE_CONFIG.set("hbase.master", "192.168.230.133:60000");
         HBASE_CONFIG.set("hbase.zookeeper.quorum", "192.168.230.133");
        HBASE_CONFIG.set("hbase.zookeeper.property.clientPort", "2181");
         hbaseConfig = HBaseConfiguration.create(HBASE_CONFIG);
         
         pool = new HTablePool(hbaseConfig, 1000); 
         factory = new HTableFactory();
    }
    /*
     * Insert Test single thread
    * */
    public static void SingleThreadInsert()throws IOException
    {
         System.out.println("---------开始SingleThreadInsert测试----------");
        long start = System.currentTimeMillis();
         //HTableInterface table = null;
         HTable table = null;
         table = (HTable)pool.getTable(tableName);
         table.setAutoFlush(false);
         table.setWriteBufferSize(24*1024*1024);
        //构造测试数据
        List<Put> list = new ArrayList<Put>();
        int count = 10000;
        byte[] buffer = new byte[350];
        Random rand = new Random();
        for(int i=0;i<count;i++)
        {
            Put put = new Put(String.format("row %d",i).getBytes());
            rand.nextBytes(buffer);
            put.add("f1".getBytes(), null, buffer);
             //wal=false
            put.setWriteToWAL(false);
            list.add(put);    
            if(i%10000 == 0)
           {
                table.put(list);
                list.clear();    
                table.flushCommits();
            }            
       }
        long stop = System.currentTimeMillis();
        //System.out.println("WAL="+wal+",autoFlush="+autoFlush+",buffer="+writeBuffer+",count="+count);
          
       System.out.println("插入数据："+count+"共耗时："+ (stop - start)*1.0/1000+"s");
        
       System.out.println("---------结束SingleThreadInsert测试----------");
     }
    /*
     * 多线程环境下线程插入函数 
     * 
     * */
    public static void InsertProcess()throws IOException
    {
        long start = System.currentTimeMillis();
        //HTableInterface table = null;
        HTable table = null;
        table = (HTable)pool.getTable(tableName);
        table.setAutoFlush(false);
        table.setWriteBufferSize(24*1024*1024);
        //构造测试数据
        List<Put> list = new ArrayList<Put>();
        int count = 10000;
        byte[] buffer = new byte[256];
        Random rand = new Random();
        for(int i=0;i<count;i++)
        {
           Put put = new Put(String.format("row %d",i).getBytes());
            rand.nextBytes(buffer);
            put.add("f1".getBytes(), null, buffer);
           //wal=false
            put.setWriteToWAL(false);
            list.add(put);    
            if(i%10000 == 0)
            {
                table.put(list);
                list.clear();    
                 table.flushCommits();
            }            
        }
        long stop = System.currentTimeMillis();
        //System.out.println("WAL="+wal+",autoFlush="+autoFlush+",buffer="+writeBuffer+",count="+count);
          
        System.out.println("线程:"+Thread.currentThread().getId()+"插入数据："+count+"共耗时："+ (stop - start)*1.0/1000+"s");
    }
    
   
    /*
     * Mutil thread insert test
     * */
    public static void MultThreadInsert() throws InterruptedException
    {
        System.out.println("---------开始MultThreadInsert测试----------");
        long start = System.currentTimeMillis();
        int threadNumber = 10;
        Thread[] threads=new Thread[threadNumber];
        for(int i=0;i<threads.length;i++)
        {
            threads[i]= new ImportThread();
            threads[i].start();            
        }
        for(int j=0;j< threads.length;j++)
        {
             (threads[j]).join();
        }
        long stop = System.currentTimeMillis();
          
        System.out.println("MultThreadInsert："+threadNumber*10000+"共耗时："+ (stop - start)*1.0/1000+"s");        
        System.out.println("---------结束MultThreadInsert测试----------");
    }    


    public static void main(String[] args)  throws Exception{
        //SingleThreadInsert();        
        MultThreadInsert();
        
        
    }
    
    public static class ImportThread extends Thread{
        public void HandleThread()
        {                        
           //this.TableName = "T_TEST_1";
                   
        }
        public void run(){
            try{
                InsertProcess();            
            }
            catch(IOException e){
                e.printStackTrace();                
            }finally{
                System.gc();
                }
            }            
        }
}

