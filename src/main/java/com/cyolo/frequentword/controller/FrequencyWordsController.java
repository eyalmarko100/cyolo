package com.cyolo.frequentword.controller;


import com.cyolo.frequentword.response.WordCacheStatsResponse;
import com.cyolo.frequentword.services.WordProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FrequencyWordsController {

    @Autowired
    private WordProcessorService wordProcessor;


    @PostMapping("/add-words")
    public ResponseEntity<String> uploadFile(@RequestBody String words) {

        wordProcessor.addWordsToQueue(words);

        return new ResponseEntity<>("words were uploaded successfully", HttpStatus.OK);

    }

    @GetMapping("/words-stats")
    public ResponseEntity<WordCacheStatsResponse> getTopWords() {

        WordCacheStatsResponse wordCacheStatsResponse =wordProcessor.getCurrentFrequentlyStats();

        return new ResponseEntity<>(wordCacheStatsResponse, HttpStatus.OK);

    }
}
