package SourceModel;

/**    
 * @author wenyanqi  
 * @category 建模过程中各个阶段中用到的cost统计信息
 *  3-17
 */

public class CostStatic {

//	//序列化和反序列化一个字节花费的时间
//	public long csSerdeBytesCost;
//	
//	//从本地硬盘中读取一个字节的时间
//	public long csLocalIOReadCost;
//	
//	//往硬盘中写一个字节的时间
//	public long csLocalIOWriteCost;
//	
//	//在网络上传输一个字节所花费的时间
//	public long csNetworkCost;
		
	

//	
//	//TimSort一次花费的时间
//	public long csTimSortCost;
//	
//	//shuffle 过程中压缩一个字节的时间
//	public long csShuffleCompressCost;
//	
//	//shuffle 过程中解缩一个字节的时间
//	public long csShuffleUnCompressCost;
//	
//	//spill过程中压缩一个字节的时间
//	public long csSpillCompressCost;
//	
//	//spill过程中压缩一个字节的时间
//	public long csSpillUnCompressCost;
//	
//	//在merge的时候，队列排序花费的时间
//	public long csQueueSortCost;
//	
//	//merge过程中combine的时间
//	public long csMergeCombCost;
//	
//	//对一条记录进行分区的时间
//	public long csPartitionCost;
//	
//	//在spill过程中combine一条记录的时间
//	public long csSpillCombCost;
	
	//序列化需要的CPU
	public static double csSerdeCPUCost;   //需要提前算出来
	
	public static long csWriteResultByteTime; //需要提前算出来
	//计算方法：在监控数据中加输出信息，然后算一下是多少

}
