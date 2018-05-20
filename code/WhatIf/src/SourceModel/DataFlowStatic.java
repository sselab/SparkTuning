package SourceModel;

import java.util.HashMap;
import java.util.LinkedList;


/**
 *@author wenyanqi
 * 建模阶段用到的所有的dataflow统计信息
 */
public class DataFlowStatic extends Statistics{

	
	//HDFS中一个partition的大小，默认为128M
	public long dsSplitSize;
	
	public static HashMap<Integer, Long> dsinputBytesList = new HashMap<Integer, Long>();
	
	public static HashMap<Integer, Long> dsShuffleReadBytesList = new HashMap<Integer, Long>();
	
	public static HashMap<Integer, Long> dsShuffleReadRecList = new HashMap<Integer, Long>();
	
}
