package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.blogpessoal.model.Postagens;

// o repository equivale a interface em POO
// Herança - recebe 2 parametros
public interface PostagemRepository extends JpaRepository<Postagens, Long>{
	
	// Equivale = SELECT * FROM tb_postagens WHERE titulo LIKE "%titulo%";
	public List<Postagens> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);
					// find = select
					// all = *
					// by = where
					// titulo = atributo da classe postagem
					// containing = like
					// @Param = essa anotação é obrigatória quando usamos 'like'


}
