package com.generation.blogpessoal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.Postagens;

// o repository equivale a interface em POO
// Herança - recebe 2 parametros
public interface PostagemRepository extends JpaRepository<Postagens, Long>{

}
