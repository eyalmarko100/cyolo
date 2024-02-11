package com.cyolo.frequentword;

import com.cyolo.frequentword.dto.WordNameValue;
import com.cyolo.frequentword.handlers.WordSortedHandler;
import com.cyolo.frequentword.response.WordCacheStatsResponse;
import com.cyolo.frequentword.services.WordProcessorService;
import com.cyolo.frequentword.util.WordHandlerUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FrequentedApplicationIntegrationTests {


    @Autowired
    private WordProcessorService wordProcessorService;

   // @Test
    public void processWordFrequencyTest() throws InterruptedException {
        String testData = "apple,banana,apple,orange,banana,banana,kiwi,apple,apple,kiwi,strawberry,melon,melon";
        wordProcessorService.addWordsToQueue(testData);
        Thread.sleep(10000);
        WordCacheStatsResponse wordCacheStatsResponse = wordProcessorService.getCurrentFrequentlyStats();

        List<WordNameValue> top5Words = wordCacheStatsResponse.getTopNWordsOccurrence();
        assertNotNull(top5Words);
        assertEquals(5, top5Words.size());
        assertEquals("apple", top5Words.get(0).getName());
        assertEquals(4, top5Words.get(0).getValue());
        assertEquals("banana", top5Words.get(1).getName());
        assertEquals(3, top5Words.get(1).getValue());
        // ... Assert for 'kiwi', 'melon', and either 'orange' or 'strawberry' ...

        // Median Frequency
        int medianWordOccurrence = wordCacheStatsResponse.getMedianWordOccurrence();
        assertEquals(2, medianWordOccurrence);


        // Least Frequent Word (either 'orange' or 'strawberry')
        WordNameValue leastWordOccurrence = wordCacheStatsResponse.getLeastWordOccurrence();
        assertNotNull(leastWordOccurrence);
      //  assertTrue(leastWordOccurrence.getName().equals("orange") || leastWordOccurrence.equals("strawberry"));
     //   assertEquals(1, leastWordOccurrence.getValue());




        System.out.println(wordCacheStatsResponse);


    }

    //@Test
    void csvInvalidTest() {

        System.out.println("testing");
        String validString = "'ab\",cd,dg,wwr";
        List<String> convertedStringList = WordHandlerUtil.convertCSVStringToList(validString);
        assertTrue(convertedStringList.contains("ab") &&
                convertedStringList.contains("ab") &&
                convertedStringList.contains("dg"));

    }

    //@Test
    void csvValidTest() {

        System.out.println("testing");
        String validString = "abcd%dg,wwr,,,nn";
        List<String> convertedStringList = WordHandlerUtil.convertCSVStringToList(validString);
        assertTrue(convertedStringList.contains("ab") &&
                convertedStringList.contains("ab") &&
                convertedStringList.contains("dg"));

    }

   // @Test
    void mapTest() {
        Queue<String> queue = new ConcurrentLinkedQueue<>();
        Map<String, Integer> wordMap = new ConcurrentHashMap<>();
        List<String> words = Arrays.asList("eyal", "Shahar", "Sharon", "Shahar");
        queue.addAll(words);
        while (!queue.isEmpty()) {
            String word = queue.poll();
            wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
        }
        System.out.println("succeed");

    }

   // @Test
    void mergeListAndSortedMapWithDistinctNamesTest() {

        Map<Integer, String> top5Names = new TreeMap<>();
        top5Names.put(2, "eyal");
        top5Names.put(1, "moshe");
        top5Names.put(8, "nahum");
        top5Names.put(10, "ayelet");
        top5Names.put(9, "shiran");
        for (Integer key : top5Names.keySet()) {
            System.out.println(top5Names.get(key));
        }


        Map<String, Integer> wordMap = buildMapFromList();


        Map.Entry<String, Integer>[] entries = wordMap.entrySet().toArray(new Map.Entry[0]);


        Arrays.parallelSort(entries, Map.Entry.comparingByValue(Comparator.reverseOrder()));


        TreeMap<Integer, String> sortedParallelMap = new TreeMap<>(Comparator.reverseOrder());
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            sortedParallelMap.put(entry.getValue(), entry.getKey());
        }


        Map<String, Integer> sortedByCount = wordMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));


        sortedByCount.forEach((key, value) -> System.out.println(key + ": " + value));
        System.out.println("succeed");

    }

    //@Test
    void sortedMapWithDistinctValueUsingParallelTest() {

        Map<String, Integer> wordMap = buildMapFromList();
        Map.Entry<String, Integer>[] entries = wordMap.entrySet().toArray(new Map.Entry[wordMap.size()]);
        Arrays.parallelSort(entries, Map.Entry.comparingByValue(Comparator.reverseOrder()));


        TreeMap<Integer, String> sortedParallelMap = new TreeMap<>(Comparator.reverseOrder());
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            sortedParallelMap.put(entry.getValue(), entry.getKey());
        }

        sortedParallelMap.forEach((key, value) -> System.out.println(key + ": " + value));

        List<String> expectedOrder = Arrays.asList("Shahar", "Sharon", "eyal"); // Example order
        //List<String> actualOrder = new ArrayList<>(sortedParallelMap.keySet());
        //assertEquals(expectedOrder, actualOrder, "The order of elements in the sorted map is incorrect.");

        System.out.println("succeed");

    }

    //@Test
    void sortedMapWithDistinctValueUsingTimSortAlgorithmTest() {

        Map<String, Integer> wordMap = buildMapFromList();

        Map<String, Integer> sortedByCount = wordMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));


        sortedByCount.forEach((key, value) -> System.out.println(key + ": " + value));
        List<String> expectedOrder = Arrays.asList("Shahar", "Sharon", "eyal"); // Example order
        List<String> actualOrder = new ArrayList<>(sortedByCount.keySet());

        assertEquals(expectedOrder, actualOrder, "The order of elements in the sorted map is incorrect.");

        System.out.println("succeed");

    }


    private void addTestDataToQueue() {
        String testData = "apple, banana, apple, orange, banana, banana, kiwi";
        wordProcessorService.addWordsToQueue(testData);
    }


    private Map<String, Integer> buildMapFromList() {
        Map<String, Integer> wordMap = new ConcurrentHashMap<>();
        List<String> words = Arrays.asList("eyal",
                "Shahar",
                "Sharon",
                "Shahar",
                "Shahar",
                "Sharon",
                "Sharon",
                "Shahar");

        for (String name : words) {

            wordMap.put(name, wordMap.getOrDefault(name, 0) + 1);
        }


        return wordMap;
    }


}
