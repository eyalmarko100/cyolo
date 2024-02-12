package com.cyolo.frequentword.sorters;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
/*
  Java supply very good algorithm for sorting : it uses processor of the machine
  divide the array into small sub-arrays and merge them in the end
  however, for small data set it's not justify to use it - that's why I gave the option to the user to decide
  which way to use - this one or the simpler one (stream)
 */
public class ParallelSortMapSorter implements MapSorter {

    @Override
    public List<Map.Entry<String, Integer>> sortMap(Map<String, Integer> map) {
        Map.Entry<String, Integer>[] entries = map.entrySet().toArray(new Map.Entry[0]);
        Arrays.parallelSort(entries, Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return Arrays.asList(entries);
    }
}

