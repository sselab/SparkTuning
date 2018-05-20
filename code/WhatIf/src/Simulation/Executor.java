package Simulation;


import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/1/8.
 */
public class Executor {
    private int execId;
    private LinkedList<Core> cores=new LinkedList<>();
    private HashSet<Integer> runningStageId=new HashSet<>();
    private HashSet<Integer> runnedStageId=new HashSet<>();

    public Executor(int execId){
        this.execId=execId;
    }

    public void addCore(Core core){
        cores.add(core);
    }
    public boolean containStageId(int stageId){
        if(runnedStageId.contains(stageId)){
            return  true;
        }else{
            runningStageId.add(stageId);
            return  false;
        }
    }

    public void updateStageId(){
        runnedStageId.addAll(runningStageId);
        runningStageId.clear();
    }

    public void printDetails(){
       System.out.println("======  "+execId+"  ======");
        for(Core core:cores){
            System.out.println(core);
        }
    }
}
