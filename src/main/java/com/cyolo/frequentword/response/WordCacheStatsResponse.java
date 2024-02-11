package com.cyolo.frequentword.response;


import com.cyolo.frequentword.dto.WordNameValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordCacheStatsResponse {

    private List<WordNameValue> topNWordsOccurrence;
    private WordNameValue leastWordOccurrence;
    private AtomicInteger medianWordOccurrence = new AtomicInteger();



    public WordCacheStatsResponse(List<WordNameValue> top5WordsOccurrence, WordNameValue leastWordOccurrence, int medianWordOccurrence){
        this.topNWordsOccurrence =top5WordsOccurrence;
        this.leastWordOccurrence=leastWordOccurrence;
        this.medianWordOccurrence.set(medianWordOccurrence);
    }

    public synchronized  void updateTopNWordsOccurrence(List<WordNameValue> updatedList){
        topNWordsOccurrence =updatedList;
    }

    public synchronized void updateLeastWordOccurrence(WordNameValue updatedLeast){
        leastWordOccurrence=updatedLeast;
    }

    public void updateMedianWordOccurrence( int updatedMedianWordOccurrence){
        medianWordOccurrence.set(updatedMedianWordOccurrence);
    }

    public synchronized List<WordNameValue> getTopNWordsOccurrence(){

        return topNWordsOccurrence;

    }

    public synchronized WordNameValue getLeastWordOccurrence(){
        return leastWordOccurrence;
    }

    public int getMedianWordOccurrence(){
        if(medianWordOccurrence ==null){
            return -1;
        }
        return medianWordOccurrence.get();
    }

}
