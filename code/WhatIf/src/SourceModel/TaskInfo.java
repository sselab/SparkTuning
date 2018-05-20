package SourceModel;

import org.dom4j.Element;

/**  
 * @author wenyanqi 
 * 
 */
public class TaskInfo {

	public long taskid;
	
	public long tasktime;

	
	/*
	 * setup 阶段
	 */
	//第一次序列化的task信息的时间
	public long cDeserilizeTime;
	//第一次序列化的task信息的大小
	public long dSerilizeTaskSize;
	
	//下载依赖文件和jar包的时间
	public long cReadJarFileTime;
	//下载依赖文件和jar包的大小
	public long dReadJarFileSize;
	//本地依赖的文件和jar包大小
	public long dLocalJarFileSize;
	//HTTP或HDFS上依赖的文件和jar包大小
	public long dRemoteJarFileSize;
	
	//第二次序列化task content的大小
	public long cDeserilizeTaskTime;
	//第二次序列化task content的大小
	public long dSerilizeTaskContentSize;
	
	//第三次序列化task中RDD以及相关的依赖信息花费的时间
	public long cDeserilizeRDDTime;
	//第三次序列化task中RDD以及相关的依赖信息大小
	public long dDeserilizeRDDSize;
	
	//setup阶段的总时间
	public long cSetupPhaseTime;
	
	
	/*
	 * read 阶段
	 */
	//read阶段的总时间	
	public long cReadPhaseTime;
	
	//read进来的总记录数
	public long dReadInRecs;
		
	//read进来的总字节数
	public long dReadInBytes;
	
	/*
	 * compute 阶段
	 */
	//compute阶段的总时间
	public long cComputeTime;

	//compute之后的记录数
	public long dComputeOutRecs;
	
	
	/*
	 * send result阶段
	 */
	//发送结果所用的时间
	public long sendResultTime;
	
	//返回结果的大小
	public long dResultsBytes;
	
	//序列化结果的时间
	public long cSerilizeResultTime;
	
	//写入结果的时间 得不到
	//public long cResultWriteTime;
	
	/*
	 * shuffle write阶段
	 */
	public ShuffleWrite shufflewrite = new ShuffleWrite();
	
	/*
	 * shuffle read阶段
	 */
	public ShuffleRead shuffleread = new ShuffleRead();
	

	public void readTaskXML(Element taskelement) {
		this.tasktime = Long.parseLong(taskelement.attributeValue("time"));
		
		this.taskid = Long.parseLong(taskelement.attributeValue("id"));
		
		//setup阶段
		Element setupElement = taskelement.element("setup");
		
		this.cSetupPhaseTime = 
				Long.parseLong(setupElement.element("setUpTime").getText());
		this.dSerilizeTaskSize = 
				Long.parseLong(setupElement.element("dSerilizeTaskSize").getText());
		this.dSerilizeTaskContentSize = 
				Long.parseLong(setupElement.element("dSerilizeTaskContentSize").getText());
		this.cDeserilizeTime = 
				Long.parseLong(setupElement.element("cDeserilizeTime").getText());
		this.cReadJarFileTime = 
				Long.parseLong(setupElement.element("cReadJarFileTime").getText());
		this.cDeserilizeTaskTime = 
				Long.parseLong(setupElement.element("cDeserilizeTaskTime").getText());
		this.cDeserilizeRDDTime = 
				Long.parseLong(setupElement.element("cDeserilizeRDDTime").getText());
		this.dDeserilizeRDDSize = 
				Long.parseLong(setupElement.element("dDeserilizeRDDSize").getText());
		
		//read阶段
		Element readElement = taskelement.element("read");
		
		if(readElement == null){
			this.cReadPhaseTime = 0L;
			this.dReadInBytes = 0L;
			this.dReadInRecs = 0L;
		} else {
			this.cReadPhaseTime = 
					Long.parseLong(readElement.element("readTime").getText());
			this.dReadInBytes = 
					Long.parseLong(readElement.element("dReadInBytes").getText());
			this.dReadInRecs = 
					Long.parseLong(readElement.element("dReadInRecs").getText());
			
		}
		
		//compute阶段
		Element computeElement = taskelement.element("compute");
		
		this.cComputeTime = 
				Long.parseLong(computeElement.element("computeTime").getText());
		this.dComputeOutRecs = 
				Long.parseLong(computeElement.element("dComputeOutRecs").getText());
		
		//shuffle write阶段
		Element shufflewriteElement = taskelement.element("shuffleWrite");
		if(shufflewriteElement != null) {
			
			this.shufflewrite.mapSideCombine = 
					Boolean.parseBoolean(shufflewriteElement.element("mapSideCombine").getText());
			this.shufflewrite.byPassMergeSort = 
					Boolean.parseBoolean(shufflewriteElement.element("byPassMergeSort").getText());
			this.shufflewrite.spillEnabled = 
					Boolean.parseBoolean(shufflewriteElement.element("spillEnabled").getText());
			this.shufflewrite.cShuffleWriteTime = 
					Long.parseLong(shufflewriteElement.element("cShuffleWriteTime").getText());
			this.shufflewrite.cPartitionPhaseTime = 
					Long.parseLong(shufflewriteElement.element("cPartitionPhaseTime").getText());
			this.shufflewrite.cCombPhaseTime = 
					Long.parseLong(shufflewriteElement.element("cCombPhaseTime").getText());
			this.shufflewrite.cSortPhaseTime = 
					Long.parseLong(shufflewriteElement.element("cSortPhaseTime").getText());
			this.shufflewrite.cSpillPhaseTime = 
					Long.parseLong(shufflewriteElement.element("cSpillPhaseTime").getText());
			this.shufflewrite.dnumsSpills = 
					Long.parseLong(shufflewriteElement.element("dnumspills").getText());
			this.shufflewrite.dSpillRecs = 
					Long.parseLong(shufflewriteElement.element("dSpillRecs").getText());
			this.shufflewrite.dPerSpillRecs = 
					Long.parseLong(shufflewriteElement.element("dPerSpillRecs").getText());
			this.shufflewrite.dSpillBufferSize = 
					Long.parseLong(shufflewriteElement.element("dSpillBufferSize").getText());
			this.shufflewrite.dSpillFileSize = 
					Long.parseLong(shufflewriteElement.element("dSpillFileSize").getText());
			this.shufflewrite.cMergeReadTime = 
					Long.parseLong(shufflewriteElement.element("cMergeReadTime").getText());
			this.shufflewrite.cMergeSortTime = 
					Long.parseLong(shufflewriteElement.element("cMergeSortTime").getText());
			this.shufflewrite.cMergeCombTime = 
					Long.parseLong(shufflewriteElement.element("cMergeCombTime").getText());
			this.shufflewrite.cMergeWriteTime = 
					Long.parseLong(shufflewriteElement.element("cMergeWriteTime").getText());
			this.shufflewrite.dCombRecs = 
					Long.parseLong(shufflewriteElement.element("dCombRecs").getText());
			this.shufflewrite.dShuffleWriteRecs = 
					Long.parseLong(shufflewriteElement.element("dShuffleWriteRecs").getText());
			this.shufflewrite.dShuffleWriteBytes = 
					Long.parseLong(shufflewriteElement.element("dShuffleWriteBytes").getText());
			this.shufflewrite.objectSize =
					Long.parseLong(shufflewriteElement.element("objectSize").getText());
		}
		
		//shuffle read阶段
		Element shufflereadElement = taskelement.element("shuffleRead");
		
		if(shufflereadElement != null){
			this.shuffleread.mapSideCombine = 
					Boolean.parseBoolean(shufflereadElement.element("mapSideCombine").getText());
			this.shuffleread.byPassMergeSort = 
					Boolean.parseBoolean(shufflereadElement.element("byPassMergeSort").getText());
			this.shuffleread.spillEnabled = 
					Boolean.parseBoolean(shufflereadElement.element("spillEnabled").getText());
			this.shuffleread.cShufflereadTime = 
					Long.parseLong(shufflereadElement.element("cShuffleReadTime").getText());
			this.shuffleread.cShuffleReadFetchTime = 
					Long.parseLong(shufflereadElement.element("cShuffleFetchTime").getText());
			this.shuffleread.dShuffleReadLocalBytes = 
					Long.parseLong(shufflereadElement.element("dShuffleReadLocalBytes").getText());
			this.shuffleread.dShuffleReadRemoteBytes = 
					Long.parseLong(shufflereadElement.element("dShuffleReadRemoteBytes").getText());
			this.shuffleread.dShuffleReadBytes = this.shuffleread.dShuffleReadLocalBytes
					+ this.shuffleread.dShuffleReadRemoteBytes;
			this.shuffleread.dShuffleReadRecs = 
					Long.parseLong(shufflereadElement.element("dShuffleReadRecs").getText());
			this.shuffleread.cCombPhaseTime = 
					Long.parseLong(shufflereadElement.element("cCombPhaseTime").getText());
			this.shuffleread.cSortPhaseTime = 
					Long.parseLong(shufflereadElement.element("cSortPhaseTime").getText());
			this.shuffleread.cSpillPhaseTime = 
					Long.parseLong(shufflereadElement.element("cSpillPhaseTime").getText());
			this.shuffleread.dnumsSpills = 
					Long.parseLong(shufflereadElement.element("dnumspills").getText());
			this.shuffleread.dSpillRecs = 
					Long.parseLong(shufflereadElement.element("dSpillRecs").getText());
			this.shuffleread.dPerSpillRecs = 
					Long.parseLong(shufflereadElement.element("dPerSpillRecs").getText());
			this.shuffleread.dSpillBufferSize = 
					Long.parseLong(shufflereadElement.element("dSpillBufferSize").getText());
			this.shuffleread.dSpillFileSize = 
					Long.parseLong(shufflereadElement.element("dSpillFileSize").getText());
			this.shuffleread.cMergeReadTime = 
					Long.parseLong(shufflereadElement.element("cMergeReadTime").getText());
			this.shuffleread.cMergeSortTime = 
					Long.parseLong(shufflereadElement.element("cMergeSortTime").getText());
			this.shuffleread.cMergeCombTime = 
					Long.parseLong(shufflereadElement.element("cMergeCombTime").getText());
			this.shuffleread.dCombRecs = 
					Long.parseLong(shufflereadElement.element("dCombRecs").getText());
			this.shuffleread.sort =
					Boolean.parseBoolean(shufflereadElement.element("sort").getText());
			this.shuffleread.cSortTime = 
					Long.parseLong(shufflereadElement.element("cSortTime").getText());
			this.shuffleread.objectSize =
					Long.parseLong(shufflereadElement.element("objectSize").getText());
		}
		
		//send result阶段
		Element sendresultElement = taskelement.element("sendResult");
		
		if(sendresultElement!=null) {
			this.sendResultTime = 
					Long.parseLong(sendresultElement.element("sendResultTime").getText());
			this.dResultsBytes = 
					Long.parseLong(sendresultElement.element("dResultBytes").getText());
			this.cSerilizeResultTime =
					Long.parseLong(sendresultElement.element("cSerilizeResultTime").getText());
			
		}
		
		
	}
}
