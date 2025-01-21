package com.generation.blogpessoal.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;

/*Funcionamento deste tipo de Classe:
	Na CONTROLLER temos os métodos de forma genérica, ou seja, da forma com que 
	o CRUD esta lá pode-se aplicar a qualquer sistema de qualquer área de negocio.
	A SERVICE entra para atribuir regras especificas para cada parte do CRUD,
	as Regras de Negócio da aplicação
*/

@Service // Anotação que indica que esta é uma Classe de Serviço
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired // Injeção de dependencia
	private UsuarioRepository usuarioRepository;

	@Override // Sobreescreve o Método Construtor contido na UserDetails
	public UserDetails loadUserByUsername (String userName) throws UsernameNotFoundException{
	// Implementa o método da UserDetailsService. Recebe o usuário pela tela de login
		
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(userName); // verifica se o usario informado existe na BD
		// Objeto do tipo Classe Optional para receber o retorno da Query Method '.finsBy..'
		// '.finBy..': vem da Interface usuarioRepository;
		
		if(usuario.isPresent())
			return new UserDetailsImpl(usuario.get()); // envia o objeto usuario
		else
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
	}

}
