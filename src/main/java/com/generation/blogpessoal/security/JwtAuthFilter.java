package com.generation.blogpessoal.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter { // esta classe intercepta e valida o token
// Personaliza um filtro para validar o token, e o 'OncePer..' faz com que o filtro seja executado apenas uma vez
   
	@Autowired //injeta
    private JwtService jwtService;

    @Autowired //injeta
    private UserDetailsServiceImpl userDetailsService;

    @Override // Polimorfismo - Sobrescrita
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // 'HttpServletRequest..': armazena a requisição que será analisada pelo filtro
    // 'HttpServletResponse..': a resposta da requisição http analisada pelo filtro
    // 'FilterChain..': objeto da spring security que indica qual sera o proximo filtro a ser executado.
    // 'ServletException': erro na operação dos filtros servlet.
    // 'IOException': demais erros, de entrada e saida da aplicação.
    	
    	String authHeader = request.getHeader("Authorization"); // recebe o token da "Authorization" com a palavra Bearer
        String token = null; // recebe o token SEM a palavra Bearer
        String username = null;
    
        try{
            if (authHeader != null && authHeader.startsWith("Bearer ")) { // 'starts..':verificar se o token foi encontraado
                token = authHeader.substring(7); // se verdadeiro, 'substring' tirar o 'bearer' e o 'token' recebe o token
                username = jwtService.extractUsername(token); // recebe o usuario(email)
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // autentica o usuário
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            
            }
            filterChain.doFilter(request, response); // chama o próximo filtro
         // se a validação do token falhar temos 5 exceptionss
        }catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException 
                | SignatureException | ResponseStatusException e){
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }
    }
}