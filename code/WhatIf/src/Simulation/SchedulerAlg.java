package Simulation;


import java.util.Comparator;
/**
 * Created by Administrator on 2016/1/11.
 */
public class SchedulerAlg {

    public static Comparator<Pool> pFAIR=new Comparator<Pool>() {
        @Override
        public int compare(Pool o1, Pool o2) {
            int minShare1=o1.getMinShare();
            int minShare2=o2.getMinShare();
            int runningTasks1=o1.getRunningTasks();
            int runningTasks2=o2.getRunningTasks();
            boolean s1Needy=runningTasks1<minShare1;
            boolean s2Needy=runningTasks2<minShare2;
            double minShareRatio1=(double)runningTasks1/Math.max(minShare1,1.0);
            double minShareRatio2=(double)runningTasks2/Math.max(minShare2,1.0);
            double taskToWeightRatio1=(double)runningTasks1/o1.getWeight();
            double taskToWeightRatio2=(double)runningTasks2/o2.getWeight();

            double compare=0;


            if (s1Needy && !s2Needy) {
                return -1;
            } else if (!s1Needy && s2Needy) {
                return 1;
            } else if (s1Needy && s2Needy) {
                compare = minShareRatio1-minShareRatio2;
            } else {
                compare = taskToWeightRatio1-taskToWeightRatio2;
            }

            if (compare < 0) {
                return -1;
            } else if (compare > 0) {
               return 1;
            } else {
                return o1.getName().compareTo(o2.getName());
            }
        }
    };

    public static Comparator<Stage> sFAIR=new Comparator<Stage>() {
        @Override
        public int compare(Stage o1, Stage o2) {
            int minShare1=o1.getMinShare();
            int minShare2=o2.getMinShare();
            int runningTasks1=o1.getRunningTasks();
            int runningTasks2=o2.getRunningTasks();
            boolean s1Needy=runningTasks1<minShare1;
            boolean s2Needy=runningTasks2<minShare2;
            double minShareRatio1=(double)runningTasks1/Math.max(minShare1,1.0);
            double minShareRatio2=(double)runningTasks2/Math.max(minShare2,1.0);
            double taskToWeightRatio1=(double)runningTasks1/o1.getWeight();
            double taskToWeightRatio2=(double)runningTasks2/o2.getWeight();

            double compare=0;


            if (s1Needy && !s2Needy) {
                return -1;
            } else if (!s1Needy && s2Needy) {
                return 1;
            } else if (s1Needy && s2Needy) {
                compare = minShareRatio1-minShareRatio2;
            } else {
                compare = taskToWeightRatio1-taskToWeightRatio2;
            }

            if (compare < 0) {
                return -1;
            } else if (compare > 0) {
                return 1;
            } else {
                return o1.getName().compareTo(o2.getName());
            }
        }
    };

    public static Comparator<Stage> FIFO=new Comparator<Stage>() {
        @Override
        public int compare(Stage o1, Stage o2) {
            int priority1 = o1.getPriority();
            int priority2 = o2.getPriority();
            float res = Math.signum(priority1 - priority2);
            if (res == 0) {
                int stageId1 = o1.getId();
                int stageId2 = o2.getId();
                res = Math.signum(stageId1 - stageId2);
            }
            if (res < 0) {
                return -1;
            } else {
                return 1;
            }

        }
    };

}


