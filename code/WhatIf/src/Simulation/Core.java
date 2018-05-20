package Simulation;

/**
 * Created by Administrator on 2016/1/4.
 */
public class Core {
    private int id;
    private long finishTime;
    private int stageId;
    private boolean isRunning;
    private String record="";

    public Core(int id){
        this.isRunning=false;
        this.id=id;
        finishTime=0;
        stageId=-1;
        record+=id+"   ";
    }

    public void updateRecord(int stageId,long finishTime){
        if(finishTime!=this.finishTime) {
            record += stageId + ":" + this.finishTime + "," + finishTime + " ";
            this.finishTime = finishTime;

        }
        this.stageId = stageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }


    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }
    public String toString(){
        return  record;
    }
}
