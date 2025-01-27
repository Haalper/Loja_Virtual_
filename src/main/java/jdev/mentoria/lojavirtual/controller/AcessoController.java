package jdev.mentoria.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExceptionMentoriaJava;
import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.repository.AcessoRepository;
import jdev.mentoria.lojavirtual.service.AcessoService;

@Controller
@RestController  //usada para criar APIs REST, onde o retorno das requisições é no formato de dados, como JSON ou XML
public class AcessoController {

	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository; //injecao de dependencia para ser usada no delete
	
	@ResponseBody   //indica que o retorno de um método será enviado diretamente no formato de dados (como JSON
	@PostMapping(value = "**/salvarAcesso") //mapeando a url para receber um json
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) throws ExceptionMentoriaJava {  //O método será chamado sempre que houver uma requisição HTTP POST para a URL especificada. Ele recebe um JSON que será convertido em um objeto Acesso através da anotação @RequestBody.
		
		if (acesso.getId() == null) { //Quando o ID é null, significa que o objeto acesso ainda não foi salvo no banco de dados (ou seja, é um novo registro).
			  List<Acesso> acessos = acessoRepository.buscarAcessoDesc(acesso.getDescricao().toUpperCase());
			  
			  if (!acessos.isEmpty()) { //Se a lista não estiver vazia, significa que já existem registros no banco de dados com a mesma descrição.
				  throw new ExceptionMentoriaJava("Já existe Acesso com a descrição: " + acesso.getDescricao());
			  }
			}
		//Essas linhas verificam se o acesso que está sendo salvo no banco de dados é novo ou uma atualização,
		//e previnem a duplicação de registros com a mesma descrição, lançando uma exceção personalizada.

		
		Acesso acessoSalvo = acessoService.save(acesso); //o Json convertido em um objeto Acesso de nome acesso agora é passado para o método save do AcessoService. 
		//Esse método salva o objeto no banco de dados e retorna o objeto salvo, que é armazenado na variável acessoSalvo.
		
		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
		// Retorna uma resposta HTTP com o objeto 'acessoSalvo' como corpo e o status HTTP '200 OK'

	}
	
	@ResponseBody  
	@PostMapping(value = "**/deleteAcesso") 
	public ResponseEntity<Acesso> deleteAcesso(@RequestBody Acesso acesso) {
		
		acessoRepository.deleteById(acesso.getId()); //Usa o repositório para deletar o objeto correspondente ao id recebido.

		
		return new ResponseEntity("Acesso Removido",HttpStatus.OK);

	}
	
	//@Secured({ "ROLE_GERENTE", "ROLE_ADMIN" })    //só pode usar o método se o usuário tiver nível de gerente ou admin
	@ResponseBody
	@DeleteMapping(value = "**/deleteAcessoPorId/{id}")
	public ResponseEntity<?> deleteAcessoPorId(@PathVariable("id") Long id) { 
		
		acessoRepository.deleteById(id);
		
		return new ResponseEntity("Acesso Removido",HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/obterAcesso/{id}")
	public ResponseEntity<Acesso> obterAcesso(@PathVariable("id") Long id) throws ExceptionMentoriaJava { 
		
		Acesso acesso = acessoRepository.findById(id).orElse(null);
		
		if (acesso == null) {
			throw new ExceptionMentoriaJava("Não encontrou Acesso com código: " + id);
		}
		
		return new ResponseEntity<Acesso>(acesso,HttpStatus.OK);
	}
	
	
	
	@ResponseBody
	@GetMapping(value = "**/buscarPorDesc/{desc}")
	public ResponseEntity<List<Acesso>> buscarPorDesc(@PathVariable("desc") String desc) { 
		
		List<Acesso> acesso = acessoRepository.buscarAcessoDesc(desc.toUpperCase());
		
		return new ResponseEntity<List<Acesso>>(acesso,HttpStatus.OK);
	}

}
