package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SummarizeController {
    @Autowired
    private SummarizeService summarizeService;

//    public SummarizeController(SummarizeService summarizeService) {
//        this.summarizeService = summarizeService;
//    }

    @PostMapping("/chat")
    public String chat(@RequestBody String message)
    {
        return summarizeService.chat(message);
    }
}
