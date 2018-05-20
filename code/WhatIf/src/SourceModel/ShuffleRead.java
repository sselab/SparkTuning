package SourceModel;

/**  
 * @author wenyanqi 
 * 
 */
public class ShuffleRead {
	
	//shuffle read过程中是否开启mapSideCombine
	public boolean mapSideCombine;
	
	//shuffle read过程中 byPassMergeSort
	public boolean byPassMergeSort;
	
	public boolean spillEnabled;
	
	//shuffle read过程中是否sort
	public boolean sort;
	
	//shuffle read阶段的总时间
	public long cShufflereadTime;
	
	//combine花费的时间
	public long cCombPhaseTime;
	
	//spill过程中sort花费的时间
	public long cSortPhaseTime;
	
	//spill花费的时间
	public long cSpillPhaseTime;
	
	//merge read花费的时间
	public long cMergeReadTime;
	
	//merge sort花费的时间
	public long cMergeSortTime;
	
	//merge combine花费的时间
	public long cMergeCombTime;
	
	//merge read花费的时间
	public long cMergereadTime;
	
	//shuffle read获取数据的时间
	public long cShuffleReadFetchTime;
	
	//shuffle Read中sort花费的时间
	public long cSortTime;
	
	//shuffle read的本地数据字节
	public long dShuffleReadLocalBytes;
	
	//shuffle read的远程数据字节数
	public long dShuffleReadRemoteBytes;
	
	//shuffle read的所有的record数
	public long dShuffleReadRecs;
	
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
	
	//shuffle read的总字节数
	public long dShuffleReadBytes;
	
	//shuffle read中spill过程中一条records所占的内存大小
	public long objectSize;

	
}
