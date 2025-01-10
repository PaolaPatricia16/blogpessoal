package com.generation.blogpessoal.model;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity // indica que a classe é uma tabela
@Table(name = "tb_postagens") // indica o nome da tabela no bd
public class Postagens{
	
	// as ANOTAÇOES devem estar GRUDADAS ao campo que correspondem.
	
	@Id // indica que esse campo é a PK no bd
	@GeneratedValue(strategy = GenerationType.IDENTITY) // é o auto_increment no BD
	private Long id;
	
	@NotBlank(message = "Esse campo é obrigatório") // Equivale ao Not Null, acrescentando uma msg para o usuário
	@Size(min = 5, max = 100, message = "Digite no mínimo 5 e no máximo 100 caracteres") // define o tamanho do texto para este campo
	private String titulo;
	
	@NotBlank(message = "Esse campo é obrigatório") // Equivale ao Not Null, acrescentando uma msg para o usuário
	@Size(min = 10, max = 1000, message = "Digite no mínimo 10 e no máximo 1000 caracteres") // define o tamanho do texto para este campo
	private String texto;
	
	@UpdateTimestamp // o BD fica responsável por gerar e atualizar a data automáticamente
	private LocalDateTime data;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	}
	
	
	
}