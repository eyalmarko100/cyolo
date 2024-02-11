package com.cyolo.frequentword.sorters;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamSortMapSorter implements MapSorter {
    @Override
    public List<Map.Entry<String, Integer>> sortMap(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}

