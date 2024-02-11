package com.cyolo.frequentword.intreation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class WordControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private static  Map<Integer,String> fruits;//= new HashMap<>();




   // @Test
    public void testAddWordsEndpoint() throws Exception {
        String words = "apple, banana, orange";
        for(int i=0;i<100;++i){
            mockMvc.perform(post("/add-words")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content(words))
                    .andExpect(status().isOk())
                    .andExpect(content().string("words were uploaded successfully"));
            Thread.sleep(300);
        }

    }

  //  @Test
    public void testGetTopWordsEndpoint() throws Exception {
        mockMvc.perform(get("/words-stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topNWordsOccurrence").exists())
                .andExpect(jsonPath("$.leastWordOccurrence").exists())
                .andExpect(jsonPath("$.medianWordOccurrence").exists());

    }
}

