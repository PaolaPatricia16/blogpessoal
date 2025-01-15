package com.generation.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Tb_temas")
public class Tema {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // long = bigint
	
	@NotNull(message = "O Atributo Descrição é obrigatório")
	private String descricao; // string = varchar

	// Essa anotação indica que esta Classe é o lado '1' 
	// ela vai ter uma List contendo os Objetos da Classe Postagens
	// Dada a relação BIDIRECIONAL serão carregados os dados de Postagens + tb_temas
	@OneToMany (fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE)
	// fecth = tipo da busca, no caso é a Lazy(preguiçosa)
	//
	// mappedBy = faz a relação BIDIRECIONAL, e indica qual o atributo que esta na Classe Postagens armazenando os dados da PK daqui
	// 'tema' foi o atributo que criamos em Postagens, ela representa a FK
	// Faz o link entre as classes --> Tema.postagem = Postagens.tema
	// 
	// cascade = efeito Cascata, oque acontece na CLasse Proprietária é replicado para a Classe Dependente.
	// CascadeType.REMOVE = se excluirmos um tema e este tema possuir postagens, elas tabmém serão deletadas.
	
	@JsonIgnoreProperties("tema")
	// Ignora o atributo que esta " ", para que a busca não caia em loop infinito
	private List<Postagens> postagem;
	// Lis postagem = irá armazenar todos os Objetos da Classe Postagens
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Postagens> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagens> postagem) {
		this.postagem = postagem;
	}
	



}
