package ms;

/*
 * 集群管理信息表更新
 * （1）Cache Server状态表采用Redis Hash数据结构（key，field1，value1，field2，value2，...）：
 *     cacheID，CPU，v1，memory，v2，connectNumber，v3，loadAvg，v4，...
 *     linux下的一些命令：uptime，free -m，vmstat，iostat
 *     此表最初数据来源于cache服务器FeedbackToMaster，本地对应ReFromCacheThread
 *     
 * （2）Cache Server分类排序表（newCache，lowCache，mediumCache，highCache，tooHighcache，noAvailCache）
 *     采用Redis Sorted Set数据结构（key，cache1，score1，cache2，score2，...）：
 *     cacheType，cacheID1，s2，cacheID2，s2，cacheID3，s3，...
 *     根据cache server状态表中的各项数据进行计算，得出服务器的score存入cache server分类排序表
 */


/*
 * 由于协同策略更改，此功能暂时放弃，参见MasterServer类中的注释
 * （3）cache与client映射表采用Redis Set数据结构（key，member1，member2，member3，...），此结构是无序状态：
 *     cacheID，client1，client2，client3，...
 *     每当返回给一个client目标节点，就将节点和client存入此映射表
 */


public class ClusterInfo {
	

}
