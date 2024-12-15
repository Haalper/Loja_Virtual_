package jdev.mentoria.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) {  //O método será chamado sempre que houver uma requisição HTTP POST para a URL especificada. Ele recebe um JSON que será convertido em um objeto Acesso através da anotação @RequestBody.
		
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
}
