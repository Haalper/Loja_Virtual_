package jdev.mentoria.lojavirtual.security;

import java.util.Date;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jdev.mentoria.lojavirtual.ApplicationContextLoad;
import jdev.mentoria.lojavirtual.model.Usuario;
import jdev.mentoria.lojavirtual.repository.UsuarioRepository;



/*Criar a autenticação e retonar também a autenticação JWT*/
@Service
@Component
public class JWTTokenAutenticacaoService {
	
	
    // Tempo de expiração do token (11 dias em milissegundos)
	private static final long EXPIRATION_TIME = 959990000;
	
	// Chave secreta usada para assinar o token JWT
	private static final String SECRET = "ss/-*-*sds565dsd-s/d-s*dsds";
	
	// Prefixo que será adicionado ao token JWT para identificação
	private static final String TOKEN_PREFIX = "Bearer";
	
	 // Nome do cabeçalho HTTP onde o token será colocado
	private static final String HEADER_STRING = "Authorization";
	
	 // Método responsável por criar o token JWT e enviá-lo na resposta
	public void addAuthentication(HttpServletResponse response, String username) throws Exception {
	
		
		// Geração do token JWT utilizando o nome de usuário e tempo de expiração
		String JWT = Jwts.builder()./*Chama o gerador de token*/
				setSubject(username) // Define o nome de usuário como o sujeito do token
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // Define a data de expiração
				.signWith(SignatureAlgorithm.HS512, SECRET)  // Assina o token com o algoritmo HS512 e a chave secreta
				.compact(); // Compacta o token gerado para que ele fique pronto para uso
		
		 // Criação do token final, adicionando o prefixo "Bearer" no início
		String token = TOKEN_PREFIX + " " + JWT;
		/*Exemplo: Bearer *-/a*dad9s5d6as5d4s5d4s45dsd54s.sd4s4d45s45d4sd54d45s4d5s.ds5d5s5d5s65d6s6d*/
		
		
		// Adiciona o token gerado ao cabeçalho da resposta HTTP. A resposta HTTP quem dá a resposta para a tela e para o cliente,
		// podendo ser outra API, navegador, aplicativo, JavaScript ou qualquer outra chamada Java.
		response.addHeader(HEADER_STRING, token);
		
		liberacaoCors(response);
		
		// Adiciona o token gerado ao corpo da resposta para facilitar o teste em ferramentas como o Postman
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
		
	}
	

	/*verifica se o token JWT recebido na requisição é válido e, se for, retorna uma autenticação válida para o Spring Security. ou caso nao seja valido retona null*/
	public Authentication getAuthetication(HttpServletRequest request, HttpServletResponse response) {
		
		// Obtém o token do cabeçalho "Authorization" da requisição
		String token = request.getHeader(HEADER_STRING);
		
		// Verifica se o token não é nulo0
		if (token != null) {
			
			 // Remove o prefixo "Bearer" do token para obter o valor limpo
			String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
			
			/* Valida o token utilizando a chave secreta e obtém o "subject" (usuário associado ao token) */
	        String user = Jwts.parser()
	                .setSigningKey(SECRET) // Define a chave secreta para validar o token
	                .parseClaimsJws(tokenLimpo) // Faz o parsing do token JWT
	                .getBody().getSubject(); // Obtém o nome de usuário contido no token
	        
	        // Verifica se o nome de usuário foi encontrado no token
	        if (user != null) {
	            
	            // Busca o usuário no banco de dados pelo login (nome de usuário)
	            Usuario usuario = ApplicationContextLoad //Obtém o contexto da aplicação Spring - que é o ambiente onde todos os componentes, serviços e repositórios configurados estão disponíveis.
	                    .getApplicationContext() // Acessa o contexto da aplicação para obter beans (objetos Spring) gerenciados
	                    .getBean(UsuarioRepository.class) //// Obtém o bean do repositório que gerencia a entidade Usuario
	                    .findUserByLogin(user); // Retorna o objeto usuário, caso exista
	            
	            // Se o usuário for encontrado, cria e retorna uma autenticação válida
	            if (usuario != null) {
	                return new UsernamePasswordAuthenticationToken(
	                        usuario.getLogin(), // Login do usuário
	                        usuario.getSenha(), // Senha do usuário
	                        usuario.getAuthorities() // Permissões do usuário
							);
				}
				
			}
			
		}
		
		liberacaoCors(response); //// Configura cabeçalhos na resposta HTTP para resolver problemas de CORS, permitindo que o backend aceite requisições de diferentes origens (como de outro domínio, porta ou protocolo).
		return null; //// Retorna null se o token não for válido ou o usuário não for encontrado
		
// Resumidamente, o método getAuthentication pega o token JWT enviado no cabeçalho da requisição HTTP,
//valida esse token usando a chave secreta, extrai o nome de usuário (subject) e busca no banco de dados o objeto do usuário correspondente.
//Se o token for válido e o usuário existir, o método retorna uma instância de autenticação (Authentication) que representa o usuário autenticado no sistema.
//
// Finalidade: Esse método é usado para autenticar e identificar o usuário com base no token JWT 
//recebido, permitindo que o sistema saiba quem está fazendo a requisição e aplique as permissões ou restrições necessárias.
	}
	
	
	/* Método que adiciona cabeçalhos CORS à resposta HTTP para evitar erros de acesso entre origens diferentes */
	private void liberacaoCors(HttpServletResponse response) {
		
	    // Adiciona permissão para qualquer origem acessar a API, caso o cabeçalho ainda não esteja definido
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
	    // Permite o envio de qualquer cabeçalho na requisição, caso não esteja configurado
		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		
	    // Permite que qualquer cabeçalho adicional seja solicitado na requisição, caso não esteja configurado
		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
	    // Permite o uso de qualquer método HTTP (GET, POST, PUT, DELETE, etc.), caso não esteja configurado
		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
		
	}
	


}


/*
 * A classe JWTTokenAutenticacaoService não é responsável pelo login em si, 
 * mas sim pela geração e gerenciamento do token JWT após a autenticação do usuário. 
 * Ela cria o token, que é usado nas próximas requisições para provar que o usuário 
 * está autenticado, sem a necessidade de enviar novamente suas credenciais.
 * 
 * Ela monta o token com informações como o nome do usuário, um prazo de validade, e uma assinatura 
 * segura (SECRET). O token gerado é retornado no cabeçalho da resposta HTTP, permitindo que outras 
 * partes do sistema, como APIs ou navegadores, usem esse token para realizar operações autenticadas.
 */
