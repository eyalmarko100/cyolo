package com.cyolo.frequentword.sorters;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/*
  This is the common way java supply for sorting - its good enough for small to large (but not too large) data set
  the user can evaluate it ( combining with scheduler periodic time) and decide which way to use
 */
public class StreamSortMapSorter implements MapSorter {
    @Override
    public List<Map.Entry<String, Integer>> sortMap(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}

