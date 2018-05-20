package org.apache.spark;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by Administrator on 2016/3/16.
 */
public class ShuffleRead extends  Shuffle{
    public boolean mapSideCombine;
    public boolean sort;
    public boolean byPassMergeSort;
    public boolean spillEnabled;

    public long dShuffleReadLocalBytes;
    public long cReadLocalDataTime;
    public long dShuffleReadRemoteBytes;
    public long cShuffleFetchTime;
    public long cReadRemoteDataTime;
    public long tShuffleReadCompressTime;
    public long tShuffleReadCompressSize;
    public long cCombPhaseTime;
    public long dCombRecs;
    public long cSortPhaseTime;
    public long dnumspills;
    public long dSpillRecs;
    public long cSpillPhaseTime;  //new
    public long dPerSpillRecs;
    public long cMergeReadTime;
    public long dSpillFileSize;
    public long cMergeSortTime;
    public long cMergeCombTime;
    public long dSpillBufferSize;
    public long dShuffleReadRecs;
    public long cShuffleReadTime;
    public long cSortTime;
    public long objectSize;

    public Element toXMLElement(Document document){
        Element shuffleReadElement=document.createElement("shuffleRead");

        Element mapSideCombine = document.createElement("mapSideCombine");
        mapSideCombine.setTextContent(String.valueOf(this.mapSideCombine));
        shuffleReadElement.appendChild(mapSideCombine);

        Element byPassMergeSort = document.createElement("byPassMergeSort");
        byPassMergeSort.setTextContent(String.valueOf(this.byPassMergeSort));
        shuffleReadElement.appendChild(byPassMergeSort);

        Element spillEnabled = document.createElement("spillEnabled");
        spillEnabled.setTextContent(String.valueOf(this.spillEnabled));
        shuffleReadElement.appendChild(spillEnabled);

        Element cShuffleReadTime = document.createElement("cShuffleReadTime");
        cShuffleReadTime.setTextContent(String.valueOf(this.cShuffleReadTime));
        shuffleReadElement.appendChild(cShuffleReadTime);

        Element cShuffleFetchTime = document.createElement("cShuffleFetchTime");
        cShuffleFetchTime.setTextContent(String.valueOf(this.cShuffleFetchTime));
        shuffleReadElement.appendChild(cShuffleFetchTime);

        Element dShuffleReadLocalBytes = document.createElement("dShuffleReadLocalBytes");
        dShuffleReadLocalBytes.setTextContent(String.valueOf(this.dShuffleReadLocalBytes));
        shuffleReadElement.appendChild(dShuffleReadLocalBytes);

        Element dShuffleReadRemoteBytes = document.createElement("dShuffleReadRemoteBytes");
        dShuffleReadRemoteBytes.setTextContent(String.valueOf(this.dShuffleReadRemoteBytes));
        shuffleReadElement.appendChild(dShuffleReadRemoteBytes);

        Element dShuffleReadRecs = document.createElement("dShuffleReadRecs");
        dShuffleReadRecs.setTextContent(String.valueOf(this.dShuffleReadRecs));
        shuffleReadElement.appendChild(dShuffleReadRecs);


        Element cCombPhaseTime = document.createElement("cCombPhaseTime");
        cCombPhaseTime.setTextContent(String.valueOf(this.cCombPhaseTime));
        shuffleReadElement.appendChild(cCombPhaseTime);

        Element cSortPhaseTime = document.createElement("cSortPhaseTime");
        cSortPhaseTime.setTextContent(String.valueOf(this.cSortPhaseTime));
        shuffleReadElement.appendChild(cSortPhaseTime);

        Element cSpillPhaseTime = document.createElement("cSpillPhaseTime");
        cSpillPhaseTime.setTextContent(String.valueOf(this.cSpillPhaseTime));
        shuffleReadElement.appendChild(cSpillPhaseTime);

        Element dnumspills = document.createElement("dnumspills");
        dnumspills.setTextContent(String.valueOf(this.dnumspills));
        shuffleReadElement.appendChild(dnumspills);

        Element dSpillRecs = document.createElement("dSpillRecs");
        dSpillRecs.setTextContent(String.valueOf(this.dSpillRecs));
        shuffleReadElement.appendChild(dSpillRecs);

        Element dPerSpillRecs = document.createElement("dPerSpillRecs");
        dPerSpillRecs.setTextContent(String.valueOf(this.dPerSpillRecs));
        shuffleReadElement.appendChild(dPerSpillRecs);

        Element dSpillBufferSize = document.createElement("dSpillBufferSize");
        dSpillBufferSize.setTextContent(String.valueOf(this.dSpillBufferSize));
        shuffleReadElement.appendChild(dSpillBufferSize);

        Element dSpillFileSize = document.createElement("dSpillFileSize");
        dSpillFileSize.setTextContent(String.valueOf(this.dSpillFileSize));
        shuffleReadElement.appendChild(dSpillFileSize);

        Element cMergeReadTime = document.createElement("cMergeReadTime");
        cMergeReadTime.setTextContent(String.valueOf(this.cMergeReadTime));
        shuffleReadElement.appendChild(cMergeReadTime);

        Element cMergeSortTime = document.createElement("cMergeSortTime");
        cMergeSortTime.setTextContent(String.valueOf(this.cMergeSortTime));
        shuffleReadElement.appendChild(cMergeSortTime);

        Element cMergeCombTime = document.createElement("cMergeCombTime");
        cMergeCombTime.setTextContent(String.valueOf(this.cMergeCombTime));
        shuffleReadElement.appendChild(cMergeCombTime);


        Element dCombRecs = document.createElement("dCombRecs");
        dCombRecs.setTextContent(String.valueOf(this.dCombRecs));
        shuffleReadElement.appendChild(dCombRecs);

        Element sort = document.createElement("sort");
        sort.setTextContent(String.valueOf(this.sort));
        shuffleReadElement.appendChild(sort);

        Element cSortTime = document.createElement("cSortTime");
        cSortTime.setTextContent(String.valueOf(this.cSortTime));
        shuffleReadElement.appendChild(cSortTime);

        Element objectSize = document.createElement("objectSize");
        objectSize.setTextContent(String.valueOf(this.objectSize));
        shuffleReadElement.appendChild(objectSize);


        return shuffleReadElement;
    }
}
