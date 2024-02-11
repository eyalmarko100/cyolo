package com.cyolo.frequentword.sorters;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ParallelSortMapSorter implements MapSorter {

    @Override
    public List<Map.Entry<String, Integer>> sortMap(Map<String, Integer> map) {
        Map.Entry<String, Integer>[] entries = map.entrySet().toArray(new Map.Entry[0]);
        Arrays.parallelSort(entries, Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return Arrays.asList(entries);
    }
}

