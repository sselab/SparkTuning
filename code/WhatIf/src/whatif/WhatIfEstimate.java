package whatif;

import java.io.File;
import java.util.HashMap;
import Simulation.SparkSimulation;
import Simulation.Stage;
import SourceModel.CostStatic;
import SourceModel.DataFlowStatic;
import SourceModel.Parameter;
import SourceModel.StageInfo;

/**  
 * @author wenyanqi 
 * 
 */
public class WhatIfEstimate {

	public void setParamters(HashMap<String, String> paramters) {
		Parameter.pBypassMergeThreshold = Integer.parseInt(paramters.get("spark.shuffle.sort.bypassMergeThreshold"));
		//Parameter.pCompressionCodec = paramters.get("spark.io.compression.codec");
		Parameter.pExecutorCores = Integer.parseInt(paramters.get("spark.executor.cores"));
		//Parameter.pFileTransferTo = Boolean.getBoolean(paramters.get("spark.file.transferTo"));
		Parameter.pMaxSizeInFligh = Integer.parseInt(paramters.get("spark.reducer.maxSizeInFlight"));
		Parameter.pMemoryFraction = Double.parseDouble(paramters.get("spark.shuffle.memoryFraction"));
		Parameter.pMemoryMapThreshold = Long.parseLong(paramters.get("spark.storage.memoryMapThreshold"));
		Parameter.pSafetyFraction = Double.parseDouble(paramters.get("spark.shuffle.safetyFraction"));
		Parameter.pSerializer = paramters.get("spark.shuffle.safetyFraction");
		Parameter.pShuffleCompress = Boolean.getBoolean(paramters.get("spark.shuffle.compresss"));
		Parameter.pShuffleManager = paramters.get("spark.shuffle.manager");
		Parameter.pShuffleSpill = Boolean.getBoolean(paramters.get("spark.shuffle.spill"));
		Parameter.pShuffleSpillCompress = Boolean.getBoolean(paramters.get("spark.shuffle.spill.compress"));
		
	}
	
	public static void whatifcore(Stage[] stageinfos) {
		//ArrayList<Long, Long> stageinfo = new ArrayList<Long, Long>();
		//handleProfile.stageinfos
		
		//sort
//		int[] tasksNum = new int[2];
//		tasksNum[0] = 665;
//		tasksNum[1] = 120;
		
		//bayes
//		int[] tasksNum = new int[handleProfile.stageinfos.size()];
//		tasksNum[0] = 120;
//		tasksNum[1] = 120;
//		tasksNum[2] = 120;
//		tasksNum[3] = 120;
//		tasksNum[4] = 120;
//		tasksNum[5] = 120;
//		tasksNum[6] = 120;
//		tasksNum[7] = 120;
//		tasksNum[8] = 120;
//		tasksNum[9] = 120;
		
		//kmeans
//		int[] tasksNum=new int[26];
//		tasksNum[0]=85;
//		tasksNum[1]=85;
//		tasksNum[2]=85;
//		tasksNum[3]=85;
//		tasksNum[4]=85;
//		tasksNum[5]=85;
//		tasksNum[6]=85;
//		tasksNum[7]=85;
//		tasksNum[8]=85;
//		tasksNum[9]=85;
//		tasksNum[10]=85;
//		tasksNum[11]=85;
//		tasksNum[12]=85;
//		tasksNum[13]=85;
//		tasksNum[14]=120;
//		tasksNum[15]=85;
//		tasksNum[16]=120;
//		tasksNum[17]=85;
//		tasksNum[18]=120;
//		tasksNum[19]=85;
//		tasksNum[20]=120;
//		tasksNum[21]=85;
//		tasksNum[22]=120;
//		tasksNum[23]=85;
//		tasksNum[24]=120;
//		tasksNum[25]=85;
		
		//pagerank
//		int[] tasksNum=new int[6];
//		tasksNum[0] = 120;
//		tasksNum[1] = 120;
//		tasksNum[2] = 120;
//		tasksNum[3] = 120;
//		tasksNum[4] = 120;
//		tasksNum[5] = 120;
		
		//terasort
		int[] tasksNum = new int[3];
		tasksNum[0] = 120;
		tasksNum[1] = 480;
		tasksNum[2] = 120;
		
		//wordcount
//		int[] tasksNum = new int[2];
//		tasksNum[0] = 555;
//		tasksNum[1] = 120;
		
		//sort
//		int[] tasksNum = new int[2];
//		tasksNum[0] = 1166;
//		tasksNum[1] = 120;
		
		long[] firstTasksTime = new long[handleProfile.stageinfos.size()];
		long[] othertasksTime = new long[handleProfile.stageinfos.size()];
		long prevStageShuffleWriteRecs = 0;
		long prevStageShuffleWriteBytes = 0;
		
		for(int i=0;i<handleProfile.stageinfos.size();i++) {
			//每一个stage的信息
			StageInfo stageinfo = handleProfile.stageinfos.get(i);
			
			//计算setup阶段的时间  只有第一个stage需要下载文件数据，其他stage的setup时间，看做与profile的setup时间一样大
			long cSetupPhaseTime;
			if(i==0) {
				
				long cReadJarFileTime = (long)(Parameter.dReadJarFileSize / stageinfo.first_csReadJarFileByteTime);
//				System.out.println("first_csDeserilizeContentTime:"+stageinfo.first_csDeserilizeContentTime);
//				System.out.println("first_csDeserilizeRDDTime:"+stageinfo.first_csDeserilizeRDDTime);
//				System.out.println("first_csDeserilizeTime:"+stageinfo.first_csDeserilizeTime);
//				System.out.println("cReadJarFileTime:"+cReadJarFileTime);
				
				cSetupPhaseTime = (long) (stageinfo.first_csDeserilizeContentTime
										+ stageinfo.first_csDeserilizeRDDTime
										+ stageinfo.first_csDeserilizeTime
										+ cReadJarFileTime);
			} else {
				cSetupPhaseTime = stageinfo.first_otherStageSetupTime;
			}
			
			
			//计算read的时间
			long cReadPhaseTime = 0;
			long dReadInRecs = 0;
			long dReadInBytes = 0;
			long cShuffleReadFetchTime = 0;
			long dShuffleReadFetchRecs = 0;
			long dShuffleReadFetchBytes = 0;
			long dsShuffleReadPairWidth = 0;
			long cShuffleReadCombineTime = 0;
			long dShuffleReadCombineRecs = 0;
			long dSpillBufferSize = 0;
			long dSpillNums = 0;
			long dPerSpillRecs = 0;
			long cShuffleReadSortTime = 0;
			long cShuffleReadSpillTime = 0;
			long cShuffleReadMergeReadTime = 0;
			long cShuffleReadMergeSortTime = 0;
			long cShuffleReadMergeCombineTime = 0;
			long dShuffleReadMergeCombRecs = 0;
			long cShuffleReadPhaseTime = 0;
			
			if(stageinfo.isRead) {
//				System.out.println(stageinfo.stageId);
				Parameter.dInputBytes = DataFlowStatic.dsinputBytesList.get(stageinfo.stageId);
				dReadInBytes = Parameter.dInputBytes / tasksNum[i];
				cReadPhaseTime = dReadInBytes * stageinfo.csHDFSReadCost;
				dReadInRecs = (long) (Parameter.dInputBytes / tasksNum[i] / stageinfo.dsInputPairWidth);
				
//				System.out.println("taskNum:"+tasksNum[i]);
				
				System.out.println("dReadInBytes:"+dReadInBytes+" csHDFSReadCost:"+ stageinfo.csHDFSReadCost);
			}else {
				//shuffle read
				
				if(DataFlowStatic.dsShuffleReadRecList.containsKey(stageinfo.stageId)) {
					prevStageShuffleWriteRecs = DataFlowStatic.dsShuffleReadRecList.get(stageinfo.stageId);
					prevStageShuffleWriteBytes = DataFlowStatic.dsShuffleReadBytesList.get(stageinfo.stageId);
				}
				
				cShuffleReadFetchTime = (long) (stageinfo.shuffleread_s.csShuffleReadPerRecsCost * prevStageShuffleWriteRecs / tasksNum[i]);
				dShuffleReadFetchRecs = prevStageShuffleWriteRecs / tasksNum[i];
				System.out.println("prevStageShuffleWriteBytes:"+prevStageShuffleWriteBytes);
				dShuffleReadFetchBytes = prevStageShuffleWriteBytes / tasksNum[i];
				if(dShuffleReadFetchRecs == 0 ) {
					dsShuffleReadPairWidth = dShuffleReadFetchBytes;
				} else {
					dsShuffleReadPairWidth = dShuffleReadFetchBytes / dShuffleReadFetchRecs;
				}
				
				cShuffleReadCombineTime = (long) (stageinfo.shuffleread_s.csSpillCombCost * dShuffleReadFetchRecs);
				dShuffleReadCombineRecs = (long) (dShuffleReadFetchRecs * stageinfo.shuffleread_s.dsSpillCombRecsSel);
				dSpillBufferSize = (long) (Parameter.executorMemory*1024*1024*1024 / Parameter.pExecutorCores * Parameter.pMemoryFraction * Parameter.pSafetyFraction) ;
				dSpillNums = dShuffleReadCombineRecs * stageinfo.shuffleread_s.objectSize / (dSpillBufferSize);
				dPerSpillRecs = dSpillBufferSize / stageinfo.shuffleread_s.objectSize;
				System.out.println("@@dShuffleReadFetchRecs:"+dShuffleReadFetchRecs);
			
				System.out.println("stageinfo.shuffleread_s.csSpillCombCost:"+stageinfo.shuffleread_s.csSpillCombCost);
				if( dSpillNums == 0 ) {
					cShuffleReadSpillTime = 0;
					cShuffleReadMergeReadTime = 0;
					cShuffleReadMergeSortTime = 0;
					cShuffleReadMergeCombineTime = 0;
					dReadInRecs = dShuffleReadCombineRecs;
					dReadInBytes = dShuffleReadCombineRecs * dsShuffleReadPairWidth;
					cShuffleReadSortTime = (long) (stageinfo.shuffleread_s.csTimSortCost * dShuffleReadCombineRecs * Math.log(dShuffleReadCombineRecs));
				}else{
					cShuffleReadSpillTime = (long) ((Parameter.pShuffleSpill?1:0) * dShuffleReadCombineRecs * stageinfo.shuffleread_s.csSpillPerRecsCost);
					cShuffleReadMergeReadTime = (long) ((Parameter.pShuffleSpill?1:0) * dShuffleReadCombineRecs * stageinfo.shuffleread_s.csMergeReadPerRecsCost);
					cShuffleReadMergeSortTime = (long) ((Parameter.pShuffleSpill?1:0) * dShuffleReadCombineRecs  * stageinfo.shuffleread_s.csQueueSortCost);
					cShuffleReadMergeCombineTime = (long) ((Parameter.pShuffleSpill?1:0) * dShuffleReadCombineRecs * stageinfo.shuffleread_s.csMergeCombCost);
					dShuffleReadMergeCombRecs = (long) (dShuffleReadCombineRecs * stageinfo.shuffleread_s.dsMergeCombRecsSel);
					cShuffleReadSortTime = (long) (dSpillNums * stageinfo.shuffleread_s.csTimSortCost * dPerSpillRecs * Math.log(dPerSpillRecs));
					//System.out.println("stageinfo.shuffleread_s.csTimSortCost"+stageinfo.shuffleread_s.csTimSortCost);
					dReadInRecs = dShuffleReadMergeCombRecs;
					dReadInBytes = dShuffleReadMergeCombRecs * dsShuffleReadPairWidth;
				}
				
				
//				System.out.println("dReadInBytes:"+dReadInBytes);
//				System.out.println("dReadInRecs:"+dReadInRecs);
//				System.out.println("dShuffleReadCombineRecs:"+dShuffleReadCombineRecs);
//				System.out.println("shuffleReadFetchTime:"+cShuffleReadFetchTime);
//				System.out.println("cShuffleReadCombineTime:"+cShuffleReadCombineTime);
//				System.out.println("cShuffleReadSortTime:"+cShuffleReadSortTime);
//				System.out.println("cShuffleReadSpillTime:"+cShuffleReadSpillTime);
//				System.out.println("cShuffleReadMergeReadTime:"+cShuffleReadMergeReadTime);
//				System.out.println("cShuffleReadMergeSortTime:"+cShuffleReadMergeSortTime);
//				System.out.println("cShuffleReadMergeCombineTime:"+cShuffleReadMergeCombineTime);
				cShuffleReadPhaseTime = cShuffleReadFetchTime + cShuffleReadCombineTime + cShuffleReadSortTime + 
						cShuffleReadSpillTime + cShuffleReadMergeReadTime + cShuffleReadMergeSortTime + cShuffleReadMergeCombineTime;
			}
			
			//计算compute的时间
			long cComputePhaseTime = 0;
			long dComputeOutRecs = 0;
			long dComputeOutBytes = 0;
			
			if(stageinfo.stageType) {
				cComputePhaseTime = (long) (dReadInRecs * stageinfo.csComputeRecsCost);
			} else {
				cComputePhaseTime = (long) (dReadInBytes * stageinfo.csComputeBytesCost);
				
			}
		
			dComputeOutRecs = (long) (dReadInRecs * stageinfo.dsComputeRecsSel);
			dComputeOutBytes = (long) (dReadInBytes * stageinfo.dsComputeBytesSel);
			
			//send result的时间  不要了
//			long cSendResultPhaseTime = 0;
//			long cSerResultTime = 0;
//			long cWriteResultTime = 0;
//			if(!stageinfo.stageType) {
//				cSerResultTime = (long) (CostStatic.csSerdeCPUCost * dComputeOutBytes);
//				cWriteResultTime = CostStatic.csWriteResultByteTime * dComputeOutBytes;
//				cSendResultPhaseTime = cSerResultTime + cWriteResultTime;
//			}
	
			//shuffle write的时间
			long cShuffleWritePhaseTime = 0;
			long cShuffleWritePartitionTime = 0;
			long cShuffleWriteCombTime = 0;
			long dShuffleWriteCombineRecs = 0;
			long cShuffleWriteSortTime = 0;
			long cShuffleWriteSpillTime = 0;
			long cShuffleWriteMergeReadTime = 0;
			long cShuffleWriteMergeSortTime = 0;
			long cShuffleWriteMergeCombineTime = 0;
			long dShuffleWriteMergeCombRecs = 0;
			long cShuffleWriteMergeWriteTime = 0;
			
			dSpillBufferSize = 0;
			dSpillNums = 0;
			dPerSpillRecs = 0;
			
			if(stageinfo.isShuffleWrite) {
				
				if(("sort").equals(Parameter.pShuffleManager)) {
					if(stageinfo.shufflewrite_s.mapSideCombine) {
						if(Parameter.pShuffleSpill) {
							cShuffleWritePartitionTime = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.csPartitionCost);
							System.out.println("dComputeOutRecs:"+dComputeOutRecs);
							cShuffleWriteCombTime = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.csSpillCombCost);
							dShuffleWriteCombineRecs = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.dsSpillCombRecsSel);
							dSpillBufferSize = (long) (Parameter.executorMemory*1024*1024*1024 / Parameter.pExecutorCores * Parameter.pMemoryFraction * Parameter.pSafetyFraction);
							
							dPerSpillRecs = dSpillBufferSize / stageinfo.shufflewrite_s.objectSize;
							dSpillNums = dShuffleWriteCombineRecs * stageinfo.shufflewrite_s.objectSize / (dSpillBufferSize);
							
							
							if( dSpillNums == 0 ) {
								cShuffleWriteSpillTime = 0;
								cShuffleWriteMergeReadTime = 0;
								cShuffleWriteMergeSortTime = 0;
								cShuffleWriteSortTime = (long) (stageinfo.shufflewrite_s.csTimSortCost * 
										dShuffleWriteCombineRecs * Math.log(dShuffleWriteCombineRecs));
								cShuffleWriteMergeCombineTime = 0;
								dShuffleWriteMergeCombRecs = dShuffleWriteCombineRecs;
							}else{
								cShuffleWriteSpillTime = (long) ((Parameter.pShuffleSpill?1:0) * dShuffleWriteCombineRecs * stageinfo.shufflewrite_s.csSpillPerRecsCost);
								
								cShuffleWriteMergeReadTime = (long) ((Parameter.pShuffleSpill?1:0) * dShuffleWriteCombineRecs * stageinfo.shufflewrite_s.csMergeReadPerRecsCost);
								cShuffleWriteMergeSortTime = (long) ((Parameter.pShuffleSpill?1:0) * dShuffleWriteCombineRecs  * stageinfo.shufflewrite_s.csQueueSortCost);
								cShuffleWriteMergeCombineTime = (long) ((Parameter.pShuffleSpill?1:0) * dShuffleWriteCombineRecs * stageinfo.shufflewrite_s.csMergeCombCost);
								dShuffleWriteMergeCombRecs = (long) (dShuffleWriteCombineRecs * stageinfo.shufflewrite_s.dsMergeCombRecsSel);
								cShuffleWriteSortTime = (long) (dSpillNums * dPerSpillRecs * Math.log(dPerSpillRecs) * stageinfo.shufflewrite_s.csTimSortCost) ;
								
							}
							cShuffleWriteMergeWriteTime = (long) (dShuffleWriteMergeCombRecs * stageinfo.shufflewrite_s.csMergeWritePerRecsCost);
							
							prevStageShuffleWriteRecs = dShuffleWriteMergeCombRecs * tasksNum[i];
							prevStageShuffleWriteBytes = (long) (dShuffleWriteMergeCombRecs * tasksNum[i] *
									               stageinfo.shufflewrite_s.dsShuffleWriteOutputPairWidth);
							cShuffleWritePhaseTime = cShuffleWritePartitionTime + cShuffleWriteCombTime + 
									cShuffleWriteSortTime + cShuffleWriteSpillTime + cShuffleWriteMergeReadTime +
									cShuffleWriteMergeSortTime + cShuffleWriteMergeCombineTime +
									cShuffleWriteMergeWriteTime;
						} else {
							cShuffleWritePartitionTime = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.csPartitionCost);
							cShuffleWriteCombTime = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.csSpillCombCost);
							dShuffleWriteCombineRecs = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.dsSpillCombRecsSel 
													  * stageinfo.shufflewrite_s.dsMergeCombRecsSel);
							
							cShuffleWriteSortTime = (long) (dShuffleWriteCombineRecs *
												Math.log(dShuffleWriteCombineRecs) * stageinfo.shufflewrite_s.csTimSortCost);
							cShuffleWriteMergeWriteTime = (long) (dShuffleWriteCombineRecs * stageinfo.shufflewrite_s.csMergeWritePerRecsCost);
							
							cShuffleWritePhaseTime = cShuffleWritePartitionTime + cShuffleWriteCombTime +
									cShuffleWriteSortTime + cShuffleWriteMergeWriteTime;
							
							prevStageShuffleWriteRecs = dShuffleWriteCombineRecs * tasksNum[i];
							prevStageShuffleWriteBytes = (long) (dShuffleWriteCombineRecs * tasksNum[i] 
									            * stageinfo.shufflewrite_s.dsShuffleWriteOutputPairWidth);
							
						}
					} else if(Parameter.pBypassMergeThreshold > tasksNum[i]) {
						cShuffleWritePartitionTime = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.csPartitionCost);
						cShuffleWriteSpillTime = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.csSpillPerRecsCost);
						cShuffleWriteMergeWriteTime = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.csMergeWritePerRecsCost);
//						System.out.println("dComputeOutRecs:"+dComputeOutRecs);
//						System.out.println("cShuffleWritePartitionTime:"+cShuffleWritePartitionTime);
//						System.out.println("cShuffleWriteSpillTime:"+cShuffleWriteSpillTime);
//						System.out.println("cShuffleWriteMergeWriteTime:"+cShuffleWriteMergeWriteTime);
						cShuffleWritePhaseTime = cShuffleWritePartitionTime + cShuffleWriteSpillTime + cShuffleWriteMergeWriteTime;
						
						prevStageShuffleWriteRecs = dComputeOutRecs * tasksNum[i];
						prevStageShuffleWriteBytes = (long) (dComputeOutRecs * tasksNum[i]
								                    * stageinfo.shufflewrite_s.dsShuffleWriteOutputPairWidth);
					} else {
						cShuffleWritePartitionTime = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.csPartitionCost);
						dSpillBufferSize = (long) (Parameter.executorMemory*1024*1024*1024 / Parameter.pExecutorCores * Parameter.pMemoryFraction * Parameter.pSafetyFraction);
						
						
						dSpillNums = dComputeOutRecs * stageinfo.shufflewrite_s.objectSize / (dSpillBufferSize);
						
						if(Parameter.pShuffleSpill) {
							if(dSpillNums != 0) {
								dPerSpillRecs = dSpillBufferSize / stageinfo.shufflewrite_s.objectSize;
								cShuffleWriteSortTime = (long) (dSpillNums * dPerSpillRecs * Math.log(dPerSpillRecs) 
								        * stageinfo.shufflewrite_s.csTimSortCost) ;
							} else {
								cShuffleWriteSortTime = (long) (stageinfo.shufflewrite_s.csTimSortCost * 
										dShuffleWriteCombineRecs * Math.log(dShuffleWriteCombineRecs));
							}
							
						}else {
							cShuffleWriteSortTime = (long) (dComputeOutRecs * Math.log(dComputeOutRecs) * 
									         stageinfo.shufflewrite_s.csTimSortCost);
						}
						
						if( dSpillNums == 0 ) {
							cShuffleWriteSpillTime = 0;
							cShuffleWriteMergeReadTime = 0;
						}else{
							cShuffleWriteSpillTime = (long) ((Parameter.pShuffleSpill?1:0) * dComputeOutRecs * stageinfo.shufflewrite_s.csSpillPerRecsCost);
							
							cShuffleWriteMergeReadTime = (long) ((Parameter.pShuffleSpill?1:0) * dComputeOutRecs * stageinfo.shufflewrite_s.csMergeReadPerRecsCost);
							
						}
						cShuffleWriteMergeWriteTime = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.csMergeWritePerRecsCost);
						prevStageShuffleWriteRecs = dComputeOutRecs * tasksNum[i];
						prevStageShuffleWriteBytes = (long) (dComputeOutRecs * tasksNum[i] *
								               stageinfo.shufflewrite_s.dsShuffleWriteOutputPairWidth);
						
						cShuffleWritePhaseTime = cShuffleWritePartitionTime + cShuffleWriteSortTime +
								 cShuffleWriteSpillTime + cShuffleWriteMergeReadTime + cShuffleWriteMergeWriteTime;
					}
				
				} else {
					//hash
					if(stageinfo.shufflewrite_s.mapSideCombine) {
						
						if(Parameter.pShuffleSpill) {
							
							cShuffleWriteCombTime = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.csSpillCombCost);
							dShuffleWriteCombineRecs = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.dsSpillCombRecsSel);
							dSpillBufferSize = (long) (Parameter.executorMemory*1024*1024*1024 / Parameter.pExecutorCores * Parameter.pMemoryFraction * Parameter.pSafetyFraction);
							
							dPerSpillRecs = dSpillBufferSize / stageinfo.shufflewrite_s.objectSize;
							dSpillNums = dShuffleWriteCombineRecs * stageinfo.shufflewrite_s.objectSize / (dSpillBufferSize);
							
							if( dSpillNums == 0 ) {
								cShuffleWriteSpillTime = 0;
								cShuffleWriteMergeReadTime = 0;
								cShuffleWriteMergeSortTime = 0;
								cShuffleWriteMergeCombineTime = 0;
								dShuffleWriteMergeCombRecs = dShuffleWriteCombineRecs;
								cShuffleWriteSortTime = (long) (stageinfo.shufflewrite_s.csTimSortCost * 
										dShuffleWriteCombineRecs * Math.log(dShuffleWriteCombineRecs));
							}else{
								cShuffleWriteSpillTime = (long) ((Parameter.pShuffleSpill?1:0) * dShuffleWriteCombineRecs * stageinfo.shufflewrite_s.csSpillPerRecsCost);
								
								cShuffleWriteMergeReadTime = (long) ((Parameter.pShuffleSpill?1:0) * dShuffleWriteCombineRecs * stageinfo.shufflewrite_s.csMergeReadPerRecsCost);
								cShuffleWriteMergeSortTime = (long) ((Parameter.pShuffleSpill?1:0) * dShuffleWriteCombineRecs  * stageinfo.shufflewrite_s.csQueueSortCost);
								cShuffleWriteMergeCombineTime = (long) ((Parameter.pShuffleSpill?1:0) * dShuffleWriteCombineRecs * stageinfo.shufflewrite_s.csMergeCombCost);
								dShuffleWriteMergeCombRecs = (long) (dShuffleWriteCombineRecs * stageinfo.shufflewrite_s.dsMergeCombRecsSel);
								cShuffleWriteSortTime = (long) (dSpillNums * dPerSpillRecs * Math.log(dPerSpillRecs) * stageinfo.shufflewrite_s.csTimSortCost) ;
								
							}
							cShuffleWritePartitionTime = (long) (dShuffleWriteMergeCombRecs * stageinfo.shufflewrite_s.csPartitionCost);
							cShuffleWriteMergeWriteTime = (long) (dShuffleWriteMergeCombRecs * stageinfo.shufflewrite_s.csMergeWritePerRecsCost);
							prevStageShuffleWriteRecs = dShuffleWriteMergeCombRecs * tasksNum[i];
							prevStageShuffleWriteBytes = (long) (dShuffleWriteMergeCombRecs * tasksNum[i] *
									               stageinfo.shufflewrite_s.dsShuffleWriteOutputPairWidth);
							
							cShuffleWritePhaseTime = cShuffleWritePartitionTime + cShuffleWriteCombTime + 
									cShuffleWriteSortTime + cShuffleWriteSpillTime + cShuffleWriteMergeReadTime +
									cShuffleWriteMergeSortTime + cShuffleWriteMergeCombineTime +
									cShuffleWriteMergeWriteTime;
						} else {
							cShuffleWriteCombTime = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.csSpillCombCost);
							dShuffleWriteCombineRecs = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.dsSpillCombRecsSel * stageinfo.shufflewrite_s.dsMergeCombRecsSel);
							cShuffleWritePartitionTime = (long) (dShuffleWriteCombineRecs *stageinfo.shufflewrite_s.csPartitionCost);
							cShuffleWriteMergeWriteTime = (long) (dShuffleWriteCombineRecs * stageinfo.shufflewrite_s.csMergeWritePerRecsCost);
							
							prevStageShuffleWriteRecs = dShuffleWriteCombineRecs * tasksNum[i];
							prevStageShuffleWriteBytes = (long) (dShuffleWriteCombineRecs * tasksNum[i] *
									               stageinfo.shufflewrite_s.dsShuffleWriteOutputPairWidth);
							
							cShuffleWritePhaseTime = cShuffleWritePartitionTime + cShuffleWriteCombTime + cShuffleWriteMergeWriteTime;
							
						}
					} else {
						cShuffleWritePartitionTime = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.csPartitionCost);
						cShuffleWriteMergeWriteTime = (long) (dComputeOutRecs * stageinfo.shufflewrite_s.csMergeWritePerRecsCost);
						
						prevStageShuffleWriteRecs = dComputeOutRecs * tasksNum[i];
						prevStageShuffleWriteBytes = (long) (dComputeOutRecs * tasksNum[i] *
								               stageinfo.shufflewrite_s.dsShuffleWriteOutputPairWidth);
						
						cShuffleWritePhaseTime = cShuffleWritePartitionTime + cShuffleWriteMergeWriteTime;
					}
					
				}
				System.out.println("prev prevStageShuffleWriteRecs:"+prevStageShuffleWriteRecs);
			}
			
//			System.out.println("cShuffleWritePartitionTime:"+cShuffleWritePartitionTime);
//			System.out.println("cShuffleWriteCombTime:"+cShuffleWriteCombTime);
//			System.out.println("cShuffleWriteSortTime:"+cShuffleWriteSortTime);
//			System.out.println("cShuffleWriteSpillTime:"+cShuffleWriteSpillTime);
//			System.out.println("cShuffleWriteMergeReadTime:"+cShuffleWriteMergeReadTime);
//			System.out.println("cShuffleWriteMergeSortTime:"+cShuffleWriteMergeSortTime);
//			System.out.println("cShuffleWriteMergeCombineTime:"+cShuffleWriteMergeCombineTime);
//			System.out.println("cShuffleWriteMergeWriteTime:"+cShuffleWriteMergeWriteTime);
//			
//			
//			
//			
			System.out.println("stageID:"+stageinfo.stageId+"-------------------");
			System.out.println("cSetupPhaseTime:"+cSetupPhaseTime);
			System.out.println("cReadPhaseTime:"+cReadPhaseTime);
			System.out.println("cShuffleReadPhaseTime:"+cShuffleReadPhaseTime);
			System.out.println("cComputePhaseTime:"+cComputePhaseTime);
			System.out.println("cShuffleWritePhaseTime:"+cShuffleWritePhaseTime);
			System.out.println("-------------------------------------------------");
			long firstTaskTime = cSetupPhaseTime + cReadPhaseTime + cShuffleReadPhaseTime + cComputePhaseTime 
								 + cShuffleWritePhaseTime ;
			long otherTaskTime = firstTaskTime - cSetupPhaseTime;
			
			stageinfos[i].setId(stageinfo.stageId);
			stageinfos[i].setFirstTaskTime(firstTaskTime);
			stageinfos[i].setSecondTaskTime(otherTaskTime);
//			System.out.println(stageinfo.tasknums);
			stageinfos[i].setTaskNums(tasksNum[i]);
		
		}
		
	}

	
	
	public long estimate(HashMap<String, String> paramters) {
		
		//读取配置参数
		setParamters(paramters);
		
		//除了配置的参数，还需要知道应用程序依赖的jarFile的大小，应用程序处理的数据的大小，以及executor的memory
		
		Stage[] stageinfos = new Stage[handleProfile.stageinfos.size()];
		
		//whatif 预估每个stage，task的计算时间
		whatifcore(stageinfos);  
		File driverXML = new File(handleProfile.applicationFile,"driver/DAG.xml");
		int executorNum = 20;
		//陈炜昭调度模拟阶段
		SparkSimulation simulation=new SparkSimulation(executorNum,executorNum*Parameter.pExecutorCores,stageinfos,driverXML);
	   
		return simulation.start();

	}
	
	public static void main(String[] args) {
		
		
		//args[0]应用程序依赖的jarFile的大小,args[1]应用程序 处理的数据的大小，args[2]executor的memory  G为单位
		if(args.length < 5) {
			System.out.println("error usage: jarFile的大小（M）  数据的大小（M）  executor_memory（G） executor的个数   日志的位置");
			return ;
		}

		Parameter.dInputBytes = Long.parseLong(args[1])*1024*1024;
		
		//bayes
//		DataFlowStatic.dsinputBytesList.put(0,(long)(20.0*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(6,(long)(20.0*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(7,(long)(21.1*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(8,(long)(20.9*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(9,(long)(21*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(11,(long)(40.8*1024*1024*1024));
//		DataFlowStatic.dsShuffleReadBytesList.put(3, (long)(1353.6*1024*1024));
//		DataFlowStatic.dsShuffleReadRecList.put(3, (long)108727091);
//		DataFlowStatic.dsShuffleReadBytesList.put(5, (long)(1364.6*1024*1024));
//		DataFlowStatic.dsShuffleReadRecList.put(5, (long)109610442);
		
		//kmeans
//		DataFlowStatic.dsinputBytesList.put(0,(long)(10.1*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(1,(long)(19.8*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(2,(long)(11.3*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(3,(long)(23.3*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(4,(long)(14.6*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(5,(long)(18.4*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(6,(long)(16.3*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(7,(long)(26.2*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(8,(long)(14.9*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(9,(long)(19.7*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(10,(long)(14.7*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(11,(long)(28.3*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(12,(long)(18.7*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(13,(long)(12.6*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(15,(long)(11.2*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(17,(long)(11.2*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(19,(long)(11.2*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(21,(long)(11.2*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(23,(long)(11.2*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(25,(long)(9.9*1024*1024*1024));
		
		//terasort
//		DataFlowStatic.dsinputBytesList.put(0,(long) (14.8*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(1,(long) (59.6*1024*1024*1024));

		
		
		//sort
//		DataFlowStatic.dsinputBytesList.put(0,(long)(70.0*1024*1024*1024));
		
		//pagerank
//		DataFlowStatic.dsinputBytesList.put(0,(long)(2.8*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(2,(long)(11.9*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(3,(long)(10.9*1024*1024*1024));
//		DataFlowStatic.dsinputBytesList.put(4,(long)(10.7*1024*1024*1024));
		
		//wordcount
//		DataFlowStatic.dsinputBytesList.put(0,(long)(69.4*1024*1024*1024));
		
		Parameter.dReadJarFileSize =  Long.parseLong(args[0])*1024*1024 + (long)60.0*1024*1024*1024;
		Parameter.executorMemory = Double.parseDouble(args[2]);//double.parseDouble(args[2]) ;
		int executorNum = Integer.parseInt(args[3]);
		String logPath = args[4];
	
		//task个数
		
		handleProfile.readXML(logPath);
		Stage[] stageinfos = new Stage[handleProfile.stageinfos.size()];
		for(int i=0;i<stageinfos.length;i++) {
			stageinfos[i] = new Stage();
		}
		
		//whatif 预估每个stage，task的计算时间
		whatifcore(stageinfos);  
		//输出各个stage的信息
		for(int i=0;i<stageinfos.length;i++) {
			System.out.println("stageID:"+stageinfos[i].getId()+" firstTaskTime: "+stageinfos[i].getFirstTaskTime() + " secondTaskTime: "+stageinfos[i].getSecondTaskTime());
		}
		File driverXML = new File(handleProfile.applicationFile,"driver/DAG.xml");
		
		//陈炜昭调度模拟阶段
		SparkSimulation simulation=new SparkSimulation(executorNum,executorNum*Parameter.pExecutorCores,stageinfos,driverXML);
		long sumTime = simulation.start();
		System.out.println("任务执行总时间："+sumTime);
		
	}
}
