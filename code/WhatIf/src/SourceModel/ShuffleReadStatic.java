package SourceModel;

/**  
 * @author wenyanqi 
 * 
 */
public class ShuffleReadStatic {
	
	//shuffle read过程中是否开启mapSideCombine
	public boolean mapSideCombine;
	
	//shuffle read过程中 byPassMergeSort
	public boolean byPassMergeSort;
	
	public boolean spillEnabled;
	
	//shuffle read读取的总数据中本地数据的比例
//	public long dsLocalBytesRadio;   //暂时没用到
	
	//Shuffle过程中数据压缩率，即压缩之前和压缩后的数据量比值
//	public long dsShuffleCompressRatio; //暂时没用到
	
	//spill过程中数据压缩率，即压缩之前和压缩后的数据量比值
	//public long dsSpillCompressRatio;
	
	//spill combine前后结果记录的比率
	public double dsSpillCombRecsSel;  //profile可以算出来
	
	//merge的combine前后记录比率
	public double dsMergeCombRecsSel;   //profile可以算出来
	
	
	public double csSpillPerRecsCost; //profile可以算出来

	
	public double csMergeReadPerRecsCost;  //profile可以算出来
	
	
	//TimSort一次花费的时间
	public double csTimSortCost;  //profile可以算出来
		
	//在merge的时候，队列排序花费的时间
	public double csQueueSortCost;  //profile可以算出来
	
	//merge过程中combine的时间
	public double csMergeCombCost;  //profile可以算出来
	
	//在spill过程中combine一条记录的时间
	public double csSpillCombCost;  //profile可以算出来
		
	//shuffle read 一条记录的花费时间
	public double csShuffleReadPerRecsCost;  //profile可以算出来
	
	//shuffle read spill中一条record的内存大小
	public long objectSize;

}
