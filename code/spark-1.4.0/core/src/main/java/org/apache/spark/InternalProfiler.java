package org.apache.spark;

import org.apache.spark.util.SystemClock;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/1/6.
 */
public  class InternalProfiler {
    public static Map<Long,TaskProfile> taskIdToProfile=Collections.synchronizedMap(new TreeMap<Long,TaskProfile>());
    private static ConcurrentHashMap<Long,Long>  threadIdToTaskId=new ConcurrentHashMap<Long, Long>();
    public static Map<Integer, List<TaskProfile>> stageIdToTaskProfiles = new HashMap<Integer, List<TaskProfile>>();
    public static String applicationId ;



    public static  void oneTaskStart(long threadId,long taskId){
        threadIdToTaskId.put(threadId,taskId);
        TaskProfile taskProfile=new TaskProfile(taskId);
        taskIdToProfile.put(taskId, taskProfile);
    }

    public static TaskProfile getTaskProfileForThreadId(long threadId){
        long taskId=threadIdToTaskId.get(threadId);
        TaskProfile taskProfile=taskIdToProfile.get(taskId);
        return taskProfile;
    }

    public static void writeStageXML(int stageId){
        String logPath = "d:/usr/sparklogs/"+applicationId+"/executor";
        List<TaskProfile> taskProfiles=stageIdToTaskProfiles.get(stageId);
        if(taskProfiles!=null) {
            try {
                File path = new File(logPath);
                if (!path.exists()) {
                    path.mkdirs();
                }
                String name = "Stage-" + stageId + ".xml";
                File file = new File(path, name);
                if (!file.exists()) {
                    file.createNewFile();
                }



                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document=builder.newDocument();
                document.setXmlVersion("1.0");

                Element root=document.createElement("Stage");
                root.setAttribute("id", String.valueOf(stageId));
                document.appendChild(root);


                int i=0;

                for(TaskProfile taskProfile:taskProfiles){
                    if(i==0){
                        i++;
                        Element shuffleManager =document.createElement("ShuffleManager");
                        shuffleManager.setTextContent(taskProfile.shuffleManager);
                        root.appendChild(shuffleManager);

                        Element isRead = document.createElement("isRead");
                        isRead.setTextContent(String.valueOf(taskProfile.isRead));
                        root.appendChild(isRead);

                        Element isShuffleRead = document.createElement("isShuffleRead");
                        isShuffleRead.setTextContent(String.valueOf(taskProfile.isShuffleRead));
                        root.appendChild(isShuffleRead);

                        Element isShuffleWrite = document.createElement("isShuffleWrite");
                        isShuffleWrite.setTextContent(String.valueOf(taskProfile.isShuffleWrite));
                        root.appendChild(isShuffleWrite);

                    }
                    root.appendChild(taskProfile.toXMLElement(document));
                }

                TransformerFactory transformerFactory=TransformerFactory.newInstance();
                Transformer transformer=transformerFactory.newTransformer();
                DOMSource domSource=new DOMSource(document);

                FileOutputStream fout=new FileOutputStream(file);
                StreamResult xmlResult=new StreamResult(fout);
                transformer.transform(domSource,xmlResult);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

//    public static void writeStageInfoXML(Long stageid) {
//
//        File logPathRoot = new File(logPath);
//        if(!logPathRoot.isDirectory()) {
//            logPathRoot.mkdir();
//        }
//
//        List<TaskProfile> taskProfiles = stageIdToTaskProfiles.get(stageid);
//        if(taskProfiles != null) {
//            File applicationLog = new File(logPathRoot,applicationId);
//            if(!applicationLog.isDirectory()) {
//                applicationLog.mkdir();
//            }
//
//            File stageLog = new File(applicationLog,stageid.toString()+".xml");
//            if(!stageLog.exists()){
//                stageLog.createNewFile();
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder builder = factory.newDocumentBuilder();
//                Document document=builder.newDocument();
//                document.setXmlVersion("1.0");
//
//                Element root=document.createElement("stage");
//                document.appendChild(root);
//                Element stageId = document.createElement("stageId");
//                stageId.setTextContent(stageid.toString());
//                root.appendChild(stageId);
//                for(TaskProfile taskProfile: taskProfiles) {
//                    Element taskRoot = document.createElement("task");
//                    root.appendChild(taskRoot);
//                    taskRoot.setAttribute("taskId",taskProfile.taskId);
//                }
//            }
//
//        }
//    }

}
