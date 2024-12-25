package jdev.mentoria.lojavirtual.security;

import java.util.Date;


import javax.servlet.http.HttpServletResponse;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;




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
	
		
		/*Montagem do Token*/
		
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
		
		
		// Adiciona o token gerado ao corpo da resposta para facilitar o teste em ferramentas como o Postman
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
		
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
