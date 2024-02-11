package com.cyolo.frequentword.util;

import com.cyolo.frequentword.dto.WordNameValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;

public class WordHandlerUtil {

    public static List<String> convertCSVStringToList(String csvString) {

        String[] items = csvString.split(",");
        return Arrays.asList(items);

    }

    public static List<WordNameValue> buildListOfPair(SortedMap<String, Integer> sortedMap) {
        List<WordNameValue> wordWordNameValues = new ArrayList<>();

        return wordWordNameValues;
    }
}
