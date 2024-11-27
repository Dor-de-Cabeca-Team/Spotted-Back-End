package com.Rede_Social.Config;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

	@Value("${HASH_TOKEN}")
	private String secretKey;
	public static final SignatureAlgorithm ALGORITMO_ASSINATURA = SignatureAlgorithm.HS256;
	public static final int HORAS_EXPIRACAO_TOKEN = 48;

	public String getSecretKey() {
		return secretKey;
	}
}