package jdev.mentoria.lojavirtual;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jdev.mentoria.lojavirtual.controller.AcessoController;
import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.repository.AcessoRepository;
import jdev.mentoria.lojavirtual.service.AcessoService;
import junit.framework.TestCase;

@SpringBootTest(classes = LojaVirtual2Application.class)
public class LojaVirtual2ApplicationTests extends TestCase { //TestCase, que é uma classe base fornecida pelo JUnit (ou bibliotecas relacionadas). Isso não é obrigatório no JUnit moderno, mas pode ser usado para compatibilidade ou organização.

	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@Autowired
	private AcessoController acessoController;
	
	@Test
	public void testCadastraAcesso() {
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ADMIN");
		
		assertEquals(true, acesso.getId() == null);
		// verifica se o valor de acesso.getId() é realmente null antes de realizar a operação de salvamento. Isso garante que, antes do salvamento, o objeto acesso não tem um ID, como esperado.
		
		acesso = acessoController.salvarAcesso(acesso).getBody();   //o objeto acesso com a descrição "ROLE_ADMIN" é passado como parâmetro. Esse objeto é usado pelo Controller, modificado (gerado um ID) durante o processo de salvamento no banco e retornado como resposta. 
		//O retorno de salvarAcesso é uma instância de ResponseEntity, que encapsula o objeto Acesso. O método .getBody() extrai o objeto Acesso da resposta.

		
		assertEquals(true, acesso.getId() > 0); //Aqui, o teste verifica se o ID do objeto Acesso retornado é maior que 0. Isso confirma que o objeto foi salvo no banco de dados, pois o ID foi gerado (normalmente pelo banco de dados).
		
		assertEquals("ROLE_ADMIN", acesso.getDescricao()); 
		// verifica se a descrição (ROLE_ADMIN) do objeto Acesso foi mantida corretamente após o processo de salvamento

		
		Acesso acesso2 = acessoRepository.findById(acesso.getId()).get();
		
		assertEquals(acesso.getId(), acesso2.getId());
		//esperando que o meu objeto acesso2 tenha o mesmo id do objeto acesso
		
		
		/*Teste de delete*/
		acessoRepository.deleteById(acesso2.getId());
		
		acessoRepository.flush(); /*Roda esse SQL de delete no banco de dados*/
		
		Acesso acesso3 = acessoRepository.findById(acesso2.getId()).orElse(null);
		//findById procura o acesso2 que foi apagado do banco – para não dar exceção, colocamos o orElse(null), que retornará nulo
		
		assertEquals(true, acesso3 == null);

		
		/*Teste de query*/
		acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ALUNO");
		
		acesso = acessoController.salvarAcesso(acesso).getBody();
		//O objeto Acesso com a descrição "ROLE_ALUNO" é salvo no banco de dados.
	
		List<Acesso> acessos = acessoRepository.buscarAcessoDesc("ALUNO".trim().toUpperCase());
		//chama o método buscarAcessoDesc, que verifica se existe, no banco de dados, algum registro na tabela Acesso cujo campo descrição (descricao) contenha a palavra "ALUNO"
		
		assertEquals(1, acessos.size());
		//validação - O teste valida se apenas um objeto foi retornado pela consulta, indicando que a busca funcionou conforme esperado.
		
		acessoRepository.deleteById(acesso.getId());
		//é uma boa prática em testes, pois garante que os dados temporários não permaneçam no banco de dados após a execução do teste.

		

	}

}
