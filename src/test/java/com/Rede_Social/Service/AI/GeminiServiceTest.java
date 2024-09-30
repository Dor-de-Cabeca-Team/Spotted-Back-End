package com.Rede_Social.Service.AI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {
        "gemini.key=AIzaSyC38UYkmMwoKia2elAf-ugTls8HWZNKm0A",
        "gemini.url=https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent"
})
class GeminiServiceTest {

    @Autowired
    GeminiService geminiService;

    @MockBean
    RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(geminiService, "restTemplate", restTemplate);
    }


    @Test
    void testValidadeAI_NotOffensive() {
        String responseJson = "{\"candidates\":[{\"content\":{\"parts\":[{\"text\":\"1\"}]},\"safetyRatings\":[{\"category\":\"HARM_CATEGORY_HATE_SPEECH\",\"probability\":\"NEGLIGIBLE\"},{\"category\":\"HARM_CATEGORY_HARASSMENT\",\"probability\":\"NEGLIGIBLE\"}]}]}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseJson, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        boolean result = geminiService.validadeAI("Hello, world!");
        assertTrue(result);
    }

    @Test
    void testValidadeAI_Offensive() {
        String responseJson = "{\"candidates\":[{\"content\":{\"parts\":[{\"text\":\"1\"}]},"
                + "\"safetyRatings\":[{\"category\":\"HARM_CATEGORY_HATE_SPEECH\",\"probability\":\"HIGH\"}]}]}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseJson, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);
        boolean result = geminiService.validadeAI("Discurso de odio");
        assertFalse(result);
    }

    @Test
    void testValidadeAI_APIFailure() {
        ResponseEntity<String> errorResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(errorResponse);

        assertThrows(RuntimeException.class, () -> geminiService.validadeAI("Test input"));
    }

    @Test
    void testExtractResponseFromJson_ValidResponse() {
        String json = "{\"candidates\":[{\"content\":{\"parts\":[{\"text\":\"1\"}]},\"safetyRatings\":[{\"category\":\"HARM_CATEGORY_HATE_SPEECH\",\"probability\":\"NEGLIGIBLE\"},{\"category\":\"HARM_CATEGORY_HARASSMENT\",\"probability\":\"NEGLIGIBLE\"}]}]}";
        String result = ReflectionTestUtils.invokeMethod(geminiService, "extractResponseFromJson", json);
        assertEquals("1", result);
    }

    @Test
    void testExtractResponseFromJson_OffensiveResponse() {
        String json = "{\"candidates\":[{\"content\":{\"parts\":[{\"text\":\"1\"}]},\"safetyRatings\":[{\"category\":\"HARM_CATEGORY_HATE_SPEECH\",\"probability\":\"MEDIUM\"},{\"category\":\"HARM_CATEGORY_HARASSMENT\",\"probability\":\"LOW\"}]}]}";
        String result = ReflectionTestUtils.invokeMethod(geminiService, "extractResponseFromJson", json);
        assertEquals("0", result);
    }

    @Test
    void testExtractResponseFromJson_InvalidJson() {
        String invalidJson = "{\"invalid\":\"json\"}";
        assertThrows(RuntimeException.class, () -> ReflectionTestUtils.invokeMethod(geminiService, "extractResponseFromJson", invalidJson));
    }


    @Test
    void testValidadeAI_Integration_NotOffensive() {
        String responseJson = "{\"candidates\":[{\"content\":{\"parts\":[{\"text\":\"1\"}]},\"safetyRatings\":[{\"category\":\"HARM_CATEGORY_HATE_SPEECH\",\"probability\":\"NEGLIGIBLE\"},{\"category\":\"HARM_CATEGORY_HARASSMENT\",\"probability\":\"NEGLIGIBLE\"}]}]}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseJson, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        boolean result = geminiService.validadeAI("Hello, world!");
        assertTrue(result);
    }

    @Test
    void testValidadeAI_Integration_Offensive_HateSpeech() {
        String responseJson = "{\"candidates\":[{\"content\":{\"parts\":[{\"text\":\"1\"}]},\"safetyRatings\":[{\"category\":\"HARM_CATEGORY_HATE_SPEECH\",\"probability\":\"MEDIUM\"},{\"category\":\"HARM_CATEGORY_HARASSMENT\",\"probability\":\"LOW\"}]}]}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseJson, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        boolean result = geminiService.validadeAI("Offensive hate speech content");
        assertFalse(result);
    }

    @Test
    void testValidadeAI_Integration_APIFailure() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(RuntimeException.class, () -> geminiService.validadeAI("Test input"));
    }
}