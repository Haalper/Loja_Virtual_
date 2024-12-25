package jdev.mentoria.lojavirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jdev.mentoria.lojavirtual.model.Usuario;
import jdev.mentoria.lojavirtual.repository.UsuarioRepository;


@Service
public class ImplementacaoUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//metodo da interface do spring que vai ajudar a gente na autenticação na parte de jwt

		Usuario usuario = usuarioRepository.findUserByLogin(username);/* Recebe o login pra consulta */


		if (usuario == null) { //verifica se usuário não existe e caso true, mostra uma exception
			throw new UsernameNotFoundException("Usuário não foi encontrado");
		}


		return new User(usuario.getLogin(), usuario.getPassword(), usuario.getAuthorities());
		//Se o usuário for encontrado, ele é convertido em um objeto User do Spring Security, que contém: login, senha e permissões do usuário
	}


}


//Integração com UsuarioRepository:
//
//A classe usa o UsuarioRepository para buscar no banco de dados o usuário com o login fornecido.
//O método findUserByLogin do repositório é chamado para essa consulta.
//Autenticação via Spring Security:
//
//O método loadUserByUsername recebe um username (o login do usuário) e o usa para buscar o usuário no banco de dados.
//Se o usuário não for encontrado, uma UsernameNotFoundException é lançada.
//Se o usuário for encontrado, ele é convertido em um objeto User do Spring Security, que contém:
//Login do usuário.
//Senha do usuário.
//Permissões/autorização (usando getAuthorities do objeto Usuario).


//Assim, ImplementacaoUserDetailsService  busca o usuário e repassa os detalhes necessários ao Spring Security para autenticação e autorização.
