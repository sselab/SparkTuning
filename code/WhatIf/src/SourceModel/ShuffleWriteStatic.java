package SourceModel;

/**  
 * @author wenyanqi 
 * 
 */
public class ShuffleWriteStatic {
	
	//shufflewrite过程中是否开启mapSideCombine
	public boolean mapSideCombine;
	
	//shufflewrite过程中 byPassMergeSort
	public boolean byPassMergeSort;
	
	public boolean spillEnabled;
	
	//Shuffle过程中数据压缩率，即压缩之前和压缩后的数据量比值
	public long dsShuffleCompressRatio;  //没有用到
	
	//spill过程中数据压缩率，即压缩之前和压缩后的数据量比值
	public long dsSpillCompressRatio;   //没有用到
	
	//spill combine前后结果记录的比率
	public double dsSpillCombRecsSel;  //profile可以算出来
	
	//merge的combine前后记录比率
	public double dsMergeCombRecsSel;  //profile可以算出来
	
	
 	public double csSpillPerRecsCost;  //profile可以算出来
	
 	public double csMergeWritePerRecsCost ;  //profile可以算出来
	public double csMergeReadPerRecsCost;  //profile可以算出来
	
	//TimSort一次花费的时间
	public double csTimSortCost;  //profile可以算出来
	
	//在merge的时候，队列排序花费的时间
	public double csQueueSortCost;  //profile可以算出来
	
	//merge过程中combine的时间
	public double csMergeCombCost;   //profile可以算出来
	
	//对一条记录进行分区的时间
	public double csPartitionCost;   //profile可以算出来
	
	//在spill过程中combine一条记录的时间
	public double csSpillCombCost;   //profile可以算出来
		
	//shuffle write读进来记录的宽度
	public double dsShuffleWriteInputPairWidth;  //profile可以算出来
	
	//shuffle write输出的记录宽度
	public double dsShuffleWriteOutputPairWidth;  //profile可以算出来
	
	//shuffle write spill中一条record的内存大小
	public long objectSize;
}
