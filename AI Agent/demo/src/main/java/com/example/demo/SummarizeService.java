package com.example.demo;


import com.example.demo.aitools.CalculatorTool;
import com.example.demo.aitools.CurrencyExchangeTool;
import com.example.demo.aitools.WeatherTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import org.springframework.ai.openai.OpenAiChatOptions;
import java.util.ArrayList;
import java.util.List;

@Service
public class SummarizeService {
    private ChatClient chatClient;

    private CalculatorTool calculatorTool;
    private WeatherTool weatherTool;
    private CurrencyExchangeTool currencyExchangeTool;

    private List<Message> history = new ArrayList<>();

    private final String SYSTEM_PROMPT = """
         You are a helpful AI Assistant with access to external tool.
         Follow these rules:
         1. For arithmetic calculation always use calculator tool.
         2. After receiving tool result explain the answer naturally.
         3. For current weather, ALWAYS use the currentWeather tool.
         4. For currency conversion or exchange rates, ALWAYS use the convertCurrency tool.
         5. You may call multiple tools when solving a multi-step request.
         6. After receiving tool results, explain the answer naturally.
         7. Never invent current weather or exchange-rate information.
        """;


    public SummarizeService(ChatClient.Builder builder, CalculatorTool calculatorTool,
                            WeatherTool weatherTool,
                            CurrencyExchangeTool currencyExchangeTool) {
        this.chatClient = builder.build();
        this.calculatorTool = calculatorTool;
        this.weatherTool = weatherTool;
        this.currencyExchangeTool = currencyExchangeTool;
    }

    public String chat(String message)
    {

        history.add(new UserMessage(message));

        String output = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(history) // I have to send all data of user and assistant
                .tools(calculatorTool,weatherTool,currencyExchangeTool)
                .call()
                .content();
        history.add(new AssistantMessage(output));
        return output;
    }
//public String chat(String message)
//{
//    history.add(new UserMessage(message));
//
//    String output = chatClient.prompt()
//            .system(SYSTEM_PROMPT)
//            .messages(history)
//            .tools(calculatorTool)
//            .options(OpenAiChatOptions.builder()
//                    .reasoningEffort("none")
//            )
//            .call()
//            .content();
//    history.add(new AssistantMessage(output));
//    return output;
//}


}
