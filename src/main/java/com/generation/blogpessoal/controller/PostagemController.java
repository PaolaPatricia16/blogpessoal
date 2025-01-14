package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping; //
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagens;
import com.generation.blogpessoal.repository.PostagemRepository;

import jakarta.validation.Valid;


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
		
		@GetMapping("/{id}") // indica que o id é uma variável que ira vir pelo http
		public ResponseEntity<Postagens> getById(@PathVariable Long id){ // o método gitById vai buscar o conteúdo da variável id
		// o "@Parth.." indica que o parametro que o getById precisa irá vir pela URL/(http)
			
			return postagemRepository.findById(id) // o método findById = Optional (que vimos em POO - trata de exceções)
			// o resultado dessa busca será guardado no Optional
				
				// Verifica se há alguma "coisa" no Optional
				.map // o Map é um método do Optional = If true
					(resposta -> ResponseEntity.ok(resposta)) // resposta = retorna a busca feita pelo 'find'
					// constroí uma entidade 'resposta', passa o método 'ok' e mostra oque o foi encontrado
				
				.orElse // = Else
					(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // build = constroí a respota negativa 404 e envia ao front
		}	
		
		
		@GetMapping("/titulo/{titulo}") // {} = quando dentro de uma anotação significa que é uma variável
		// "/titulo" = o endereço do endpoint
		// {titulo} = variavel do BD. (No Insomnia iremos preencher com o texto que queremos buscar).
		public ResponseEntity<List<Postagens>> getByTitulo(@PathVariable String titulo){ 
		// o getBy.. = busca o conteúdo da variavel (titulo) e armazena em uma List
			
				return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
				// o findAll... = pega o resultado do get e exibe no corpo da resposta.
				// a Lista sera criada, mesmo que vazia, por isso o status sera 200 = ok
		}
		
		
		@PostMapping // indica que este método 'post' irá responder todas as requisições HTTP POST
		// o POST = INSERT INTO tb_postagens(titulo, texto, data) VALUES ("Titulo", "Texto", CURRENT_TIMESTAMP());
		public ResponseEntity<Postagens> post(@Valid @RequestBody Postagens postagem){
		// @Valid = valida (ANTES DE GRAVAR NO BD) se dados inseridos na requisição condizem com as regras que definimos anteriormente
		// quando criamos os campos da tb, o size, not null e etc
		// @RequestBody = recebe o Objeto Postagens que veio da requisição, no formato JSON e insere no parametro 'postagem' do método POST
		
			return ResponseEntity.status(HttpStatus.CREATED) // executa o método do JPA que salva os dados no BD
			// save = 201 que indica que os dados foram gravados corretamente
			
					.body(postagemRepository.save(postagem)); 
					// .save = salva as informações no BD
					// .body = ele mostra as informações ao usuário antes de gravar.
					// Ex: quando fazemos o cadastro em um site e depois ele apresenta os dados que preenchemos
					// para conferirmos se esta tudo certo, e só depois de confirmar, o sistema grava os dados.
		
		// a leitura do código se da de baixo pra cima, ou seja, .body -> return
		}
		
		
		@PutMapping // indica que este método 'put' iraá responder as requisições do tipo HTTP PUT
		// o PUT = UPDATE tb_postagens SET titulo = "titulo", texto = "texto", data = CURRENT_TIMESTAMP() WHERE id = id;
		public ResponseEntity<Postagens> put(@Valid @RequestBody Postagens postagem){ // ok -> 200
			return postagemRepository.findById(postagem.getId()) // retorna se  o finById' localizar o id inserido pelo usuário

					.map // If true
						(resposta -> ResponseEntity.status(HttpStatus.OK)
						// pega o retorno do 'findById' e ao inves de mostrar o Objeto com os dados, ele executa a linha abaixo
						 .body(postagemRepository.save(postagem)))
						// pega os dados inseridos na requisição pelo usuário, armazena em 'postagem' 
						// e substitui os dados que estavam no Objeto Postagens (que foram trazidos do BD)
						// Após fazer a substituição, ele armazena no 'respostas' e exibe STATUS OK -> 200
					
						// save = Se o 'findById' encontrar o ID, o 'save'  atualiza TODOS os dados do registro que pertence a esse ID
						//        Se o ID não for encontrado, é criado um novo registro.
						
					.orElse // Else
						(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
						// build = constroí a respota negativa 404 e envia ao front
		}
		
		@ResponseStatus(HttpStatus.NO_CONTENT) // indica que o metodo DELETE tem um 'status http' especifico
		// quando a operação for bem sucedida. 204
		@DeleteMapping("/{id}") // indica que este é um método DELETE e que ele irá responder todas as requisições do tipo HTTP DELETE
		public void delete(@PathVariable Long id) { // tipo void, pois após deletar o registro não há oque retornar
		//@PathVariable = atribui o conteudo enviado pelo endpoint (url) na variavel '{id}' no parametro 'Long id'
			
			Optional<Postagens> postagem = postagemRepository.findById(id);
			//Cria um objeto Optional da classe Postagens chamado postagem
			//findById = envia o resultado a 'postagem'
			//Estamos usando um Optinal, para se precaver caso o retorno seja NULL. Com isso evitamos o 'NullPoint...'
			// e podemos fazer a trativa do erro antes apresentar ao usuario
			
			if(postagem.isEmpty()) // verifica se o Objeto 'postagem' esta vazio
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
				// se estiver, envia um HTTP NOT FOUND -> 404
			
			postagemRepository.deleteById(id);
			// Se NÃO estiver vazio, executa o método 'deleteby'
			// depois envia o HTTP NO CONTENT 204 -> é enviado indicando que a esclusão foi bem sucedida
		
		}
		
}