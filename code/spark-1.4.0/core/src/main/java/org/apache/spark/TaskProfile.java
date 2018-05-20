package org.apache.spark;



import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/15.
 */
public class TaskProfile implements Serializable {

    public HashMap<Integer,Integer> hashToReadOrWriteSorter=new HashMap<Integer, Integer>();
    public HashMap<Integer,Integer> hashToReadOrWriteAggerator=new HashMap<Integer, Integer>();
    public HashMap<Integer,Integer> hashToAggreatorMap=new HashMap<Integer, Integer>();

    public long taskId;
    public long stageId;
    public long taskTime;

    public String shuffleManager;


    public boolean isRead;
    public long readTime;
    public long dReadInRecs;
    public long dReadInBytes;

    public boolean isSetup;
    public long setUpTime;
    public long dSerilizeTaskSize;
    public long dSerilizeTaskContentSize;
    public long cDeserilizeTime;
    public long cReadJarFileTime;
    public long cReadJarFileSize;
    public long cDeserilizeTaskTime;
    public long cDeserilizeRDDTime;
    public long dDeserilizeRDDSize;

    public boolean isShuffleWrite;
    public ShuffleWrite shuffleWrite;

    public boolean isCompute;
    public long computeTime;
    public long dComputeOutRecs;




    public boolean isShuffleRead;
    public ShuffleRead shuffleRead;

   // public boolean isSendResult;
    public long sendResultTime;
    public long dResultBytes;
    public long cSerilizeResultTime;


    public long trueExecuteTime;

    public TaskProfile(long taskId) {
            this.taskId = taskId;

            this.isSetup = true;
            this.isRead = false;
            this.isShuffleRead = false;
            this.isShuffleWrite = false;
            //this.isSendResult = false;
            this.isCompute = true;
           shuffleRead=new ShuffleRead();
            shuffleWrite=new ShuffleWrite();
    }


    public Shuffle setShuffleReadOrWrite(int hashcode){
        int index=hashToReadOrWriteSorter.get(hashcode);
        if(index==0){
            return shuffleRead;
        }else{
            return shuffleWrite;
        }
    }

    public Shuffle getShuffleReadOrWrite(int hashcode){
        int index=hashToReadOrWriteAggerator.get(hashcode);
        if(index==0){
            return shuffleRead;
        }else{
            return shuffleWrite;
        }
    }

    public Shuffle getShuffleReadOrWriteFromMap(int hashcode){
        int hash=hashToAggreatorMap.get(hashcode);
        int index=hashToReadOrWriteAggerator.get(hash);
        if(index==0){
            return shuffleRead;
        }else{
            return shuffleWrite;
        }
    }

    public Element toXMLElement(Document document){
        taskTime=setUpTime+readTime+shuffleRead.cShuffleReadTime+computeTime+shuffleWrite.cShuffleWriteTime+cSerilizeResultTime+sendResultTime;
        Element taskElement=document.createElement("task");
        taskElement.setAttribute("id",String.valueOf(taskId));
        taskElement.setAttribute("time",String.valueOf(taskTime));
        taskElement.setAttribute("trueExecuteTime",String.valueOf(trueExecuteTime));
        if(isSetup){
            Element setupElement = document.createElement("setup");
            taskElement.appendChild(setupElement);


            Element setUpTime = document.createElement("setUpTime");
            setUpTime.setTextContent(String.valueOf(this.setUpTime));
            setupElement.appendChild(setUpTime);

            Element dSerilizeTaskSize = document.createElement("dSerilizeTaskSize");
            dSerilizeTaskSize.setTextContent(String.valueOf(this.dSerilizeTaskSize));
            setupElement.appendChild(dSerilizeTaskSize);

            Element dSerilizeTaskContentSize = document.createElement("dSerilizeTaskContentSize");
            dSerilizeTaskContentSize.setTextContent(String.valueOf(this.dSerilizeTaskContentSize));
            setupElement.appendChild(dSerilizeTaskContentSize);

            Element cDeserilizeTime = document.createElement("cDeserilizeTime");
            cDeserilizeTime.setTextContent(String.valueOf(this.cDeserilizeTime));
            setupElement.appendChild(cDeserilizeTime);

            Element cReadJarFileTime = document.createElement("cReadJarFileTime");
            cReadJarFileTime.setTextContent(String.valueOf(this.cReadJarFileTime));
            setupElement.appendChild(cReadJarFileTime);

            Element cDeserilizeTaskTime = document.createElement("cDeserilizeTaskTime");
            cDeserilizeTaskTime.setTextContent(String.valueOf(this.cDeserilizeTaskTime));
            setupElement.appendChild(cDeserilizeTaskTime);

            Element cDeserilizeRDDTime = document.createElement("cDeserilizeRDDTime");
            cDeserilizeRDDTime.setTextContent(String.valueOf(this.cDeserilizeRDDTime));
            setupElement.appendChild(cDeserilizeRDDTime);

            Element dDeserilizeRDDSize = document.createElement("dDeserilizeRDDSize");
            dDeserilizeRDDSize.setTextContent(String.valueOf(this.dDeserilizeRDDSize));
            setupElement.appendChild(dDeserilizeRDDSize);


        }
        if(isRead) {
            Element readElement = document.createElement("read");
            taskElement.appendChild(readElement);

            Element readTime = document.createElement("readTime");
            readTime.setTextContent(String.valueOf(this.readTime));
            readElement.appendChild(readTime);

            Element dReadInRecs = document.createElement("dReadInRecs");
            dReadInRecs.setTextContent(String.valueOf(this.dReadInRecs));
            readElement.appendChild(dReadInRecs);

            Element dReadInBytes = document.createElement("dReadInBytes");
            dReadInBytes.setTextContent(String.valueOf(this.dReadInBytes));
            readElement.appendChild(dReadInBytes);
        }

        if(isShuffleRead){
            taskElement.appendChild(shuffleRead.toXMLElement(document));
        }

        if(isCompute){
            Element computeElement = document.createElement("compute");
            taskElement.appendChild(computeElement);

            Element computeTime = document.createElement("computeTime");
            computeTime.setTextContent(String.valueOf(this.computeTime));
            computeElement.appendChild(computeTime);

            Element dComputeOutRecs = document.createElement("dComputeOutRecs");
            dComputeOutRecs.setTextContent(String.valueOf(this.dComputeOutRecs));
            computeElement.appendChild(dComputeOutRecs);

        }

        if(isShuffleWrite){
            taskElement.appendChild(shuffleWrite.toXMLElement(document));
        }


            Element sendResultElement = document.createElement("sendResult");
            taskElement.appendChild(sendResultElement);

            Element sendResultTime = document.createElement("sendResultTime");
            sendResultTime.setTextContent(String.valueOf(this.sendResultTime));
            sendResultElement.appendChild(sendResultTime);

            Element dResultBytes = document.createElement("dResultBytes");
            dResultBytes.setTextContent(String.valueOf(this.dResultBytes));
            sendResultElement.appendChild(dResultBytes);

            Element cSerilizeResultTime = document.createElement("cSerilizeResultTime");
            cSerilizeResultTime.setTextContent(String.valueOf(this.cSerilizeResultTime));
            sendResultElement.appendChild(cSerilizeResultTime);



        return taskElement;
    }


}

 class Shuffle implements Serializable{

 }



