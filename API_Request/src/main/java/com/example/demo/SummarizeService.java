package com.example.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SummarizeService {
    private ChatClient chatClient;

    public SummarizeService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String summarize(String ticket)
    {
        String output = chatClient.prompt()
                .system("""
                    You are a helpful technical writer.
                    Always answer in exactly 10 numbered lines.
                    Keep each line concise.
                    Include code snippets where appropriate.
                    """)
                .user(ticket)
                .call()
                .content();

        return output;
    }
}
