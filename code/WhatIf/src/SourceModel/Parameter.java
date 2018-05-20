package SourceModel;

public class Parameter {
	
	//spark.reducer.maxSizeInFlight,默认48M，以M为单位
	public static int pMaxSizeInFligh = 48;  //目前还不能用
	
	//spark.shuffle.spill 默认为true
	public static boolean pShuffleSpill = true;  //可以用
	
	//spark.shuffle.compress 默认为true
	public static boolean pShuffleCompress = true;  //不可以
	
	//spark.shuffle.spill.compress 默认为true
	public static boolean pShuffleSpillCompress = true;   //不可以，或者改变了这个需要重新profile
	
	//spark.shuffle.sort.bypassMergeThreshold 默认为200
	public static int pBypassMergeThreshold = 666;   //可以  相当于其实只有两种效果，一个比taskNum大，一个是比taskNum小
	
	//Spark.io.compression.codec 默认值为snappy   不可变
	public static String pCompressionCodec = "snappy";   //假设固定压缩方式
	
	//spark.shuffle.manager 默认值为sort
	public static String pShuffleManager = "sort";  
	
	//spark.shuffle.memoryFraction 默认为0.2
	public static double pMemoryFraction = 0.1;  //0.1-0.9
	
	//spark.shuffle.safetyFraction 默认为0.8
	public static double pSafetyFraction = 0.5;  //0.1-0.9
	
	//spark.serializer 默认为java序列化方式    不可变
	public static String pSerializer = "java";//假设固定序列化方式
	
	//spark.file.transferTo 默认为false   //不可变
	public static boolean pFileTransferTo = false;  //假定固定是false
	
	//spark.executor.cores 默认为1 
	public static int pExecutorCores = 4;  //1-10
	
	//spark.storage.memoryMapThreshold 默认为2M,单位是M
	public static long pMemoryMapThreshold = 2;  //没用到
	
	//spark.akka.frameSize 10M
	public static double pakkaFrameSize = 10;  //没用到
	
	//spark.default.parallelism 
//	public static firstStageTaskNum
	
	//假设 ：combine之前和combine之后的record宽度一样
	//假设：mapCombine为false时，byPassMergeSort一定为true
	//假设：profile的应用程序中一定会发生spill
	
	//抽象数据量+依赖文件的大小，假设已知
	public static long dReadJarFileSize = 10;
	//处理的数据文件大小
	public static long dInputBytes = 0;
	//默认的executor 的memory的设置
	public static double executorMemory = 0;

	public static int stageSize = 26;
	
}
