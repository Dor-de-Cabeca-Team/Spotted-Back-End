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
        String prompt = "Analise a seguinte mensagem e determine se é extremamente ofensiva, se há menção direta de discurso de ódio, nazismo, racismo, homofobia, misoginia, transfobia. Se a mensagem conter qualquer uma das ofensas anteriores, em grau elevado, retorne o número '0'. Caso constŕario, se não for ofensiva, retorne o número '1'. Apenas um número deve ser retornado sem qualquer explicação adicional. Mensagem: " + userInput;
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

            return "1".equals(result.trim());
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
            JsonNode contentNode = candidatesNode.path("content");
            JsonNode textNode = contentNode.path("parts").get(0).path("text");

            String result = textNode.asText();
            System.out.println("Resultado da API é" + result);

            // Retorna o resultado da API
            return result;
        } catch (Exception e) {
            return "0";
        }
    }

}