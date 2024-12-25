package jdev.mentoria.lojavirtual.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {  
//herda de CrudRepository, que fornece métodos básicos de CRUD.
	
	//método pra consultar um usuário
	@Query(value = "select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);
	
//value = "select u from Usuario u where u.login = ?1": Significa "selecione o objeto Usuario (apelidado de u) onde o campo login é igual ao parâmetro passado (?1)."
//	?1: É o primeiro argumento do método, no caso, String login.
//			O método retorna o Usuario correspondente ou null se não encontrar.

}
