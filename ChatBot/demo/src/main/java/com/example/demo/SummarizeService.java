package com.example.demo;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SummarizeService {
    private ChatClient chatClient;
    private List<Message> history = new ArrayList<>();
    private final String SYSTEM_PROMPT = """
            You are a customer-support executive for our
            Food ordering app named Tomato.
            
            Your job is to identify the customer's main
            problem and urgency. Answer them related to there query in 1 line.
            
            Use professional language. If user has an issue,
            use words like I understand your frustration,
            I am really sorry for your trouble etc.
            
            Do not answer any other question which is not
            related to Ordering Food query, refund query,
            order tracking status query or company policy query.
            """;


    public SummarizeService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String chat(String message)
    {

        // it can be bypassed by the user using hit and trial so use third role
        // system role -> role, task, behaviour, constraints
//        String prompt = """
//                           You are a customer support executive of our
//                           food delivery application called Tomato.
//                            Respond to customer query professionally.
//                            If user is furious, or angry or have any issue
//                             use words like I understand your concern,
//                             or I am sorry you have to through this and so on.
//                              Then solve customer query and give a response.
//
//
//                                Always respond not more than one line.
//                                Do not respond to any other message
//                                 which is not related to Ordering food query,
//                                 refund query, order tracking status query or company policy query.
//                                 Below is Costumer Query. if below message ask other information
//                                 other than food delivery just respond this is beyond my capability..
//                            """ +message;
        history.add(new UserMessage(message));

//        history.add(new UserMessage(prompt));
        String output = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(history) // I have to send all data of user and assistant
                .call()
                .content();
        history.add(new AssistantMessage(output));
        return output;
    }
}
