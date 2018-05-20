package Simulation;


import java.util.LinkedList;

/**
 * Created by Administrator on 2016/1/3.
 */
public class Stage {
    private int id;
    private int jobId;
    private String type;
    private String poolName;
    private int taskNums;
    private int waitingTaskNums;
    private int runningTaskNums;
    private long finishTime=0;
    private long startTime=0;

    private long delayTime;
    private long firstTaskTime;
    private long secondTaskTime;
    private LinkedList<Integer> denpendency;

    private int weight = 1;
    private int minShare = 0;
    private int priority;
    private String name;
    private int runningTasks=0;

    public Stage(int id, int jobId, String type,String poolName, int taskNums, int delayTime, long firstTaskTime, long secondTaskTime,LinkedList<Integer> denpencyList) {
        this.id = id;
        this.jobId = jobId;
        this.type = type;
        this.poolName=poolName;
        this.taskNums = taskNums;
        this.waitingTaskNums=taskNums;
        this.runningTaskNums=taskNums;
        this.delayTime = delayTime;
        this.firstTaskTime = firstTaskTime;
        this.secondTaskTime = secondTaskTime;
        this.denpendency=denpencyList;
        this.priority=jobId;
        this.name=id+"";
    }

    public Stage() {
		// TODO Auto-generated constructor stub
	}

	public int getRunningTasks() {
        return runningTasks;
    }

    public void setRunningTasks(int runningTasks) {
        this.runningTasks = runningTasks;
    }
    public void addRunningTasks() {
        runningTasks ++;
    }
    public void subsectRunningTasks() {
        runningTasks --;
        if(runningTasks<0){
            runningTasks=0;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return id+":"+startTime+","+finishTime+","+(finishTime-startTime);
    }

    public int getWaitingTaskNums() {
        return waitingTaskNums;
    }

    public void setWaitingTaskNums(int waitingTaskNums) {
        this.waitingTaskNums = waitingTaskNums;
    }

    public int getRunningTaskNums() {
        return runningTaskNums;
    }

    public void setRunningTaskNums(int runningTaskNums) {
        this.runningTaskNums = runningTaskNums;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public boolean removeDenpency(Integer stageId){
       return denpendency.remove(stageId);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isNoDenpency(){
        return  denpendency.size()==0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTaskNums() {
        return taskNums;
    }

    public void setTaskNums(int taskNums) {
        this.taskNums = taskNums;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public long getFirstTaskTime() {
        return firstTaskTime;
    }

    public void setFirstTaskTime(long firstTaskTime2) {
        this.firstTaskTime = firstTaskTime2;
    }

    public long getSecondTaskTime() {
        return secondTaskTime;
    }

    public void setSecondTaskTime(long secondTaskTime) {
        this.secondTaskTime = secondTaskTime;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public int getMinShare() {
        return minShare;
    }

    public void setMinShare(int minShare) {
        this.minShare = minShare;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
