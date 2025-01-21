package com.generation.blogpessoal.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.blogpessoal.model.Usuario;

public class UserDetailsImpl implements UserDetails { // a interface UserDetails esta sendo implementada
// o UserDetails herda a interface Serializable
	
	private static final long serialVersionUID = 1L;
	// criamos um atributo para armazenar o Número de VERSÂO da nossa Classe.
	// A cada alteração nos ATRIBUTOS é criado um NOVO número de Classe.
	// Se aplica para que quando uma classe for carregada possamos comparar o Número de SERIALIZAÇÃO, sendo iguais o carregamento e concluido.
	// Serialização: transforma os atributos em bytes + 'serialVerionsUID' = num da Classe
	
	private String userName;
	private String password;
	private List<GrantedAuthority> authorities; 
	// 'authorities': armazena os direitos/nivel de acesso do usuário 
	
	public UserDetailsImpl(Usuario user) { // Método Construtor
		this.userName = user.getUsuario(); // '.get': Recupera o valor preenchido através da Classe Usuario
		this.password = user.getSenha(); // '.get': Recupera o valor preenchido através da Classe Usuario
	}
	
	
	//Polimorfismos do Método Construtor acima
	public UserDetailsImpl() {		
	}
	
	@Override // Sobreescreve o Método Construtor contido na UserDetails
	// como não implementamos essa função, a Collection sempre irá retornar vazia
	public Collection<? extends GrantedAuthority> getAuthorities(){ // 'get': trás os direitos de acesso do usuario
		return authorities;
	}
	
	@Override // Sobreescreve o Método contido na UserDetails
	public String getPassword() { // 'get': retorna o valor de password
		return password;
	}
	
	@Override // Sobreescreve o Método contido na UserDetails
	public String getUsername() { // 'get': retorna o valor de username 
		return userName;
	}
	 
	@Override // Sobreescreve o Método contido na UserDetails - Propriedade do usuário
	public boolean isAccountNonExpired() { // Indica se o acesso do usuário expirou
		return true; // permite autenticação
	}
	
	@Override // Sobreescreve o Método contido na UserDetails - Propriedade do usuário
	public boolean isAccountNonLocked() { // Indica se o usuário esta bloqueado ou desbloqueado
		return true; // permite autenticação
	}
	
	@Override // Sobreescreve o Método contido na UserDetails - Propriedade do usuário
	public boolean isCredentialsNonExpired() { // Indica se as credencias/senha do usuario expiraram
		return true; // permite autenticação
	}
	
	@Override // Sobreescreve o Método contido na UserDetails - Propriedade do usuário
	public boolean isEnabled() { // Indica se o usua´rio está habilitado ou desabilitado
		return true;
	}
	
	
}

