package com.cyolo.frequentword.handlers;


import com.cyolo.frequentword.dto.WordNameValue;
import com.cyolo.frequentword.response.WordCacheStatsResponse;
import com.cyolo.frequentword.sorters.MapSorter;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
public class WordSortedHandler {

    private static final Logger logger = LoggerFactory.getLogger(WordSortedHandler.class);
    private final Queue<String> wordQueue;

    private final Map<String, Integer> wordMap = new HashMap<>();

    // flag to indicate that processing is running - in case of queue is reaching the threshold  we set
    //  and periodic task is also processing than wait
    private final AtomicBoolean isProcessing = new AtomicBoolean(false);

    // can be configurable from outside
    @Value("${topNWords}")
    private int topNWords;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private WordCacheStatsResponse wordCacheStatsResponse;

    private final MapSorter mapSorter;

    public WordSortedHandler(Queue<String> wordQueue, MapSorter mapSorter) {
        this.wordQueue = wordQueue;
        this.mapSorter = mapSorter;
        this.wordCacheStatsResponse = new WordCacheStatsResponse();
    }

    // run periodically every n seconds
    @Scheduled(fixedDelayString = "${processing.delay.milliseconds}")
    public void processWordFrequency() {
        processQueue();

    }

    public void triggerProcessing() {
        logger.debug("triggerProcessing");
        executorService.submit(this::processQueue);
    }

    public WordCacheStatsResponse getCurrentWordStats() {
        return wordCacheStatsResponse;
    }

    private void processQueue() {

        if (isProcessing.compareAndSet(false, true)) {
            try {
                while (!wordQueue.isEmpty()) {
                    String currentWord = wordQueue.poll();
                    wordMap.put(currentWord, wordMap.getOrDefault(currentWord, 0) + 1);
                    logger.debug("size of word map is {}", wordMap.keySet().size());
                }

                List<Map.Entry<String, Integer>> sortedEntries = mapSorter.sortMap(wordMap);


                if (sortedEntries.size() > 0) {
                    processSortedEntries(sortedEntries);
                } else {
                    logger.info("queue is empty,no processing needed");
                }

            } finally {
                isProcessing.set(false);
            }

        }
    }

    private void processSortedEntries(List<Map.Entry<String, Integer>> sortedEntries) {
        updateTopNWords(sortedEntries);
        updateLeastFrequentWord(sortedEntries);
        updateMedianFrequency(sortedEntries);
    }

    private void updateTopNWords(List<Map.Entry<String, Integer>> sortedEntries) {
        if (!sortedEntries.isEmpty()) {
            List<WordNameValue> topNWordsList = sortedEntries.stream()
                    .limit(topNWords)
                    .map(e -> new WordNameValue(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
            logger.debug(wordCacheStatsResponse.toString());
            wordCacheStatsResponse.updateTopNWordsOccurrence(topNWordsList);
        }
    }

    private void updateLeastFrequentWord(List<Map.Entry<String, Integer>> sortedEntries) {
        if (!sortedEntries.isEmpty()) {
            Map.Entry<String, Integer> leastFrequent = sortedEntries.get(sortedEntries.size() - 1);
            wordCacheStatsResponse.updateLeastWordOccurrence(new WordNameValue(leastFrequent.getKey(), leastFrequent.getValue()));
        }
    }

    private void updateMedianFrequency(List<Map.Entry<String, Integer>> sortedEntries) {
        if (!sortedEntries.isEmpty()) {
            int medianIndex = sortedEntries.size() / 2;
            int medianValue = sortedEntries.size() % 2 == 0 ?
                    (sortedEntries.get(medianIndex - 1).getValue() + sortedEntries.get(medianIndex).getValue()) / 2 :
                    sortedEntries.get(medianIndex).getValue();
            wordCacheStatsResponse.updateMedianWordOccurrence(medianValue);
        }
    }


    @PreDestroy
    public void cleanUp() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    logger.error("ExecutorService did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
    }
}
