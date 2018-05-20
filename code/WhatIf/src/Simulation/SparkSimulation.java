package Simulation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
import java.io.File;

/**
 * Created by Administrator on 2016/1/3.
 */
public class SparkSimulation {
    private int coreNums;
    private int executorNums;
    private long minFinishTime=0;
    private long lastMinFinishTime=0;

    private List<Stage> stageList=new ArrayList<Stage>();
    private List<Stage> waitingStageList=new ArrayList<Stage>();
    private List<Stage> runningStageList=new ArrayList<Stage>();
    private List<Core> coreList=new ArrayList<Core>();
    private List<Executor> executorList=new ArrayList<Executor>();
    private Map<Integer,Integer> jobIdToStageNums=new HashMap<>();
    private Map<String,Pool> nameToPool=new HashMap<>();

    private Comparator<Stage> FIFOComparator=SchedulerAlg.FIFO;
    private Comparator<Stage> FAIRComparator=SchedulerAlg.sFAIR;
    private Comparator<Pool>  poolComparator=SchedulerAlg.pFAIR;

    private Comparator<Stage> stageComparator=new Comparator<Stage>() {
        @Override
        public int compare(Stage o1, Stage o2) {
            Pool pool1=nameToPool.get(o1.getPoolName());
            Pool pool2=nameToPool.get(o2.getPoolName());
            int poolFlag=poolComparator.compare(pool1,pool2);
            if(poolFlag!=0){
                return poolFlag;
            }else{
                return  FIFOComparator.compare(o1,o2);
            }
        }
    };

    public void getSortedList(){
        ArrayList<Stage> result=new ArrayList<>();
        TreeSet<Pool> pools=new TreeSet(poolComparator);
        HashMap<String,ArrayList<Stage>> poolToStage=new HashMap<String,ArrayList<Stage>>();
        for(Stage stage:runningStageList){
            String poolName=stage.getPoolName();
            pools.add(nameToPool.get(poolName));
            if(poolToStage.get(poolName)==null){
                poolToStage.put(poolName,new ArrayList<Stage>());
            }
            poolToStage.get(poolName).add(stage);
        }
        for(Pool pool:pools){
            List stages=poolToStage.get(pool.getName());
            if(pool.getMode()==0) {
                Collections.sort(stages,FAIRComparator);
            }else if(pool.getMode()==1){
                Collections.sort(stages,FIFOComparator);
            }
            result.addAll(stages);
        }
        runningStageList=result;

    }

    public SparkSimulation(int executorNums,int coreNums,Stage[] stageInfos,File file){
        this.executorNums=executorNums;
        this.coreNums=coreNums;
        for(int i=0;i<executorNums;i++){
            executorList.add(new Executor(i));
        }
        for(int i=0;i<coreNums;i++){
            Core core=new Core(i);
            coreList.add(core);
            executorList.get(i%executorNums).addCore(core);

        }

        constructStageFromXMLAndStageInfo(file, stageInfos);
        Collections.sort(stageList, stageComparator);
    }

    public void constructStageFromXMLAndStageInfo(File file,Stage[] stageInfos)  {
        try{
            DocumentBuilderFactory dbFactory =DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            Document doc=dbBuilder.parse(file);
            NodeList poolList=doc.getElementsByTagName("pool");
            for(int j=0;j<poolList.getLength();j++){
                Element pool=(Element)poolList.item(j);
                String name=pool.getElementsByTagName("name").item(0).getTextContent();
                int mode=Integer.parseInt(pool.getElementsByTagName("mode").item(0).getTextContent());
                int minShare=Integer.parseInt(pool.getElementsByTagName("minShare").item(0).getTextContent());
                int weight=Integer.parseInt(pool.getElementsByTagName("weight").item(0).getTextContent());
                nameToPool.put(name,new Pool(name,mode,minShare,weight));
            }
            //System.out.println(nameToPool.size());

            NodeList jobList=doc.getElementsByTagName("job");
            int i;
            for(i=0;i<jobList.getLength();i++){
                Element job=(Element)jobList.item(i);
                int jobId=Integer.parseInt(job.getAttribute("id"));
                NodeList sL=job.getElementsByTagName("stage");
                jobIdToStageNums.put(jobId,sL.getLength());
                for(int j=0;j<sL.getLength();j++){
                    Element stage=(Element)sL.item(j);
                    int stageId=Integer.parseInt(stage.getElementsByTagName("stageId").item(0).getTextContent());

                    Stage info=null;
                    for(int k=0;k<stageInfos.length;k++){
                        if(stageInfos[k].getId()==stageId) {
                            info=stageInfos[k];
                            break;
                        }
                    }


                    String stageType=stage.getElementsByTagName("type").item(0).getTextContent();

                    String poolName=stage.getElementsByTagName("poolName").item(0).getTextContent();
                    nameToPool.get(poolName).addJob(jobId);

                    String parents=stage.getElementsByTagName("parents").item(0).getTextContent().trim();
                    LinkedList<Integer> denpencyList = new LinkedList<Integer>();
                    if(parents.length()!=0) {
                        for (String did : parents.split(" ")) {
                            denpencyList.add(Integer.parseInt(did));
                        }
                    }

                    String isUsed=stage.getElementsByTagName("isUsed").item(0).getTextContent();

                    if(info!=null) {
                        if("true".equals(isUsed)){
                            info.setTaskNums(0);
                        }
                        stageList.add(new Stage(stageId, jobId, stageType, poolName, info.getTaskNums(), 0, info.getFirstTaskTime(), info.getSecondTaskTime(), denpencyList));
                    } else{
                        stageList.add(new Stage(stageId, jobId, stageType, poolName, 0, 0, 0 ,0, denpencyList));
                    }
                }

            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public long start(){

        run();
//        System.out.println("============ EXECUTOR ============");
//        System.out.println("====== execId ======");
//        System.out.println("CoreNumber stageId:taskStartTime,taskEndTime stageId:startTime,endTime .......");
//        System.out.println();
//        for(Executor executor:executorList){
//            executor.printDetails();
//        }
//        System.out.println();
//        System.out.println("============ EXECUTOR ============");
//        System.out.println();
        Collections.sort(stageList, FIFOComparator);
//        System.out.println("============  STAGE  ============");
//        System.out.println("stageId:stageStartTime,stageEndTime,stageExecuteTime");          //stageStartTime 是第一个属于这个stage的task开始执行时间，而不是stage提交时间（网页上的）
//        System.out.println();
        for(Stage stage:stageList){
            System.out.println(stage);
        }
        System.out.println(minFinishTime);
//        System.out.println();
//        System.out.println("============  STAGE  ============");
//        System.out.println();
        return minFinishTime;
    }


    //判断executor是否是第二次加载此stage的Task。（第二次加载是指第一次加载的task已经完成，如果同时加载4个同stage的task，还是算第一次加载）
    public boolean isRunSencondTime(Core core,Stage stage){
        return executorList.get(core.getId()%executorNums).containStageId(stage.getId());
    }

    //更新executor中完成过的task所属于的stage编号。用来判断是否二次加载。
    public void updateExecutorInfos(){
        for(Executor executor:executorList){
            executor.updateStageId();
        }
    }

    //开始时，获取每个Pool的初始可执行Job
    public void addStageForStart(){
        for(Pool pool:nameToPool.values()){
            Integer jobId=pool.findNextJob();
            //System.out.println(jobId);
            if(jobId!=null){
                pool.setRunningStagesNumsThisJob(jobIdToStageNums.get(jobId));
                findStage(jobId);
            }
        }
    }

    //寻找Pool中的下一个Job
    public void updateStageForPool(Stage stage){
        String poolName=stage.getPoolName();
        Pool pool=nameToPool.get(poolName);
        if(pool.isJobFinish()){
            Integer jobId=pool.findNextJob();
            if(jobId!=null){
                pool.setRunningStagesNumsThisJob(jobIdToStageNums.get(jobId));
               // pool.setRunningTasksNum(0);
               findStage(jobId);
            }
        }

    }

    //寻找属于某个Job的stage
    public void findStage(int jobId) {
        for (Stage stage : stageList) {
            if (stage.getJobId() == jobId) {
                waitingStageList.add(stage);

            }
        }
    }

    //寻找当前等待的stage中可执行的stage。
    public void findNoDenpencyStage() {

        Iterator<Stage> waitingIterator=waitingStageList.iterator();
        while(waitingIterator.hasNext()){
            Stage stage=waitingIterator.next();
            if(stage.isNoDenpency()){
                runningStageList.add(stage);
                waitingIterator.remove();
            }
        }


    }

    //根据编号寻找stage
    public Stage findStageById(int stageId){
        for(Stage stage:runningStageList){
            if(stage.getId()==stageId)
                return stage;
        }
        return null;
    }

    //更新每个Pool中正在运行的任务数量
    public void updatePoolRunningTasks(){
        for(Core core:coreList){
            if(core.getFinishTime()==minFinishTime){
                if(core.isRunning()){
                    int stageId=core.getStageId();
                    if(stageId!=-1){
                        Stage stage=findStageById(core.getStageId());
                        nameToPool.get(stage.getPoolName()).subsectRunningTasks();
                    }
                }
            }
        }
    }

    //模拟运行
    public void run() {
        addStageForStart();
        boolean isFirstTime=true;
        while (waitingStageList.size() != 0 || runningStageList.size() != 0) {
            findNoDenpencyStage();

            for (Core core : coreList) {
                if(isFirstTime) {
                    Collections.sort(runningStageList, FIFOComparator);
                }else{
                    getSortedList();
                }
                if (core.getFinishTime() == minFinishTime) {
                    boolean launchTask=false;
                    for (Stage stage : runningStageList) {

                        if(stage.getTaskNums()==0){
                            stage.setStartTime(minFinishTime);
                        }

                        if (stage.getWaitingTaskNums() != 0) {
                            //System.out.println(stage.getId());
                            if(core.isRunning()){
                                Stage runningStage=findStageById(core.getStageId());
                                runningStage.setRunningTaskNums(runningStage.getRunningTaskNums()-1);
                                runningStage.subsectRunningTasks();
                               // nameToPool.get(runningStage.getPoolName()).subsectRunningTaskNums();
                            }
                           if(stage.getWaitingTaskNums()==stage.getTaskNums()){
                               stage.setStartTime(minFinishTime);
                           }
                            stage.setWaitingTaskNums(stage.getWaitingTaskNums() - 1);
                            stage.addRunningTasks();
                            nameToPool.get(stage.getPoolName()).addRunningTasks();

                            if (isRunSencondTime(core,stage)) {
                                core.updateRecord(stage.getId(), minFinishTime+stage.getSecondTaskTime()+stage.getDelayTime());

                            } else {
                                core.updateRecord(stage.getId(), minFinishTime + stage.getFirstTaskTime()+stage.getDelayTime());

                            }
                            launchTask=true;
                            break;
                        }

                    }

                    if(!launchTask) {
                        if(core.isRunning()){
                            Stage runningStage=findStageById(core.getStageId());
                            runningStage.setRunningTaskNums(runningStage.getRunningTaskNums()-1);
                            runningStage.subsectRunningTasks();
                           // nameToPool.get(runningStage.getPoolName()).subsectRunningTaskNums();
                            core.setStageId(-1);
                        }
                        core.setIsRunning(false);
                    }else{
                        core.setIsRunning(true);
                    }

                }
            }
            isFirstTime=false;

            Iterator<Stage> runningIterator=runningStageList.iterator();
            int count=0;
            while(runningIterator.hasNext()){
                Stage stage=runningIterator.next();
                if(stage.getRunningTaskNums()==0){
                    count++;
                    //System.out.println("finish"+stage.getId());
                    stage.setFinishTime(minFinishTime);
                    updateStageForPool(stage);
                    for(Stage stage1:stageList){
                        stage1.removeDenpency(stage.getId());
                    }
                    runningIterator.remove();
                }
            }
            if(count!=0){
                for(Core core:coreList){
                    if(!core.isRunning()){
                        core.setFinishTime(minFinishTime);
                    }
                }
            }else{

                updateExecutorInfos();
                for(Core core:coreList){
                    if(!core.isRunning()){
                        core.setFinishTime(lastMinFinishTime);
                    }
                }

            }

            updateFinishTime();
            updatePoolRunningTasks();


        }



    }

    public void updateFinishTime(){
        long min=Long.MAX_VALUE;
        long lastMin=Long.MAX_VALUE;
        for(Core core:coreList){
            long time=core.getFinishTime();
            if(min>time) {
                min=time;
            }

        }
        for(Core core:coreList){
        	long time=core.getFinishTime();
            if(lastMin>time&&time!=min) {
                lastMin=time;
            }

        }
        if(lastMin==Integer.MAX_VALUE){
            lastMin=min;
        }
        //System.out.println(min);
       // System.out.println(lastMin);
        minFinishTime=min;
        lastMinFinishTime=lastMin;
    }

    public static void main(String[] args){

        Stage[] stageInfos=new Stage[9];
//        int[] taskNums=new int[]{4,6,10,10,4,6,6,4,4,4};
//        int[] firstTaskTime=new int[]{6338,4207,3348,388,333,333,735,888,2100,400};
//        int[] secondedTime=new int[]{5290,3800,1200,315,333,333,530,1474,2500,410};
//        for(int i=0;i<9;i++){
//            Stage stage=new Stage(i,0,"","",taskNums[i],0,firstTaskTime[i],secondedTime[i],null);
//            stageInfos[i]=stage;
//        }
        SparkSimulation simulation1=new SparkSimulation(1,2,stageInfos,new File("1457935512826.xml"));
        simulation1.start();
    }
}
