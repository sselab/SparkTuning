package org.apache.spark;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import scala.collection.JavaConversions;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * Created by Administrator on 2016/3/14.
 */
public class DriverTracer {
    public static ArrayList<org.apache.spark.Pool> pools=new ArrayList<org.apache.spark.Pool>();
    public static ArrayList<org.apache.spark.Stage> stages=new ArrayList<org.apache.spark.Stage>();
    public static Map<Integer, org.apache.spark.Stage> stageIdToStage = new TreeMap<Integer, org.apache.spark.Stage>();
    public static String appID;
//    public static Map<Integer, Long> stageIdToScehdulerDelay = new HashMap<Integer, Long>();
//    public static Map<Integer, Long> stageIdToResultGet = new HashMap<Integer, Long>();
//    public static Map<Integer, Long> stageIdToResultSize = new HashMap<Integer, Long>();
//    public static Map<Integer, Integer> stageIdToTaskNums = new HashMap<Integer, Integer>();
//    public static Map<Integer, String> stageIdToParents = new HashMap<Integer, String>();
//    public static Map<Integer, String> stageIdToPool = new HashMap<Integer, String>();
//    public static Map<Integer, StringBuilder> stageIdToExecuteTime =new HashMap<Integer, StringBuilder>();

    public static void traceAppId(String aid){
        appID=aid;
    }
    public static void tracePoolCreate(String poolName,int schedulingMode,int initMinShare,int initWeight){
        pools.add(new org.apache.spark.Pool(poolName,schedulingMode,initMinShare,initWeight));
    }

    public static void tracePoolNameForStage(String poolName,int stageId){
        stageIdToStage.get(stageId).setPoolName(poolName);
    }

    public static void traceUsedStage(int stageId){
        stageIdToStage.get(stageId).setIsUsed("true");
    }

    public static void traceShuffleStageCreate(int jobId, int stageId, int numTasks, String type, scala.collection.immutable.List<org.apache.spark.scheduler.Stage> parents) {
        System.out.println(stageId);
        org.apache.spark.Stage stage=new org.apache.spark.Stage(jobId, stageId,numTasks,type,JavaConversions.asJavaList(parents));
        stages.add(stage);
        stageIdToStage.put(stageId,stage);
    }

    public static void traceResultStageCreate(int jobId, int stageId, int numTasks, String type, scala.collection.immutable.List<org.apache.spark.scheduler.Stage> parents){
        System.out.println(stageId);
        org.apache.spark.Stage stage=new org.apache.spark.Stage(jobId, stageId,numTasks,type,JavaConversions.asJavaList(parents));
        stages.add(stage);
        stageIdToStage.put(stageId,stage);
    }

    public static void traceTaskEnd(){

    }

    public static void traceStageEnd(){

    }

    public static void traceEnd(){
        writeXML();
    }

    public static void writeXML(){
        String logPath = "d:/usr/sparklogs/"+appID+"/driver";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document=builder.newDocument();
            document.setXmlVersion("1.0");

            Element root=document.createElement("Application");
            root.setAttribute("id",appID);
            document.appendChild(root);

            Element poolsElement=document.createElement("pools");
            for(org.apache.spark.Pool pool:pools){
                poolsElement.appendChild(pool.toXMLElement(document));
            }
            root.appendChild(poolsElement);

            Element jobsElement=document.createElement("jobs");
            Map<String, Element> jobMap = new HashMap<String, Element>();
            for(org.apache.spark.Stage stage:stages){
                Element stageElement=stage.toXMLElement(document);
                String jobId=stageElement.getFirstChild().getTextContent();
                Element jobElement=jobMap.get(jobId);
                if(jobElement==null){
                    jobElement=document.createElement("job");
                    jobElement.setAttribute("id",jobId);
                    jobMap.put(jobId, jobElement);
                    jobsElement.appendChild(jobElement);
                }
                jobElement.appendChild(stageElement);
            }
            root.appendChild(jobsElement);

            TransformerFactory transformerFactory=TransformerFactory.newInstance();
            Transformer transformer=transformerFactory.newTransformer();
            DOMSource domSource=new DOMSource(document);


            File path=new File(logPath);

            if(!path.exists()){
                path.mkdirs();
            }

            String name="DAG.xml";
            File file=new File(path,name);
            if(!file.exists()){
                file.createNewFile();
            }

            FileOutputStream fout=new FileOutputStream(file);
            StreamResult xmlResult=new StreamResult(fout);
            transformer.transform(domSource,xmlResult);


        }catch (Exception e){
            e.printStackTrace();
        }


    }


}

class Pool{
    private String poolName;
    private int schedulingMode;
    private int initMinShare;
    private int initWeight;

    public Pool(String poolName, int schedulingMode, int initMinShare, int initWeight) {
        this.poolName = poolName;
        this.schedulingMode = schedulingMode;
        this.initMinShare = initMinShare;
        this.initWeight = initWeight;
    }

    public Element toXMLElement(Document document){
        Element poolElement=document.createElement("pool");

        Element name=document.createElement("name");
        name.setTextContent(this.poolName);
        poolElement.appendChild(name);

        Element mode=document.createElement("mode");
        mode.setTextContent(String.valueOf(this.schedulingMode));
        poolElement.appendChild(mode);

        Element minShare=document.createElement("minShare");
        minShare.setTextContent(String.valueOf(this.initMinShare));
        poolElement.appendChild(minShare);

        Element weight=document.createElement("weight");
        weight.setTextContent(String.valueOf(this.initWeight));
        poolElement.appendChild(weight);


        return poolElement;
    }
}

class Stage{
    private int jobId;
    private int stageId;
    private int numTasks;
    private String type;
    private List<org.apache.spark.scheduler.Stage> parents=new ArrayList<org.apache.spark.scheduler.Stage>();
    private String poolName;
    private String isUsed;

    public Stage(int jobId, int stageId, int numTasks, String type, List<org.apache.spark.scheduler.Stage> parents) {
        this.jobId = jobId;
        this.stageId = stageId;
        this.numTasks = numTasks;
        this.type = type;
        this.parents = parents;
        this.isUsed = "false";
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public Element toXMLElement(Document document){
        Element stageElement=document.createElement("stage");

        Element jobId=document.createElement("jobId");
        jobId.setTextContent(String.valueOf(this.jobId));
        stageElement.appendChild(jobId);

        Element stageId=document.createElement("stageId");
        stageId.setTextContent(String.valueOf(this.stageId));
        stageElement.appendChild(stageId);

        Element numTasks=document.createElement("numTasks");
        numTasks.setTextContent(String.valueOf(this.numTasks));
        stageElement.appendChild(numTasks);

        Element type=document.createElement("type");
        type.setTextContent(this.type);
        stageElement.appendChild(type);

        String parentIds = " ";
        for (org.apache.spark.scheduler.Stage s : parents) {
           int sId=s.id();
            parentIds += (sId+" ");
        }
        Element parents=document.createElement("parents");
        parents.setTextContent(parentIds);
        stageElement.appendChild(parents);


        if(poolName==null){
            poolName="root";
        }
        Element poolName=document.createElement("poolName");
        poolName.setTextContent(this.poolName);
        stageElement.appendChild(poolName);

        Element isUsed=document.createElement("isUsed");
        isUsed.setTextContent(this.isUsed);
        stageElement.appendChild(isUsed);

        return stageElement;
    }
}
