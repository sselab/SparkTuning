package SourceModel;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.html.HTMLDocument.Iterator;

/**  
 * @author wenyanqi 
 * 
 */
public class StageInfo {
	
	public int stageId;
	public String shuffleManager;  //sort，hash
	public boolean stageType = false; //true shuffleMap false result task
	
	//stage中包含的阶段信息
	public boolean isRead;
	public boolean isShuffleRead;
	public boolean isShuffleWrite;
	public int tasknums;
	public boolean isSendResult;
	
	//该stage中所有的task信息
	public ArrayList<TaskInfo> taskinfos = new ArrayList<TaskInfo>();
	
	//该stage中的static信息
	//输入的一条record的字节宽度
	public double dsInputPairWidth;  //profile可以算出来
	
	//compute对数据的筛选率，即compute之前的数据量除以compute之后的数据量
	public double dsComputeRecsSel;  //profile可以算出来

	//在HDFS上读取一个字节所花费的时间
	public  long csHDFSReadCost; 
		
	//shuffle task的 compute一条记录所花费的时间
	public double csComputeRecsCost;   //profile可以算出来
	
	//result task的compute一个bytes所花费的时间
	public double csComputeBytesCost;  //profile可以算出来
	
	public long dReadInRecs;
	
	//对应func时，处理之前和处理之后数据量的比例
	public double dsComputeBytesSel;   //profile可以算出来
	
	public double first_csDeserilizeTime ;   //profile可以算出来
	public double first_csReadJarFileByteTime ;   //profile可以算出来
	public double first_csDeserilizeContentTime;   //profile可以算出来
	public double first_csDeserilizeRDDTime;  //profile可以算出来
	
	//非第一个stage的setup时间
	public long first_otherStageSetupTime;  //profile可以算出来
	
	public  ShuffleReadStatic shuffleread_s = new ShuffleReadStatic();
	public  ShuffleWriteStatic shufflewrite_s = new ShuffleWriteStatic();
	
	//处理得到的profile信息，算出该stage中的static信息
	public void handleStatic() {
		
		//setup阶段
		TaskInfo  firstTask = new TaskInfo();
		for(int i=taskinfos.size()-1;i>=0;i--) {
			if(taskinfos.get(i).taskid == 0) {
				firstTask = taskinfos.get(i);
			}
		}
		first_csDeserilizeTime = firstTask.cDeserilizeTime;
		first_csReadJarFileByteTime = 1.0*Parameter.dReadJarFileSize / firstTask.cReadJarFileTime;
//		System.out.println("dReadJarFileSize:"+Parameter.dReadJarFileSize +"  cReadJarFileTime:"+taskinfos.get(0).cReadJarFileTime + " first_csReadJarFileByteTime:"+first_csReadJarFileByteTime);
		first_csDeserilizeContentTime = firstTask.cDeserilizeTaskTime;
		first_csDeserilizeRDDTime = firstTask.cDeserilizeRDDTime;
		
		long sumSetupTime = 0L;
		for(int i=taskinfos.size()-1;i>=0;i--) {
			sumSetupTime += taskinfos.get(i).cSetupPhaseTime;
		}
		first_otherStageSetupTime = sumSetupTime / taskinfos.size();
		System.out.println("average setup time:" + sumSetupTime/ taskinfos.size());
		
		//read
		long sumInputBytes = 0L;
		long sumInputRecs = 0L;
		long sumCombineRecs = 0L;
		long sumReadTime = 0;
		if(isRead) {
			for(int i=taskinfos.size()-1;i>=0;i--) {
				sumInputBytes += taskinfos.get(i).dReadInBytes;
				sumInputRecs  += taskinfos.get(i).dReadInRecs;
				sumReadTime   += taskinfos.get(i).cReadPhaseTime;
			}
			
			this.csHDFSReadCost = sumReadTime / sumInputBytes;
			
			tasknums = taskinfos.size();
			
			System.out.println("average read time:" + sumReadTime/ taskinfos.size());
		} else if(isShuffleRead) {
			//应该是shuffleread combine sort之后的字节数
			for(int i=taskinfos.size()-1;i>=0;i--) {
				sumInputBytes  += taskinfos.get(i).shuffleread.dShuffleReadBytes;
				sumInputRecs   += taskinfos.get(i).shuffleread.dShuffleReadRecs;
				sumCombineRecs += taskinfos.get(i).shuffleread.dCombRecs;
			}
			
			tasknums = taskinfos.size();
		}
		
		if(sumInputRecs!=0) {
			dsInputPairWidth = 1.0*sumInputBytes/sumInputRecs;
		} else {
			dsInputPairWidth = sumInputBytes;
		}
		
		if(isRead) {
			dReadInRecs = sumInputRecs;
		}else {
			dReadInRecs = sumCombineRecs;
		}

		//computeAfterRec
		long sumComputeTime = 0;
		long sumComputeRecs = 0L;
		for(int i=taskinfos.size()-1;i>=0;i--) {
			//！！！！！如果是func的时候，dComputeOutRecs得不到
			sumComputeRecs += taskinfos.get(i).dComputeOutRecs;
			sumComputeTime += taskinfos.get(i).cComputeTime;
		}
		System.out.println("average compute time:" + sumComputeTime/taskinfos.size());
		if(sumComputeRecs!=0) {
			csComputeRecsCost = 1.0*sumComputeTime / sumInputRecs;
		}else {
			csComputeBytesCost = 1.0*sumComputeTime / (dReadInRecs*dsInputPairWidth);
		}
		
		dsComputeRecsSel = (double)sumComputeRecs / dReadInRecs;
		
		//shuffle write
		long sumComputeOutRecs = 0;
		long sumCombineTime = 0;
		long sumSpillNums = 0;
		long sumSpillRecsSortTime = 0;
		long sumSpillPhaseTime = 0;
		long sumSpillOutRecs = 0;
		long sumMergeReadTime = 0;
		long sumMergeSortTime = 0;
		long sumMergeCombineTime = 0;
		long sumSpillCombineRecs = 0;
		long sumMergeWriteTime = 0;
		long sumMergeWriteRecs = 0;
		long sumMergeWriteBytes = 0;
		long sumPerSpillRecs = 0;
		long sumSpillBufferSize = 0;
		long sumPartitionTime = 0;
		long sumObjectSize = 0;
		long sumShuffleWriteTime = 0;
		
		double sumCsTimSortTime = 0.0;
	
		if(isShuffleWrite) {
			stageType = true;
			for(int i=taskinfos.size()-1;i>=0;i--) {
				sumShuffleWriteTime += taskinfos.get(i).shufflewrite.cShuffleWriteTime;
				
				
				sumComputeOutRecs    += taskinfos.get(i).dComputeOutRecs;
				sumCombineTime       += taskinfos.get(i).shufflewrite.cCombPhaseTime;
				sumSpillNums         += taskinfos.get(i).shufflewrite.dnumsSpills;
				sumSpillRecsSortTime += taskinfos.get(i).shufflewrite.cSortPhaseTime
										/ (taskinfos.get(i).shufflewrite.dPerSpillRecs * 
											Math.log(taskinfos.get(i).shufflewrite.dPerSpillRecs));
				sumSpillOutRecs      += taskinfos.get(i).shufflewrite.dPerSpillRecs * taskinfos.get(i).shufflewrite.dnumsSpills;
				if(taskinfos.get(i).shufflewrite.dnumsSpills==0){
					sumSpillOutRecs +=taskinfos.get(i).shufflewrite.dSpillRecs;
				}
				sumSpillPhaseTime   += taskinfos.get(i).shufflewrite.cSpillPhaseTime;
				sumMergeReadTime    += taskinfos.get(i).shufflewrite.cMergeReadTime;
				sumSpillCombineRecs += taskinfos.get(i).shufflewrite.dSpillRecs;
				sumCsTimSortTime    += 1.0*taskinfos.get(i).shufflewrite.cSortPhaseTime /
						taskinfos.get(i).shufflewrite.dSpillRecs / Math.log(taskinfos.get(i).shufflewrite.dSpillRecs);
				sumMergeSortTime    += taskinfos.get(i).shufflewrite.cMergeSortTime;
				sumMergeCombineTime += taskinfos.get(i).shufflewrite.cMergeCombTime;
				sumMergeWriteRecs   += taskinfos.get(i).shufflewrite.dShuffleWriteRecs;
				sumMergeWriteBytes  += taskinfos.get(i).shufflewrite.dShuffleWriteBytes;
				sumMergeWriteTime   += taskinfos.get(i).shufflewrite.cMergeWriteTime;
				//sumPartitionRecs += taskinfos.get(i).shufflewrite.d;
				
				sumPartitionTime    += taskinfos.get(i).shufflewrite.cPartitionPhaseTime;
				sumPerSpillRecs     += taskinfos.get(i).shufflewrite.dPerSpillRecs;
				sumSpillBufferSize  += taskinfos.get(i).shufflewrite.dSpillBufferSize;
				sumObjectSize       += taskinfos.get(i).shufflewrite.objectSize;
			}
			
			System.out.println("average shuffle write time:" +sumShuffleWriteTime/ taskinfos.size());
			this.shufflewrite_s.csPartitionCost        = 1.0*sumPartitionTime / sumComputeOutRecs;
			this.shufflewrite_s.csSpillCombCost        = 1.0*sumCombineTime / sumComputeOutRecs;
			this.shufflewrite_s.csSpillPerRecsCost     = 1.0*sumSpillPhaseTime / sumSpillOutRecs;
			this.shufflewrite_s.csMergeReadPerRecsCost = 1.0*sumMergeReadTime / sumSpillOutRecs;
			this.shufflewrite_s.objectSize             = sumObjectSize / taskinfos.size();
			
			if(sumSpillNums!=0){
				this.shufflewrite_s.csTimSortCost  = 1.0*sumSpillRecsSortTime / sumSpillNums;
			} else {
				this.shufflewrite_s.csTimSortCost  = sumCsTimSortTime / taskinfos.size();
			}
			this.shufflewrite_s.csQueueSortCost    = 1.0*sumMergeSortTime / sumSpillCombineRecs;
			this.shufflewrite_s.dsMergeCombRecsSel = (double)1.0*sumMergeWriteRecs / sumSpillCombineRecs;
			
			this.shufflewrite_s.dsSpillCombRecsSel = (double)1.0*sumSpillCombineRecs / sumComputeOutRecs;
			
			this.shufflewrite_s.csMergeCombCost    = 1.0*sumMergeCombineTime / sumSpillCombineRecs;
			this.shufflewrite_s.csMergeWritePerRecsCost = 1.0*sumMergeWriteTime / sumMergeWriteRecs;
			System.out.println("shufflewrite_s.csMergeWritePerRecsCost:"+this.shufflewrite_s.csMergeWritePerRecsCost);
			this.shufflewrite_s.dsShuffleWriteOutputPairWidth = 1.0*sumMergeWriteBytes / sumMergeWriteRecs;
			
			if(sumPerSpillRecs == 0) {
				//不应该出现这种情况在profile信息中
				this.shufflewrite_s.dsShuffleWriteInputPairWidth = 0;
			} else {
				this.shufflewrite_s.dsShuffleWriteInputPairWidth = 1.0*sumSpillBufferSize / sumPerSpillRecs;
			}
			
			if(("hash").equals(this.shuffleManager) && (this.shufflewrite_s.mapSideCombine == true)) {
				this.shufflewrite_s.csPartitionCost = 1.0*sumPartitionTime / sumMergeWriteRecs ;
			} 
		}
		
		//shuffle read
		long sumShuffleReadRecs = 0;
		long sumShuffleReadTime = 0;
		long sumShuffleRead_temp = 0;
		long sumShuffleReadOutRecs = 0;
		double csTimsortCost = 0.0;
		if(isShuffleRead){
			
			sumCombineTime       = 0;
			sumSpillNums         = 0;
			sumSpillRecsSortTime = 0;			
			sumSpillOutRecs      = 0;
			sumSpillPhaseTime    = 0;
			sumMergeReadTime     = 0;
			sumMergeCombineTime  = 0;
			sumSpillCombineRecs  = 0;
			sumMergeSortTime     = 0;
			sumObjectSize        = 0;
			
			for(int i=taskinfos.size()-1;i>=0;i--) {
				sumShuffleRead_temp += taskinfos.get(i).shuffleread.cShufflereadTime;
				sumShuffleReadRecs += taskinfos.get(i).shuffleread.dShuffleReadRecs;
				sumShuffleReadTime += taskinfos.get(i).shuffleread.cShuffleReadFetchTime;
				sumCombineTime     += taskinfos.get(i).shuffleread.cCombPhaseTime;
				sumSpillNums       += taskinfos.get(i).shuffleread.dnumsSpills;
				csTimsortCost      += 1.0*taskinfos.get(i).shuffleread.cSortPhaseTime /
						(taskinfos.get(i).shuffleread.dSpillRecs * Math.log(taskinfos.get(i).shuffleread.dSpillRecs));
				
				///////////////////////////////////////////////
				if(taskinfos.get(i).shuffleread.dPerSpillRecs!=0){
				sumSpillRecsSortTime += 1.0*taskinfos.get(i).shuffleread.cSortPhaseTime
										/ (taskinfos.get(i).shuffleread.dPerSpillRecs * 
											Math.log(taskinfos.get(i).shuffleread.dPerSpillRecs));
				}
				sumSpillOutRecs += taskinfos.get(i).shuffleread.dPerSpillRecs * taskinfos.get(i).shuffleread.dnumsSpills;
				if(taskinfos.get(i).shuffleread.dnumsSpills==0){
					sumSpillOutRecs +=taskinfos.get(i).shuffleread.dSpillRecs;
				}
				sumSpillPhaseTime     += taskinfos.get(i).shuffleread.cSpillPhaseTime;
				sumMergeReadTime      += taskinfos.get(i).shuffleread.cMergeReadTime;
				sumMergeCombineTime   += taskinfos.get(i).shuffleread.cMergeCombTime;
				sumSpillCombineRecs   += taskinfos.get(i).shuffleread.dSpillRecs;
				sumMergeSortTime      += taskinfos.get(i).shuffleread.cMergeSortTime;
				sumShuffleReadOutRecs += taskinfos.get(i).shuffleread.dCombRecs;
				sumObjectSize         += taskinfos.get(i).shuffleread.objectSize;
			}
			
			System.out.println("average shuffle read time:"+sumShuffleRead_temp/taskinfos.size());
			this.shuffleread_s.csShuffleReadPerRecsCost = 1.0*sumShuffleReadTime / sumShuffleReadRecs;
			this.shuffleread_s.csSpillCombCost = 1.0*sumCombineTime / sumShuffleReadRecs;
			System.out.println("sumCombineTime:"+sumCombineTime+" sumShuffleReadRecs:"+sumShuffleReadRecs);
			
			if(sumSpillNums!=0){
				this.shuffleread_s.csTimSortCost = 1.0*sumSpillRecsSortTime / sumSpillNums;
			} else {
				this.shuffleread_s.csTimSortCost = csTimsortCost / taskinfos.size();
			}
			this.shuffleread_s.csSpillPerRecsCost = 1.0*sumSpillPhaseTime / sumSpillOutRecs;
			this.shuffleread_s.csMergeReadPerRecsCost = 1.0*sumMergeReadTime / sumSpillOutRecs;
		
			
			this.shuffleread_s.csQueueSortCost = 1.0*sumMergeSortTime / sumSpillCombineRecs;
			this.shuffleread_s.dsMergeCombRecsSel = 1.0*sumShuffleReadOutRecs / sumSpillCombineRecs;
			this.shuffleread_s.dsSpillCombRecsSel = 1.0*sumSpillCombineRecs / sumShuffleReadRecs;
			this.shuffleread_s.csMergeCombCost = 1.0*sumMergeCombineTime / sumSpillCombineRecs;
			this.shuffleread_s.objectSize  = sumObjectSize / taskinfos.size();
		}
		
		

		//send result
		long sumResultBytes = 0;
		long sumResultSerializeTime=0;
	
		for(int i=taskinfos.size()-1;i>=0;i--) {
			sumResultBytes += taskinfos.get(i).dResultsBytes;
			sumResultSerializeTime += taskinfos.get(i).cSerilizeResultTime;
		}
		System.out.println("average Func time:"+sumResultSerializeTime/ taskinfos.size());
		CostStatic.csSerdeCPUCost = sumResultSerializeTime / sumResultBytes;

		//func的处理
		if(isRead) {
			dsComputeBytesSel = (double)sumResultBytes / sumInputBytes;
		}else{
			//shuffle read
			dsComputeBytesSel = (double)sumResultBytes / (sumShuffleReadOutRecs * dsInputPairWidth);
		}
		
		
		//输出静态变量进行验证
		System.out.println("-------------------static datas--------------------");
		System.out.println("dsInputPairWidth:"+dsInputPairWidth);
		System.out.println("csHDFSReadCost:"+csHDFSReadCost);
		System.out.println("csComputeRecsCost:"+csComputeRecsCost);
		System.out.println("csComputeBytesCost:"+csComputeBytesCost);
		System.out.println("dsComputeRecsSel:"+dsComputeRecsSel);
		System.out.println("shufflewrite_s.csPartitionCost:"+this.shufflewrite_s.csPartitionCost);
		System.out.println("shufflewrite_s.csSpillCombCost:"+this.shufflewrite_s.csSpillCombCost);
		System.out.println("shufflewrite_s.csSpillPerRecsCost:"+this.shufflewrite_s.csSpillPerRecsCost);
		System.out.println("shufflewrite_s.csMergeReadPerRecsCost:"+shufflewrite_s.csMergeReadPerRecsCost);
		System.out.println("shufflewrite_s.objectSize:"+shufflewrite_s.objectSize );
		System.out.println("shufflewrite_s.csTimSortCost:"+shufflewrite_s.csTimSortCost);
		System.out.println("shufflewrite_s.csQueueSortCost:"+shufflewrite_s.csQueueSortCost);
		System.out.println("shufflewrite_s.dsMergeCombRecsSel:"+shufflewrite_s.dsMergeCombRecsSel);
		System.out.println("shufflewrite_s.dsSpillCombRecsSel:"+shufflewrite_s.dsSpillCombRecsSel);
		System.out.println("shufflewrite_s.csMergeCombCost:"+shufflewrite_s.csMergeCombCost);
		System.out.println("shufflewrite_s.csMergeWritePerRecsCost:"+shufflewrite_s.csMergeWritePerRecsCost);
		System.out.println("shufflewrite_s.dsShuffleWriteOutputPairWidth:"+shufflewrite_s.dsShuffleWriteOutputPairWidth);
		System.out.println("shuffleread_s.csShuffleReadPerRecsCost:"+shuffleread_s.csShuffleReadPerRecsCost);
		System.out.println("shuffleread_s.csSpillCombCost:"+shuffleread_s.csSpillCombCost);
		System.out.println("shuffleread_s.csTimSortCost:"+shuffleread_s.csTimSortCost);
		System.out.println("shuffleread_s.csSpillPerRecsCost:"+shuffleread_s.csSpillPerRecsCost);
		System.out.println("shuffleread_s.csMergeReadPerRecsCost:"+shuffleread_s.csMergeReadPerRecsCost);
		System.out.println("shuffleread_s.csQueueSortCost:"+shuffleread_s.csQueueSortCost);
		System.out.println("shuffleread_s.dsMergeCombRecsSel:"+shuffleread_s.dsMergeCombRecsSel);
		System.out.println("shuffleread_s.dsSpillCombRecsSel:"+shuffleread_s.dsSpillCombRecsSel);
		System.out.println("shuffleread_s.csMergeCombCost:"+shuffleread_s.csMergeCombCost);
		System.out.println("shuffleread_s.objectSize:"+shuffleread_s.objectSize);
		
		System.out.println("------------------------------------------------------------");
	}
	
	
}
