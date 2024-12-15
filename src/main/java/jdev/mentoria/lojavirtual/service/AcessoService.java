package jdev.mentoria.lojavirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.repository.AcessoRepository;

@Service
public class AcessoService {
	
	@Autowired
	private AcessoRepository acessoRepository;  //injeção de dependencia
	
	public Acesso save(Acesso acesso) {
		return acessoRepository.save(acesso); //chamando diretamente o método save que já está implementado dentro do JpaRepository. Esse método é uma das funcionalidades prontas que o Spring Data JPA fornece.
		
	}
}
