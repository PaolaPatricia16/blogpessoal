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
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController // define que esta classe é uma Controller do tipo REST, responsável por lidar
// com as requisições HTTP para 'postagens'
@RequestMapping("/postagens") // define a url/end point que será utilizada
@CrossOrigin(origins = "*", allowedHeaders = "*") // permite que a API seja consumida ou seja, permite o recebimento de requisições internas e externas.
// CrossOrigin: "é a porta de segurança" que define quem e quais cabeçãlhos HTTP podem fazer requisições a estaue aplicações de diferentes API
// origins = "*" : o 'origins' define quem tem permissão para acessar a API, já "*" significa que qualquer origem pode acessar
// allowedHeaders: define quais cabeçalhos HTTP podem ser enviados em uma requisição
public class PostagemController {

	@Autowired // Criar e instanciar objetos fica a cargo do Spring
	private PostagemRepository postagemRepository;
	// 'postagemRepository': Objeto criado para acessar os métodos prontos da (Interface) repository (que tem extends na JPA)

	@Autowired // injeção de dependencia da Classe Tema/Tema Controller. Permite acesso a seus
				// recursos
	private TemaRepository temaRepository;

	@GetMapping // indica que o getAll vai responder todas as requisições HTTP GET que serão
				// enviadas
	public ResponseEntity<List<Postagens>> getAll() { // o método getAll = select*from tb_postagens. Que serão inseridas
														// na list
		return ResponseEntity.ok(postagemRepository.findAll()); // executa o método findAll (padrao da interface Jpa)
		// ele é o que ira trazer os dados da BD que são inseridos na List acima.
	}

	@GetMapping("/{id}") // indica que o id é uma variável que ira vir pelo http
	public ResponseEntity<Postagens> getById(@PathVariable Long id) { // o método gitById vai buscar o conteúdo da
																		// variável id
		// o "@Parth.." indica que o parametro que o getById precisa irá vir pela
		// URL/(http)

		return postagemRepository.findById(id) // o método findById = Optional (que vimos em POO - trata de exceções)
				// o resultado dessa busca será guardado no Optional

				// Verifica se há alguma "coisa" no Optional
				.map // o Map é um método do Optional = If true
				(resposta -> ResponseEntity.ok(resposta)) // resposta = retorna a busca feita pelo 'find'
				// constroí uma entidade 'resposta', passa o método 'ok' e mostra oque o foi
				// encontrado

				.orElse // = Else
				(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // build = constroí a respota negativa 404 e
																		// envia ao front
	}

	@GetMapping("/titulo/{titulo}") // {} = quando dentro de uma anotação significa que é uma variável
	// "/titulo" = o endereço do endpoint
	// {titulo} = variavel do BD. (No Insomnia iremos preencher com o texto que
	// queremos buscar).
	public ResponseEntity<List<Postagens>> getByTitulo(@PathVariable String titulo) {
		// o getBy.. = busca o conteúdo da variavel (titulo) e armazena em uma List

		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
		// o findAll... = pega o resultado do get e exibe no corpo da resposta.
		// a Lista sera criada, mesmo que vazia, por isso o status sera 200 = ok
	}

	@PostMapping // indica que este método 'post' irá responder todas as requisições HTTP POST
	// o POST = INSERT INTO tb_postagens(titulo, texto, data) VALUES ("Titulo",
	// "Texto", CURRENT_TIMESTAMP());
	public ResponseEntity<Postagens> post(@Valid @RequestBody Postagens postagem) {
		// @Valid = valida (ANTES DE GRAVAR NO BD) se dados inseridos na requisição
		// condizem com as regras que definimos anteriormente
		// quando criamos os campos da tb, o size, not null e etc
		// @RequestBody (Corpo da Requisição) = recebe o Objeto Postagens que veio da
		// requisição, no formato JSON e insere no parametro 'postagem' do método POST
		
		if (temaRepository.existsById(postagem.getTema().getId()))
		// verifica se o ID do 'tema' que se esta tentando linkar com a 'postagem' existe
		// os 2 get's são para obter o ID do 'tema'	
			return ResponseEntity.status(HttpStatus.CREATED) // executa o método do JPA que salva os dados no BD
					// save = 201 que indica que os dados foram gravados corretamente

					.body(postagemRepository.save(postagem));
		// .save = salva as informações no BD
		// .body = ele mostra no corpo da requisição as informações ao usuário antes de gravar.
		// Ex: quando fazemos o cadastro em um site e depois ele apresenta os dados que preenchemos
		// para conferirmos se esta tudo certo, e só depois de confirmar, o sistema grava os dados.

		
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
		// Se o ID não foi encontrado, executa o comando acima.
		// Ba_Request: Tratativa de execeção, código 400
		// null: indica que o motivo do erro é somente a ausencia do Id categoria buscado, ou seja
		// que não nenhuma outra exceção que esteja causando o 400. 
		
		
		// a leitura do código se da de baixo pra cima, ou seja, .body -> return
	}

	@PutMapping // indica que este método 'put' irá responder as requisições do tipo HTTP PUT
	// o PUT = UPDATE tb_postagens SET titulo = "titulo", texto = "texto", data = CURRENT_TIMESTAMP() WHERE id = id;
	public ResponseEntity<Postagens> put(@Valid @RequestBody Postagens postagem) { // ok -> 200
		if(postagemRepository.existsById(postagem.getId())) { // Verifica se o ID informado em 'postagem' existe para ser alterado
			if(temaRepository.existsById(postagem.getTema().getId())) // verifica se o ID do 'tema' existe
				return ResponseEntity.status(HttpStatus.OK) // após executar o comando abaixo, se for bem sucedida exibe o status OK -> 200
						.body(postagemRepository.save(postagem)); // save = substitui os dados trazidos da BD pelos que o usuário inseriu na request
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
			// se o ID do 'tema'  não for encontrado, envia a mensagem 400 de erro.
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		// Se o Objeto 'postagem' não for encontrado pelo método 'existsById...'
		// o build constroí a resposta negativa 404 -> Não encontrado
	}

	@ResponseStatus(HttpStatus.NO_CONTENT) // define um código de status http especifico
	// quando a operação for bem sucedida. Ele retorna um -> 204
	// usamos quando não há a necessidade de enviar uma resposta no body da requisição.
	@DeleteMapping("/{id}") // indica que este é um método DELETE e que ele irá responder todas as
							// requisições do tipo HTTP DELETE
	public void delete(@PathVariable Long id) { // tipo void, pois após deletar o registro não há oque retornar
		// @PathVariable = atribui o conteudo enviado pelo endpoint (url) na variavel
		// '{id}' no parametro 'Long id'

		Optional<Postagens> postagem = postagemRepository.findById(id);
		// Cria um objeto Optional da classe Postagens chamado postagem
		// findById = envia o resultado a 'postagem'
		// Estamos usando um Optinal, para se precaver caso o retorno seja NULL. Com
		// isso evitamos o 'NullPoint...'
		// e podemos fazer a trativa do erro antes apresentar ao usuario

		if (postagem.isEmpty()) // verifica se o Objeto 'postagem' esta vazio
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		// se estiver, envia um HTTP NOT FOUND -> 404

		postagemRepository.deleteById(id);
		// Se NÃO estiver vazio, executa o método 'deleteby'
		// depois envia o HTTP NO CONTENT 204 -> é enviado indicando que a esclusão foi
		// bem sucedida

	}

}