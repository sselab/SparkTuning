package org.apache.spark;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by Administrator on 2016/3/16.
 */
public class ShuffleWrite extends Shuffle{
    public boolean mapSideCombine;
    public boolean byPassMergeSort;
    public boolean spillEnabled;
    public long cShuffleWriteTime;
    public long cPartitionPhaseTime;
    public long cCombPhaseTime;
    public long dCombRecs;
    public long cSortPhaseTime;
    public long dnumspills;
    public long cSpillPhaseTime;   //new
    public long dSpillRecs;
    public long dPerSpillRecs;
    public long cMergeReadTime;
    public long dSpillFileSize;
    public long cMergeSortTime;
    public long cMergeCombTime;
    public long cMergeWriteTime;
    public long dSpillBufferSize;
    public long dShuffleWriteBytes;
    public long dShuffleWriteRecs;
    public long objectSize;
//    public long cTrueShuffleWriteTime;
//    public long cSpillTime;
//    public long cMergeTime;
//    public long cMaybeSpillTime;
//    public long s1;
//    public long s2;
//    public long s3;

    public Element toXMLElement(Document document){
        Element shuffleWriteElement=document.createElement("shuffleWrite");

        Element mapSideCombine = document.createElement("mapSideCombine");
        mapSideCombine.setTextContent(String.valueOf(this.mapSideCombine));
        shuffleWriteElement.appendChild(mapSideCombine);

        Element byPassMergeSort = document.createElement("byPassMergeSort");
        byPassMergeSort.setTextContent(String.valueOf(this.byPassMergeSort));
        shuffleWriteElement.appendChild(byPassMergeSort);

        Element spillEnabled = document.createElement("spillEnabled");
        spillEnabled.setTextContent(String.valueOf(this.spillEnabled));
        shuffleWriteElement.appendChild(spillEnabled);

        //add
//        Element cTrueShuffleWriteTime = document.createElement("cTrueShuffleWriteTime");
//        cTrueShuffleWriteTime.setTextContent(String.valueOf(this.cTrueShuffleWriteTime));
//        shuffleWriteElement.appendChild(cTrueShuffleWriteTime);
//
//        Element cSpillTime = document.createElement("cSpillTime");
//        cSpillTime.setTextContent(String.valueOf(this.cSpillTime));
//        shuffleWriteElement.appendChild(cSpillTime);
//
//        Element cMergeTime = document.createElement("cMergeTime");
//        cMergeTime.setTextContent(String.valueOf(this.cMergeTime));
//        shuffleWriteElement.appendChild(cMergeTime);
//
//        Element cMaybeSpillTime = document.createElement("cMaybeSpillTime");
//        cMaybeSpillTime.setTextContent(String.valueOf(this.cMaybeSpillTime));
//        shuffleWriteElement.appendChild(cMaybeSpillTime);
//
//        Element s1 = document.createElement("s1");
//        s1.setTextContent(String.valueOf(this.s1));
//        shuffleWriteElement.appendChild(s1);
//
//        Element s2 = document.createElement("s2");
//        s2.setTextContent(String.valueOf(this.s2));
//        shuffleWriteElement.appendChild(s2);
//
//        Element s3 = document.createElement("s3");
//        s3.setTextContent(String.valueOf(this.s3));
//        shuffleWriteElement.appendChild(s3);

        //add
        Element cShuffleWriteTime = document.createElement("cShuffleWriteTime");
        cShuffleWriteTime.setTextContent(String.valueOf(this.cShuffleWriteTime));
        shuffleWriteElement.appendChild(cShuffleWriteTime);

        Element cPartitionPhaseTime = document.createElement("cPartitionPhaseTime");
        cPartitionPhaseTime.setTextContent(String.valueOf(this.cPartitionPhaseTime));
        shuffleWriteElement.appendChild(cPartitionPhaseTime);

        Element cCombPhaseTime = document.createElement("cCombPhaseTime");
        cCombPhaseTime.setTextContent(String.valueOf(this.cCombPhaseTime));
        shuffleWriteElement.appendChild(cCombPhaseTime);

        Element cSortPhaseTime = document.createElement("cSortPhaseTime");
        cSortPhaseTime.setTextContent(String.valueOf(this.cSortPhaseTime));
        shuffleWriteElement.appendChild(cSortPhaseTime);

        Element cSpillPhaseTime = document.createElement("cSpillPhaseTime");
        cSpillPhaseTime.setTextContent(String.valueOf(this.cSpillPhaseTime));
        shuffleWriteElement.appendChild(cSpillPhaseTime);

        Element dnumspills = document.createElement("dnumspills");
        dnumspills.setTextContent(String.valueOf(this.dnumspills));
        shuffleWriteElement.appendChild(dnumspills);

        Element dSpillRecs = document.createElement("dSpillRecs");
        dSpillRecs.setTextContent(String.valueOf(this.dSpillRecs));
        shuffleWriteElement.appendChild(dSpillRecs);

        Element dPerSpillRecs = document.createElement("dPerSpillRecs");
        dPerSpillRecs.setTextContent(String.valueOf(this.dPerSpillRecs));
        shuffleWriteElement.appendChild(dPerSpillRecs);

        Element dSpillBufferSize = document.createElement("dSpillBufferSize");
        dSpillBufferSize.setTextContent(String.valueOf(this.dSpillBufferSize));
        shuffleWriteElement.appendChild(dSpillBufferSize);

        Element dSpillFileSize = document.createElement("dSpillFileSize");
        dSpillFileSize.setTextContent(String.valueOf(this.dSpillFileSize));
        shuffleWriteElement.appendChild(dSpillFileSize);

        Element cMergeReadTime = document.createElement("cMergeReadTime");
        cMergeReadTime.setTextContent(String.valueOf(this.cMergeReadTime));
        shuffleWriteElement.appendChild(cMergeReadTime);

        Element cMergeSortTime = document.createElement("cMergeSortTime");
        cMergeSortTime.setTextContent(String.valueOf(this.cMergeSortTime));
        shuffleWriteElement.appendChild(cMergeSortTime);

        Element cMergeCombTime = document.createElement("cMergeCombTime");
        cMergeCombTime.setTextContent(String.valueOf(this.cMergeCombTime));
        shuffleWriteElement.appendChild(cMergeCombTime);

        Element cMergeWriteTime = document.createElement("cMergeWriteTime");
        cMergeWriteTime.setTextContent(String.valueOf(this.cMergeWriteTime));
        shuffleWriteElement.appendChild(cMergeWriteTime);

        Element dCombRecs = document.createElement("dCombRecs");
        dCombRecs.setTextContent(String.valueOf(this.dCombRecs));
        shuffleWriteElement.appendChild(dCombRecs);

        Element dShuffleWriteRecs = document.createElement("dShuffleWriteRecs");
        dShuffleWriteRecs.setTextContent(String.valueOf(this.dShuffleWriteRecs));
        shuffleWriteElement.appendChild(dShuffleWriteRecs);

        Element dShuffleWriteBytes = document.createElement("dShuffleWriteBytes");
        dShuffleWriteBytes.setTextContent(String.valueOf(this.dShuffleWriteBytes));
        shuffleWriteElement.appendChild(dShuffleWriteBytes);

        Element objectSize = document.createElement("objectSize");
        objectSize.setTextContent(String.valueOf(this.objectSize));
        shuffleWriteElement.appendChild(objectSize);


        return shuffleWriteElement;
    }
}
