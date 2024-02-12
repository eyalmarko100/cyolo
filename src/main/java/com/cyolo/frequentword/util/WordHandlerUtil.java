package com.cyolo.frequentword.util;

import com.cyolo.frequentword.dto.WordNameValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
/*
 Util class - usually used for handling more than one utility but for separation of concern I rather use it
 */
public class WordHandlerUtil {

    public static List<String> convertCSVStringToList(String csvString) {

        String[] items = csvString.split(",");
        return Arrays.asList(items);

    }


}
