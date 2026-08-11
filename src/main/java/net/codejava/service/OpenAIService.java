package net.codejava.service;

import net.codejava.entity.*;
import net.codejava.repository.*;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    
    public ProductMapping generateNewProductMapping(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getFallbackProductMapping("Eco Product");
        }
        String cleanKw = keyword.trim();

        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.contains("your-key") || apiKey.startsWith("sk-proj-sxz")) {
            return getFallbackProductMapping(cleanKw);
        }

        try {
            OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(60));
            String prompt = String.format(
                "Find an eco-friendly product alternative for the search query '%s'. " +
                "Respond with ONLY a raw valid JSON object (no markdown formatting, no ```json) with these exact keys:\n" +
                "{\n" +
                "  \"normalProduct\": \"Conventional product name\",\n" +
                "  \"ecoAlternative\": \"Eco-friendly alternative name\",\n" +
                "  \"description\": \"1-2 sentence description of why it is sustainable\",\n" +
                "  \"normalPrice\": 4.99,\n" +
                "  \"ecoPrice\": 12.99,\n" +
                "  \"category\": \"Kitchen | Bathroom | Personal Care | Lifestyle | Food | Electronics\",\n" +
                "  \"co2SavedPerUnit\": 1.5,\n" +
                "  \"plasticSavedPerUnit\": 0.4,\n" +
                "  \"pros\": \"<ul><li>...</li></ul>\",\n" +
                "  \"cons\": \"<ul><li>...</li></ul>\",\n" +
                "  \"suggestion\": \"Short encouraging suggestion\"\n" +
                "}",
                cleanKw
            );

            List<ChatMessage> messages = new ArrayList<>();
            messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a product researcher specializing in sustainable, eco-friendly product alternatives. Respond ONLY with raw JSON."));
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), prompt));

            ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(messages)
                .maxTokens(600)
                .temperature(0.7)
                .build();

            String content = service.createChatCompletion(request)
                .getChoices().get(0).getMessage().getContent().trim();

            service.shutdownExecutor();

            if (content.startsWith("```json")) {
                content = content.substring(7);
            } else if (content.startsWith("```")) {
                content = content.substring(3);
            }
            if (content.endsWith("```")) {
                content = content.substring(0, content.length() - 3);
            }
            content = content.trim();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(content);

            ProductMapping p = new ProductMapping();
            p.setNormalProduct(json.has("normalProduct") ? json.get("normalProduct").asText() : capitalize(cleanKw));
            p.setEcoAlternative(json.has("ecoAlternative") ? json.get("ecoAlternative").asText() : "Eco " + capitalize(cleanKw));
            p.setDescription(json.has("description") ? json.get("description").asText() : "Sustainable alternative for " + cleanKw);
            p.setNormalPrice(json.has("normalPrice") ? json.get("normalPrice").asDouble(4.99) : 4.99);
            p.setEcoPrice(json.has("ecoPrice") ? json.get("ecoPrice").asDouble(14.99) : 14.99);
            p.setCategory(json.has("category") ? json.get("category").asText() : "Lifestyle");
            p.setCo2SavedPerUnit(json.has("co2SavedPerUnit") ? json.get("co2SavedPerUnit").asDouble(1.5) : 1.5);
            p.setPlasticSavedPerUnit(json.has("plasticSavedPerUnit") ? json.get("plasticSavedPerUnit").asDouble(0.5) : 0.5);
            p.setPurchaseLink("https://www.amazon.com/s?k=" + URLEncoder.encode(p.getEcoAlternative(), StandardCharsets.UTF_8));
            p.setImageUrl(getCuratedImageUrl(p.getCategory(), cleanKw));
            p.setAiGeneratedPros(json.has("pros") ? json.get("pros").asText() : null);
            p.setAiGeneratedCons(json.has("cons") ? json.get("cons").asText() : null);
            p.setAiGeneratedSuggestion(json.has("suggestion") ? json.get("suggestion").asText() : null);

            return p;

        } catch (Throwable e) {
            log.error("Failed to generate product mapping via OpenAI for keyword '{}': {}", cleanKw, e.getMessage());
            return getFallbackProductMapping(cleanKw);
        }
    }

    public ProductMapping getFallbackProductMapping(String keyword) {
        String cleanKw = capitalize(keyword.trim());
        ProductMapping p = new ProductMapping();

        String normal = cleanKw.toLowerCase().contains("plastic") ? cleanKw : "Plastic " + cleanKw;
        String eco = "Reusable " + cleanKw.replace("Plastic ", "").replace("plastic ", "");
        
        if (cleanKw.toLowerCase().contains("bag")) {
            normal = "Single-Use Plastic Bags";
            eco = "Organic Cotton Mesh Tote Bags";
            p.setCategory("Kitchen");
            p.setImageUrl("https://images.unsplash.com/photo-1622560480605-d83c853bc5c3?w=400");
        } else if (cleanKw.toLowerCase().contains("towel") || cleanKw.toLowerCase().contains("paper") || cleanKw.toLowerCase().contains("cloth")) {
            normal = "Disposable Paper Towels";
            eco = "Unpaper Reusable Bamboo Cloths";
            p.setCategory("Kitchen");
            p.setImageUrl("https://images.unsplash.com/photo-1705248382836-3618e25706d0?w=400");
        } else {
            p.setCategory("Lifestyle");
            p.setImageUrl(getCuratedImageUrl("Lifestyle", cleanKw));
        }

        p.setNormalProduct(normal);
        p.setEcoAlternative(eco);
        p.setDescription("A high-impact, reusable, zero-waste alternative to single-use " + normal.toLowerCase() + ".");
        p.setNormalPrice(4.99);
        p.setEcoPrice(16.99);
        p.setCo2SavedPerUnit(2.2);
        p.setPlasticSavedPerUnit(0.8);
        try {
            p.setPurchaseLink("https://www.amazon.com/s?k=" + URLEncoder.encode(eco, StandardCharsets.UTF_8));
        } catch (Exception ignored) {
            p.setPurchaseLink("https://www.amazon.com");
        }

        Map<String, String> analysis = getFallbackAnalysis(normal, eco);
        p.setAiGeneratedPros(analysis.get("pros"));
        p.setAiGeneratedCons(analysis.get("cons"));
        p.setAiGeneratedSuggestion(analysis.get("suggestion"));

        return p;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String getCuratedImageUrl(String category, String keyword) {
        if ("Bathroom".equalsIgnoreCase(category)) {
            return "https://images.unsplash.com/photo-1607613009820-a29f7bb81c04?w=400";
        } else if ("Kitchen".equalsIgnoreCase(category)) {
            return "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400";
        } else if ("Electronics".equalsIgnoreCase(category)) {
            return "https://images.unsplash.com/photo-1550009158-9ebf69173e03?w=400";
        }
        return "https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=400";
    }

    public Map<String, String> generateProductAnalysis(String normalProduct, String ecoAlternative) {
        Map<String, String> analysis = new HashMap<>();
        
        // Fast-path for dummy/invalid keys to prevent unnecessary 401 exceptions
        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.contains("your-key") || apiKey.startsWith("sk-proj-sxz")) {
            return getFallbackAnalysis(normalProduct, ecoAlternative);
        }

        try {
            OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(60));
            
            // Generate pros of eco-friendly product
            String prosPrompt = String.format(
                "List 3-5 key advantages of using '%s' (eco-friendly product) instead of '%s' (normal product). " +
                "Focus on environmental benefits, health benefits, and long-term cost savings. " +
                "Format as HTML bullet points (<ul><li>...</li></ul>). Be concise and specific.",
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
                "List 3-5 key environmental damages caused by '%s' (normal single-use product) compared to eco-friendly alternatives. " +
                "Focus on plastic pollution, landfill persistence, health concerns, and waste issues. " +
                "Format as HTML bullet points (<ul><li>...</li></ul>). Be concise and specific.",
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
            
        } catch (Throwable e) {
            log.error("OpenAI API call failed or unauthorized, using fallback analysis: {}", e.getMessage());
            return getFallbackAnalysis(normalProduct, ecoAlternative);
        }
        
        return analysis;
    }

    private Map<String, String> getFallbackAnalysis(String normalProduct, String ecoAlternative) {
        Map<String, String> analysis = new HashMap<>();
        analysis.put("pros", "<ul>" +
            "<li><strong>🌿 Environmental Damage Saved:</strong> Prevents significant CO₂ emissions and plastic waste per year.</li>" +
            "<li><strong>♻️ Resource Conservation:</strong> Eliminates continuous energy and petroleum oil consumption used to manufacture single-use items.</li>" +
            "<li><strong>💰 Long-Term Money Saver:</strong> Replaces hundreds of single-use purchases over its lifetime.</li>" +
            "<li><strong>🛡️ 100% Non-Toxic & Safe:</strong> Made from sustainable, BPA-free, food-safe materials.</li>" +
            "</ul>");
        analysis.put("cons", "<ul>" +
            "<li><strong>🚨 Centuries in Landfills:</strong> Disposable " + normalProduct.toLowerCase() + " items persist in landfills for 450+ years without degrading.</li>" +
            "<li><strong>🌊 Destroys Marine Wildlife:</strong> Single-use plastic waste clogs rivers and oceans, killing sea turtles and marine life.</li>" +
            "<li><strong>🧪 Microplastic Pollution:</strong> Discarded plastic breaks down into toxic microplastic particles ingested by humans and wildlife.</li>" +
            "</ul>");
        analysis.put("suggestion", "Making the switch to " + ecoAlternative + " eliminates ongoing plastic pollution, protects ocean life, and saves you money!");
        return analysis;
    }
}
