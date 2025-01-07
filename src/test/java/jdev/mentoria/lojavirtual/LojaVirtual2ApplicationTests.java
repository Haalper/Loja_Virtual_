package jdev.mentoria.lojavirtual;


import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import jdev.mentoria.lojavirtual.controller.AcessoController;
import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.repository.AcessoRepository;
import jdev.mentoria.lojavirtual.service.AcessoService;
import junit.framework.TestCase;


@Profile("test")
@SpringBootTest(classes = LojaVirtual2Application.class)
public class LojaVirtual2ApplicationTests extends TestCase { //TestCase, que é uma classe base fornecida pelo JUnit (ou bibliotecas relacionadas). Isso não é obrigatório no JUnit moderno, mas pode ser usado para compatibilidade ou organização.

	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@Autowired
	private AcessoController acessoController;
	
	@Autowired
	private WebApplicationContext wac; 
	// Injeta o contexto da aplicação Spring para possibilitar os testes de integração com MockMvc
	//simulando o comportamento da API sem precisar iniciar um servidor.
	
	/*Teste do end-point de salvar*/
	@Test
	public void testRestApiCadastroAcesso() throws JsonProcessingException, Exception {
		
	    DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
	    // Configura o MockMvc com o contexto da aplicação Spring (wac), permitindo que
	    // os testes simulem requisições HTTP como se fossem feitas na aplicação real.

	    MockMvc mockMvc = builder.build();
	    // Cria uma instância de MockMvc com base na configuração acima,
	    // permitindo executar os métodos de requisição.
	    
	    Acesso acesso = new Acesso();
	    
	    acesso.setDescricao("ROLE_COMPRADOR");
	    
	    ObjectMapper objectMapper = new ObjectMapper(); 
	    // O ObjectMapper é uma classe da biblioteca Jackson usada para converter objetos
	    // Java para JSON (serialização) e JSON para objetos Java (desserialização).
	    
	    //a instancia aproveita as configurações do jackson (por isso optamos pela instancia ao invés da injeção de dependencia que viria com as configurações do spring)
	    
	    ResultActions retornoApi = mockMvc
	    						 .perform(MockMvcRequestBuilders.post("/salvarAcesso") 
	    						 // Faz uma requisição HTTP POST para o endpoint "/salvarAcesso" - passa os dados de acesso dentro do perform
	    						 .content(objectMapper.writeValueAsString(acesso))
	    						 // objectMapper Serializa o objeto acesso para JSON e o inclui no corpo da requisição.
	    						 .accept(MediaType.APPLICATION_JSON)
	    						 // Indica que a API deve retornar os dados no formato JSON.
	    						 .contentType(MediaType.APPLICATION_JSON));
                                 // Informa que o conteúdo enviado na requisição também está no formato JSON.
	    
	    //ResultActions é uma interface que representa as ações e o resultado de uma solicitação HTTP simulada realizada com o MockMvc. 
	    //Quando você faz uma requisição com mockMvc.perform(), o retorno dessa ação é um objeto ResultActions.

	    
	    System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString()); 
	    //mostra o json que a api retorna, no próprio formato exato do json
	    
	    /*Conveter o retorno da API para um obejto de acesso*/
	    
	    Acesso objetoRetorno = objectMapper. 
	    					   readValue(retornoApi.andReturn().getResponse().getContentAsString(),
	    					   Acesso.class);
	    // objectMapper Converte o JSON retornado pela API em um objeto da classe Acesso,
	    // permitindo realizar validações sobre os dados retornados.
	    // para isto, passamos 2 parametros >> retorno json da api / e o tipo da classe
	    
	    assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());
	    // Compara a descrição do objeto acesso enviado com a descrição do objeto retorno.
	    // Essa verificação garante que os dados foram salvos corretamente pela API.
	    
	    
	}
	
	@Test
	public void testRestApiDeleteAcesso() throws JsonProcessingException, Exception {
		
	    DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
	    MockMvc mockMvc = builder.build();
	    
	    Acesso acesso = new Acesso();
	    
	    acesso.setDescricao("ROLE_TESTE_DELETE");
	    
	    acesso = acessoRepository.save(acesso);
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    ResultActions retornoApi = mockMvc
	    						 .perform(MockMvcRequestBuilders.post("/deleteAcesso")
	    						 .content(objectMapper.writeValueAsString(acesso))
	    						 .accept(MediaType.APPLICATION_JSON)
	    						 .contentType(MediaType.APPLICATION_JSON));
	    
	    System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
	    System.out.println("Status de retorno: " + retornoApi.andReturn().getResponse().getStatus());
	    
	    assertEquals("Acesso Removido", retornoApi.andReturn().getResponse().getContentAsString());
	    assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	    
	    
	}
	
	@Test
	public void testRestApiDeletePorIDAcesso() throws JsonProcessingException, Exception {
		
	    DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
	    MockMvc mockMvc = builder.build();
	    
	    Acesso acesso = new Acesso();
	    
	    acesso.setDescricao("ROLE_TESTE_DELETE_ID");
	    
	    acesso = acessoRepository.save(acesso);
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    ResultActions retornoApi = mockMvc
	    						 .perform(MockMvcRequestBuilders.delete("/deleteAcessoPorId/" + acesso.getId())
	    						 .content(objectMapper.writeValueAsString(acesso))
	    						 .accept(MediaType.APPLICATION_JSON)
	    						 .contentType(MediaType.APPLICATION_JSON));
	    
	    System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
	    System.out.println("Status de retorno: " + retornoApi.andReturn().getResponse().getStatus());
	    
	    assertEquals("Acesso Removido", retornoApi.andReturn().getResponse().getContentAsString());
	    assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	    
	    
	}
	
	@Test
	public void testRestApiObterAcessoID() throws JsonProcessingException, Exception {
		
	    DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
	    MockMvc mockMvc = builder.build();
	    
	    Acesso acesso = new Acesso();
	    
	    acesso.setDescricao("ROLE_OBTER_ID");
	    
	    acesso = acessoRepository.save(acesso);
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    ResultActions retornoApi = mockMvc
	    						 .perform(MockMvcRequestBuilders.get("/obterAcesso/" + acesso.getId())
	    						 .content(objectMapper.writeValueAsString(acesso))
	    						 .accept(MediaType.APPLICATION_JSON)
	    						 .contentType(MediaType.APPLICATION_JSON));
	    
	    assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	    
	    
	    Acesso acessoRetorno = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);
	    
	    assertEquals(acesso.getDescricao(), acessoRetorno.getDescricao());
	    
	    assertEquals(acesso.getId(), acessoRetorno.getId());
	    
	}
	
	@Test
	public void testRestApiObterAcessoDesc() throws JsonProcessingException, Exception {
		
	    DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
	    MockMvc mockMvc = builder.build();
	    
	    Acesso acesso = new Acesso();
	    
	    acesso.setDescricao("ROLE_TESTE_OBTER_LIST");
	    
	    acesso = acessoRepository.save(acesso);
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    ResultActions retornoApi = mockMvc
	    						 .perform(MockMvcRequestBuilders.get("/buscarPorDesc/OBTER_LIST")
	    						 .content(objectMapper.writeValueAsString(acesso))
	    						 .accept(MediaType.APPLICATION_JSON)
	    						 .contentType(MediaType.APPLICATION_JSON));
	    
	    assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	    
	    
	    List<Acesso> retornoApiList = objectMapper.
	    							     readValue(retornoApi.andReturn()
	    									.getResponse().getContentAsString(),
	    									 new TypeReference<List<Acesso>> () {});


	    assertEquals(1, retornoApiList.size());
	    
	    assertEquals(acesso.getDescricao(), retornoApiList.get(0).getDescricao());
	    
	    
	    acessoRepository.deleteById(acesso.getId());
	    
	}



	
	@Test
	public void testCadastraAcesso() throws ExceptionMentoriaJava {
		
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
