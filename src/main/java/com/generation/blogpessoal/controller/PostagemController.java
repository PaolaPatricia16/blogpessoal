package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagens;
import com.generation.blogpessoal.repository.PostagemRepository;


@RestController // define que esta classe é uma Controller
@RequestMapping("/postagens") //define a url que será utilizada
@CrossOrigin(origins = "*", allowedHeaders = "*") // permite que a API seja consumida
// ou seja, permite o recebimento de requisições internas e externas.
public class PostagemController {

		@Autowired //permite e injeção de dependencia.
		//Criar e instanciar objetos fica a cargo do Spring
		private PostagemRepository postagemRepository;

		
		@GetMapping // indica que o getAll vai responder todas as requisições HTTP GET que serão enviadas
		public ResponseEntity<List<Postagens>> getAll(){ // o método getAll = select*from tb_postagens. Que serão inseridas na list
			return ResponseEntity.ok(postagemRepository.findAll()); // executa o método findAll (padrao da interface Jpa)
			// ele é o que ira trazer os dados da BD que são inseridos na List acima.
		}
}
