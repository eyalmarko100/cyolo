package com.cyolo.frequentword.sorters;

import java.util.List;
import java.util.Map;

public interface MapSorter {
    List<Map.Entry<String, Integer>> sortMap(Map<String, Integer> map);
}

