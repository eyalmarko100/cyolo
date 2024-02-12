package com.cyolo.frequentword.services;


import com.cyolo.frequentword.handlers.WordSortedHandler;
import com.cyolo.frequentword.response.WordCacheStatsResponse;
import com.cyolo.frequentword.util.WordHandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@EnableAsync
public class WordProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(WordProcessorService.class);

    private final Queue<String> wordQueue;

    @Value("${queue.size.threshold}")
    private int queueSizeThreshold;


    private final WordSortedHandler wordSortedHandler;

    public WordProcessorService(Queue<String> wordQueue, WordSortedHandler wordSortedHandler) {
        this.wordQueue = wordQueue;
        this.wordSortedHandler = wordSortedHandler;
    }

    // assuming string is not too long so suppose to return in no time. otherwise use 'async' - like if
    // we want to enable accepting large files
    public void addWordsToQueue(String words) {
        // usually I validate the data , here I  skipp it ..
        logger.debug("adding words [{}] to queue",words);
        wordQueue.addAll(WordHandlerUtil.convertCSVStringToList(words));
        triggerAlertAndProcessQueue();

    }

    public WordCacheStatsResponse getCurrentFrequentlyStats() {

        return wordSortedHandler.getCurrentWordStats();

    }

    /*
     we don't want the queue to grow too much regarding memory consumption - in case of peak times
     also for monitoring  the queue ( as part of other metrics in real apps)

     *******************************************
        could be a case that the 'queuesizethreshold' is not accurate if the scheduler task already working
        and when entering the 'processqueue' the size of the queue is less than the 'queuesizethreshold'
        since it's not crucial for out app according to the policy we took - given close estimation of stats,
        it's not worth it ( in our case) to add another check or mechanism to avoid this regarding code complexity and maintenance
     *******************************************


     */
    private void triggerAlertAndProcessQueue() {
        if (wordQueue.size() >= queueSizeThreshold) {
            logger.warn(" queue reached max limit processing sorting");
            wordSortedHandler.triggerProcessing();
        }
    }


}
