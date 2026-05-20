package com.fitzone.fitzonev2.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitzone.fitzonev2.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

@Service
public class ChatbotService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String groqApiUrl;

    @Value("${groq.model:llama3-8b-8192}")
    private String groqModel;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String SYSTEM_PROMPT =
            "Eres FitBot, el asistente virtual oficial de FitZone Gym. Tu rol es ayudar a los usuarios con informacion sobre el gimnasio.\n" +
            "\n" +
            "INFORMACION DEL GIMNASIO:\n" +
            "- Nombre: FitZone\n" +
            "- Descripcion: Gimnasio premium con 10 anos de experiencia, instalaciones certificadas y entrenadores profesionales.\n" +
            "- Direccion: 123 Street, New York, USA\n" +
            "- Telefono: +012 345 67890\n" +
            "- Email: fitzonegym.oficial@gmail.com\n" +
            "\n" +
            "HORARIOS DEL GIMNASIO:\n" +
            "- Lunes a Viernes: 6:00am - 10:00pm\n" +
            "- Sabados: 7:00am - 8:00pm\n" +
            "- Domingos: 8:00am - 6:00pm\n" +
            "\n" +
            "CLASES DISPONIBLES Y HORARIOS:\n" +
            "- Cardio: Lunes 6-8am, Jueves 10am-12pm, Viernes 5-7pm, Domingo 6-8am, Martes 7-9pm\n" +
            "- Crossfit: Miercoles 6-8am, Lunes 5-7pm, Sabado 6-8am, Miercoles 7-9pm, Sabado 5-7pm, Domingo 7-9pm\n" +
            "- Levantamiento de Pesas / Power Lifting: Viernes 6-8am, Martes 10am-12pm, Miercoles 5-7pm, Sabado 7-9pm\n" +
            "\n" +
            "PLANES DE MEMBRESIA:\n" +
            "- Plan Basico: Acceso a sala de fitness. Precio 50.000 COP/mes.\n" +
            "- Plan Premium: Acceso completo + clases ilimitadas + sesiones con entrenador. Precio 100.000 COP/mes.\n" +
            "- Plan Elite: Acceso VIP ilimitado 24/7 + plan nutricional + 4 sesiones entrenador/mes. Precio 150.000 COP/mes.\n" +
            "- Plan Anual: 20% de descuento pagando el ano completo.\n" +
            "\n" +
            "INSTRUCCIONES:\n" +
            "- Responde SIEMPRE en espanol.\n" +
            "- Se amigable, motivador y profesional.\n" +
            "- Si no sabes algo especifico, sugiere contactar al gimnasio directamente.\n" +
            "- Respuestas concisas (maximo 3-4 oraciones).\n" +
            "- Usa emojis ocasionalmente para ser mas amigable.\n" +
            "- No inventes informacion que no este en este contexto.\n";

    public ChatResponse chat(String userMessage) {
        try {
            // Construir JSON con Jackson
            Map<String, Object> requestMap = new LinkedHashMap<>();
            requestMap.put("model", groqModel);
            requestMap.put("temperature", 0.7);
            requestMap.put("max_tokens", 512);

            List<Map<String, String>> messages = new ArrayList<>();

            Map<String, String> systemMsg = new LinkedHashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", SYSTEM_PROMPT);
            messages.add(systemMsg);

            Map<String, String> userMsg = new LinkedHashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);
            messages.add(userMsg);

            requestMap.put("messages", messages);

            String requestBody = objectMapper.writeValueAsString(requestMap);

            // Java 11+ HttpClient — más robusto para HTTPS externo que RestTemplate
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(15))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(groqApiUrl))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + groqApiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                JsonNode choices = root.path("choices");
                if (choices.isArray() && choices.size() > 0) {
                    String content = choices.get(0).path("message").path("content").asText();
                    if (content != null && !content.isEmpty()) {
                        return new ChatResponse(content, true);
                    }
                }
                return new ChatResponse("No pude generar una respuesta. Intenta de nuevo. 🙏", false);
            } else {
                System.err.println("Groq API error " + response.statusCode() + ": " + response.body());
                return new ChatResponse("Error " + response.statusCode() + " al conectar con el asistente. Intenta de nuevo.", false);
            }

        } catch (Exception e) {
            System.err.println("ChatbotService exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            return new ChatResponse("⚠️ Error de conexion: " + e.getMessage(), false);
        }
    }
}
