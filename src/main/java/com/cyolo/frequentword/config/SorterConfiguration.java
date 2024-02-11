package com.cyolo.frequentword.config;

import com.cyolo.frequentword.sorters.MapSorter;
import com.cyolo.frequentword.sorters.ParallelSortMapSorter;
import com.cyolo.frequentword.sorters.StreamSortMapSorter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class SorterConfiguration {

    @Bean
    @Profile("parallelSort")
    public MapSorter parallelSortMapSorter() {
        return new ParallelSortMapSorter();
    }

    @Bean
    @Profile("streamSort")
    public MapSorter streamSortMapSorter() {
        return new StreamSortMapSorter();
    }
}

