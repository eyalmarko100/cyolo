package com.cyolo.frequentword;

import com.cyolo.frequentword.dto.WordNameValue;
import com.cyolo.frequentword.response.WordCacheStatsResponse;
import com.cyolo.frequentword.services.WordProcessorService;
import com.cyolo.frequentword.util.WordHandlerUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"parallelSort","test"})
class FrequentedApplicationIntegrationTests {


    @Autowired
    private WordProcessorService wordProcessorService;

    @Test
    public void processWordFrequencyTest() throws InterruptedException {
        String testData = "apple," +
                "banana," +
                "banana," +
                "apple," +
                "orange," +
                "banana," +
                "banana," +
                "kiwi," +
                "apple," +
                "apple," +
                "apple," +
                "kiwi," +
                "strawberry," +
                "kiwi," +
                "melon," +
                "melon";
        wordProcessorService.addWordsToQueue(testData);

        wordProcessorService.addWordsToQueue(testData);
        Thread.sleep(5000);
        WordCacheStatsResponse wordCacheStatsResponse = wordProcessorService.getCurrentFrequentlyStats();

        List<WordNameValue> top3Words = wordCacheStatsResponse.getTopNWordsOccurrence();
        assertNotNull(top3Words);
        assertEquals(3, top3Words.size());

        assertEquals("apple", top3Words.get(0).getName());
        assertEquals(5, top3Words.get(0).getValue());

        assertEquals("banana", top3Words.get(1).getName());
        assertEquals(4, top3Words.get(1).getValue());

        assertEquals("kiwi", top3Words.get(2).getName());
        assertEquals(3, top3Words.get(2).getValue());


        int medianWordOccurrence = wordCacheStatsResponse.getMedianWordOccurrence();
        assertEquals(2, medianWordOccurrence);


        WordNameValue leastWordOccurrence = wordCacheStatsResponse.getLeastWordOccurrence();
        assertNotNull(leastWordOccurrence);

        assertEquals(1, leastWordOccurrence.getValue());
        assertEquals("strawberry", leastWordOccurrence.getName());


    }



}
