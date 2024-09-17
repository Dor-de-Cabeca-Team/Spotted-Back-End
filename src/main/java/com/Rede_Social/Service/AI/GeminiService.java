package com.Rede_Social.Service.AI;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeminiService {

    @Value("${gemini.key}")
    private String GEMINI_KEY;

    @Value("${gemini.url}")
    private String GEMINI_URL;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean validadeAI(String userInput) {
        String prompt = "Analise a seguinte mensagem e determine se é ofensiva. Se for ofensiva, retorne o número '0'. Se não for ofensiva, retorne o número '1'. Apenas um número deve ser retornado sem qualquer explicação adicional. Mensagem: " + userInput;
        System.out.println("Prompt enviado: " + prompt);
        String jsonPayload = String.format("{\"contents\":[{\"role\": \"user\",\"parts\":[{\"text\": \"%s\"}]}]}", prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("x-goog-api-key", GEMINI_KEY);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(GEMINI_URL, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Resposta recebida: " + response.getBody());
            String result = extractResponseFromJson(response.getBody());

            System.out.println("Resultado extraído: " + result);

            return "1".equals(result);
        } else {
            throw new RuntimeException("Failed to call API: " + response.getStatusCode());
        }
    }

//    private String extractResponseFromJson(String responseBody) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode rootNode = objectMapper.readTree(responseBody);
//            JsonNode textNode = rootNode.path("candidates").get(0)
//                    .path("content")
//                    .path("parts").get(0)
//                    .path("text");
//
//            return textNode.asText();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to parse API response", e);
//        }
//    }

    private String extractResponseFromJson(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode candidatesNode = rootNode.path("candidates").get(0);
            JsonNode safetyRatingsNode = candidatesNode.path("safetyRatings");

            // Verifica se há discurso de ódio ou assédio com probabilidade "MEDIUM" ou superior
            for (JsonNode rating : safetyRatingsNode) {
                String category = rating.path("category").asText();
                String probability = rating.path("probability").asText();

                if ((category.equals("HARM_CATEGORY_HATE_SPEECH") || category.equals("HARM_CATEGORY_HARASSMENT"))
                        && (probability.equals("MEDIUM") || probability.equals("HIGH"))) {
                    // Mensagem é ofensiva
                    return "0";
                }
            }
            // Caso não tenha encontrado nada ofensivo, retorna 1 (não ofensivo)
            return "1";
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse API response", e);
        }
    }

}
