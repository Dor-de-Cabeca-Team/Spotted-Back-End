package com.Rede_Social.Auth;

import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Entity.Enum.Role;
import com.Rede_Social.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

	private final WebClient webClient;
	private final UserRepository userRepository;

	@Value("${keycloak.auth-server-url}")
	private String keycloakServerUrl;

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.resource}")
	private String clientId;

	@Value("${keycloak.credentials.secret}")
	private String clientSecret;

	public AuthService(WebClient.Builder webClientBuilder, UserRepository userRepository) {
		this.webClient = webClientBuilder.build();
		this.userRepository = userRepository;
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
				.map(response -> {
					String accessToken = (String) response.get("access_token");
					// Sincronizar usuário ao fazer login
					String[] tokenParts = accessToken.split("\\.");
					String payload = new String(java.util.Base64.getDecoder().decode(tokenParts[1]));
					Map<String, Object> claims;
					try {
						claims = new com.fasterxml.jackson.databind.ObjectMapper().readValue(payload, Map.class);
					} catch (Exception e) {
						throw new RuntimeException("Erro ao decodificar token JWT: " + e.getMessage());
					}

					String keycloakId = (String) claims.get("sub");
					String email = (String) claims.get("email");
					String username = (String) claims.get("preferred_username");

					userRepository.findByKeycloakId(keycloakId).orElseGet(() -> {
						UserEntity user = new UserEntity(keycloakId, Role.USUARIO, username, 18, email, false);
						return userRepository.save(user);
					});

					return accessToken;
				})
				.block();
	}

	public Map<String, String> register(Register register) {
		try {
			// Verificar se o e-mail já está em uso no banco local
			if (userRepository.findByEmail(register.email()).isPresent()) {
				throw new IllegalArgumentException("E-mail já está em uso.");
			}

			// Obter token de administrador
			String adminToken = getAdminAccessToken();

			// Criar usuário no Keycloak
			Map<String, Object> user = new HashMap<>();
			user.put("username", register.nome());
			user.put("email", register.email());
			user.put("enabled", true);
			user.put("emailVerified", false);
			user.put("credentials", List.of(Map.of(
					"type", "password",
					"value", register.senha(),
					"temporary", false
			)));

			webClient.post()
					.uri(keycloakServerUrl + "/admin/realms/" + realm + "/users")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + adminToken)
					.bodyValue(user)
					.retrieve()
					.onStatus(status -> status.is4xxClientError(),
							response -> Mono.just(new ResponseStatusException(response.statusCode(), "Erro ao criar usuário no Keycloak.")))
					.toBodilessEntity()
					.block();

			// Buscar o usuário recém-criado por e-mail
			List<Map<String, Object>> users = webClient.get()
					.uri(keycloakServerUrl + "/admin/realms/" + realm + "/users?email=" + register.email())
					.header("Authorization", "Bearer " + adminToken)
					.retrieve()
					.bodyToMono(List.class)
					.block();

			if (users == null || users.isEmpty()) {
				throw new RuntimeException("Usuário não encontrado após criação.");
			}

			String keycloakId = (String) users.get(0).get("id");

			// Salvar usuário no banco local
			UserEntity userEntity = new UserEntity(
					keycloakId,
					Role.USUARIO,
					register.nome(),
					register.idade(),
					register.email(),
					false
			);
			userRepository.save(userEntity);

			// Associar o papel USUARIO no Keycloak
			assignUserRole(keycloakId, adminToken);

			return Map.of(
					"userId", keycloakId,
					"message", "Usuário registrado com sucesso"
			);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Erro ao registrar usuário: " + e.getMessage());
		}
	}

	private String getAdminAccessToken() {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("client_id", "admin-cli");
		formData.add("grant_type", "password");
		formData.add("username", "admin"); // Substitua pelo administrador real
		formData.add("password", "admin"); // Substitua pela senha real

		return webClient.post()
				.uri(keycloakServerUrl + "/realms/master/protocol/openid-connect/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData(formData))
				.retrieve()
				.onStatus(status -> status.is4xxClientError(),
						response -> Mono.just(new ResponseStatusException(response.statusCode(), "Erro ao obter token de administrador.")))
				.bodyToMono(Map.class)
				.map(response -> (String) response.get("access_token"))
				.block();
	}

	private void assignUserRole(String userId, String adminToken) {
		// Obter o ID do papel USUARIO
		Map<String, Object> role = webClient.get()
				.uri(keycloakServerUrl + "/admin/realms/" + realm + "/roles/USUARIO")
				.header("Authorization", "Bearer " + adminToken)
				.retrieve()
				.bodyToMono(Map.class)
				.block();

		if (role == null) {
			throw new RuntimeException("Papel USUARIO não encontrado.");
		}

		String roleId = (String) role.get("id");
		String roleName = (String) role.get("name");

		// Associar o papel ao usuário
		List<Map<String, String>> roleMapping = List.of(Map.of(
				"id", roleId,
				"name", roleName
		));

		webClient.post()
				.uri(keycloakServerUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + adminToken)
				.bodyValue(roleMapping)
				.retrieve()
				.onStatus(status -> status.is4xxClientError(),
						response -> Mono.just(new ResponseStatusException(response.statusCode(), "Erro ao associar papel USUARIO.")))
				.toBodilessEntity()
				.block();
	}
}