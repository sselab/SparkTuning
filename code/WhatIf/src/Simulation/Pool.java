package Simulation;

import java.util.TreeSet;

/**
 * Created by Administrator on 2016/1/11.
 */
public class Pool {
    private String name;
    private int mode;
    private int minShare;
    private int weight;
    private int runningTasks;
    private TreeSet<Integer> jobSet;
    private int runningStagesNumsThisJob;

    public Pool(String name,int mode,int minShare,int weight){
        this.name=name;
        this.mode=mode;
        this.minShare=minShare;
        this.weight=weight;
        runningTasks=0;
        jobSet=new TreeSet<>();
        runningStagesNumsThisJob=0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void addJob(int jobId){
        jobSet.add(jobId);
    }

    public Integer findNextJob(){

         return jobSet.pollFirst();
    }

    public int getRunningStagesNumsThisJob() {
        return runningStagesNumsThisJob;
    }

    public void setRunningStagesNumsThisJob(int runningStagesNumsThisJob) {
        this.runningStagesNumsThisJob = runningStagesNumsThisJob;
    }

    public boolean isJobFinish(){
       runningStagesNumsThisJob--;
        return runningStagesNumsThisJob==0;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
