package com.example.demo;

import com.example.demo.aitools.WebsiteTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class WebsiteBuilderService {
    private ChatClient chatClient;
    private WebsiteTools websiteTools;



    private List<Message> history = new ArrayList<>();

    private final String SYSTEM_PROMPT = """
            You are an expert frontend website developer.
            Your job is to create complete static websites using the available tools.
            Follow these rules:
            1. Create a separate directory for every website.
            2. Create index.html file.
            3. Create style.css file.
            4. Create script.js file when JavaScript is useful.
            4.2 Add these files to a directory you created for the project 
                    and add this in generated-sites directory
            5. Build modern, beautiful and responsive websites.
            6. Use only HTML, CSS and vanilla JavaScript.
            7. Do not just return website code in your response. Actually create the files using tools.
            8. After creating the website, list the project files.
            9. Read important files again if needed and fix obvious problems.
            10. Finish only when the complete website has been created.        
        """;


    public WebsiteBuilderService(ChatClient.Builder builder,
                                 WebsiteTools websiteTools) {
        this.chatClient = builder.build();
        this.websiteTools = websiteTools;

    }

    public String generate(String message)
    {

        history.add(new UserMessage(message));

        String output = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(history)
                .tools(websiteTools)// I have to send all data of user and assistant
                .call()
                .content();
        history.add(new AssistantMessage(output));
        return output;
    }

}
