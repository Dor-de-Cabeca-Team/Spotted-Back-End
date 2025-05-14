package com.Rede_Social.Auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class AuthService {

	private final WebClient webClient;

	@Value("${keycloak.auth-server-url}")
	private String keycloakServerUrl;

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.resource}")
	private String clientId;

	@Value("${keycloak.credentials.secret}")
	private String clientSecret;

	public AuthService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.build();
	}

	public String logar(Login login) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("client_id", clientId);
		formData.add("client_secret", clientSecret);
		formData.add("grant_type", "password");
		formData.add("username", login.getEmail());
		formData.add("password", login.getPassword());

		return webClient.post()
				.uri(keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData(formData))
				.retrieve()
				.onStatus(status -> status.is4xxClientError(),
						response -> Mono.just(new ResponseStatusException(response.statusCode(), "Email ou senha incorretos.")))
				.onStatus(status -> status.is5xxServerError(),
						response -> Mono.just(new ResponseStatusException(response.statusCode(), "Erro no servidor Keycloak.")))
				.bodyToMono(Map.class)
				.map(response -> (String) response.get("access_token"))
				.block();
	}
}