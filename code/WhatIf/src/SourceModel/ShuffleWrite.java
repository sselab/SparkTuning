package SourceModel;

/**  
 * @author wenyanqi 
 * 
 */
public class ShuffleWrite {
	
	//shufflewrite过程中是否开启mapSideCombine
	public boolean mapSideCombine;
	
	//shufflewrite过程中 byPassMergeSort
	public boolean byPassMergeSort;
	
	public boolean spillEnabled;
	
	//shuffle write阶段的总时间
	public long cShuffleWriteTime;
	
	//partition花费的时间
	public long cPartitionPhaseTime;
	
	//combine花费的时间
	public long cCombPhaseTime;
	
	//sort花费的时间
	public long cSortPhaseTime;
	
	//spill花费的时间
	public long cSpillPhaseTime;
	
	//merge read花费的时间
	public long cMergeReadTime;
	
	//merge sort花费的时间
	public long cMergeSortTime;
	
	//merge combine花费的时间
	public long cMergeCombTime;
	
	//merge write花费的时间
	public long cMergeWriteTime;
	
	
	//spill的次数
	public long dnumsSpills;
	
	//spill之后的记录数
	public long dSpillRecs;
	
	//每次spill出去的记录数
	public long dPerSpillRecs;
	
	//spill的buffer的大小
	public long dSpillBufferSize;
	
	//spill出去的文件大小
	public long dSpillFileSize;
	
	//combine之后的记录数
	public long dCombRecs;
	
	//shuffle write的总记录数
	public long dShuffleWriteRecs;
	
	//shuffle write的总字节数
	public long dShuffleWriteBytes;
	
	//shuffle write中spill过程中一条records所占的内存大小
	public long objectSize;

}
