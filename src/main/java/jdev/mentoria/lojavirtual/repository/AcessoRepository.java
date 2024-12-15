package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jdev.mentoria.lojavirtual.model.Acesso;

@Repository
@Transactional
public interface AcessoRepository extends JpaRepository<Acesso, Long> {

	@Query("select a from Acesso a where upper(trim(a.descricao)) like %?1%")
	List<Acesso> buscarAcessoDesc(String desc); // a String desc será exatamente o "ALUNO" passado no teste de query dentro do arquivo de testes.

}

//JpaRepository é uma interface do Spring Data JPA que fornece métodos prontos para interagir com o banco de dados
//(como save(), findById(), findAll(), entre outros). 

//Ao estender JpaRepository, o AcessoRepository herda todos esses métodos automaticamente,
//sem precisar implementar nenhum código adicional para essas operações. 

