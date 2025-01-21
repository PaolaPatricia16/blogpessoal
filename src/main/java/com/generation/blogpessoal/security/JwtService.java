package com.generation.blogpessoal.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component // pode injetar e instanciar qualquer dependencia que for especificada na implementação da classe
public class JwtService {
	// é uma Constante da classe, de valor fixo. Armazena a chave de assinatura do Token
	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

	private Key getSignKey() { // codifica a 'SECRET' EM bASE 64
	// 'Key' define as 3 caracterisitcas que todo objeto key possui
	// Algoritmo, Codificação e Formato
		byte[] keyBytes = Decoders.BASE64.decode(SECRET); // 'keybytes': vetor do tipo 'byte', recebe a codificação
		return Keys.hmacShaKeyFor(keyBytes); // codifica a chave do token em HMac SH256
	}

	private Claims extractAllClaims(String token) { // trás todas as 'claims'(inputs) inseridas da Payload(corpo da requisição)
		return Jwts.parserBuilder() //cria uma instanci da Interface JWT
				.setSigningKey(getSignKey()).build() // método da interface, que verificar se a assinatura do token é valida
				.parseClaimsJws(token).getBody(); // se for valida, retorna extrais todas as claims e retorna oq encontrou
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { // retorna uma claim específica
		final Claims claims = extractAllClaims(token); // cria um obj da Interface Claims que rece todas as claims encontradas
		return claimsResolver.apply(claims);
	}

	public String extractUsername(String token) { // recupera os daddos da claim, usuário/e´mail
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) { //recupera dados da claim exp, data e hora de expiração do token
		return extractClaim(token, Claims::getExpiration);
	}

	private Boolean isTokenExpired(String token) { // recupaera dados da clais exp, data e horario do token
		return extractExpiration(token).before(new Date()); // 'before': verifica se o token esta ou nao expirado
	}

	public Boolean validateToken(String token, UserDetails userDetails) { // valida se o token pertence ao usuario que enviou
		final String username = extractUsername(token); // retorna a claim sub, que contem o usuário autenticado(email)
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // retorna true se o usuario da claim = usuario autenticado
	}

	private String createToken(Map<String, Object> claims, String userName) { // cria o token
		return Jwts.builder() // 'builder':  cria o token com base nos dados abaixo
					.setClaims(claims) // insere as claims no payload
					.setSubject(userName) // insere a claim sub, preenchida com o usuario(email)
					.setIssuedAt(new Date(System.currentTimeMillis())) // insere a claim iat, preenchida com data e hora da criação do token
					.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))// insere a claim expiration, preenchida com data e hora
					// da criação do token + o tempo limite de validade do token, neste exemplo 60min
					.signWith(getSignKey(), SignatureAlgorithm.HS256).compact(); // insere a assinatura do token
	}

	public String generateToken(String userName) { // gera um novo token com o usuario(email). Utilizado na Class UsuarioService
		Map<String, Object> claims = new HashMap<>();  // collection map vazia, sera enviada como parametro no metodo createToken
		return createToken(claims, userName); // retorna a execução do createToken. 
		// Cria o token com o formato: 'header'.'payload'.'signature'
	}

}