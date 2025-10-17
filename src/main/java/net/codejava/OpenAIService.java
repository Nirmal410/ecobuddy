package net.codejava;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OpenAIService {
    
    @Value("${openai.api.key}")
    private String apiKey;
    
    @Value("${openai.model:gpt-4}")
    private String model;
    
    public Map<String, String> generateProductAnalysis(String normalProduct, String ecoAlternative) {
        Map<String, String> analysis = new HashMap<>();
        
        try {
            OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(60));
            
            // Generate pros of eco-friendly product
            String prosPrompt = String.format(
                "List 3-5 key advantages of using '%s' (eco-friendly product) instead of '%s' (normal product). " +
                "Focus on environmental benefits, health benefits, and long-term cost savings. " +
                "Format as bullet points. Be concise and specific.",
                ecoAlternative, normalProduct
            );
            
            List<ChatMessage> prosMessages = new ArrayList<>();
            prosMessages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), 
                "You are an environmental expert helping people understand the benefits of eco-friendly products."));
            prosMessages.add(new ChatMessage(ChatMessageRole.USER.value(), prosPrompt));
            
            ChatCompletionRequest prosRequest = ChatCompletionRequest.builder()
                .model(model)
                .messages(prosMessages)
                .maxTokens(300)
                .temperature(0.7)
                .build();
            
            String pros = service.createChatCompletion(prosRequest)
                .getChoices().get(0).getMessage().getContent();
            
            // Generate cons of normal product
            String consPrompt = String.format(
                "List 3-5 key disadvantages of using '%s' (normal product) compared to eco-friendly alternatives. " +
                "Focus on environmental damage, health concerns, and waste issues. " +
                "Format as bullet points. Be concise and specific.",
                normalProduct
            );
            
            List<ChatMessage> consMessages = new ArrayList<>();
            consMessages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), 
                "You are an environmental expert helping people understand the negative impacts of conventional products."));
            consMessages.add(new ChatMessage(ChatMessageRole.USER.value(), consPrompt));
            
            ChatCompletionRequest consRequest = ChatCompletionRequest.builder()
                .model(model)
                .messages(consMessages)
                .maxTokens(300)
                .temperature(0.7)
                .build();
            
            String cons = service.createChatCompletion(consRequest)
                .getChoices().get(0).getMessage().getContent();
            
            // Generate suggestion
            String suggestionPrompt = String.format(
                "Provide a brief, encouraging suggestion (2-3 sentences) for someone considering switching from '%s' to '%s'. " +
                "Make it personal and motivating.",
                normalProduct, ecoAlternative
            );
            
            List<ChatMessage> suggestionMessages = new ArrayList<>();
            suggestionMessages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), 
                "You are a friendly sustainability coach encouraging eco-friendly choices."));
            suggestionMessages.add(new ChatMessage(ChatMessageRole.USER.value(), suggestionPrompt));
            
            ChatCompletionRequest suggestionRequest = ChatCompletionRequest.builder()
                .model(model)
                .messages(suggestionMessages)
                .maxTokens(200)
                .temperature(0.8)
                .build();
            
            String suggestion = service.createChatCompletion(suggestionRequest)
                .getChoices().get(0).getMessage().getContent();
            
            analysis.put("pros", pros);
            analysis.put("cons", cons);
            analysis.put("suggestion", suggestion);
            
            service.shutdownExecutor();
            
        } catch (Exception e) {
            log.error("Error calling OpenAI API: ", e);
            analysis.put("pros", "• Environmentally friendly\n• Reduces waste\n• Sustainable choice");
            analysis.put("cons", "• Creates pollution\n• Not biodegradable\n• Harmful to environment");
            analysis.put("suggestion", "Making the switch to eco-friendly alternatives is a great step towards a sustainable future!");
        }
        
        return analysis;
    }
}
