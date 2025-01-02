package jdev.mentoria.lojavirtual.security;

import java.io.IOException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import com.fasterxml.jackson.databind.ObjectMapper;


import jdev.mentoria.lojavirtual.model.Usuario;


//Classe que representa um filtro de login usando JWT (Json Web Token)
//Esta classe é responsável por processar o *primeiro login* do usuário, ou seja, a autenticação inicial feita com login e senha.
//As credenciais são verificadas -> Em caso de sucesso, um token JWT é gerado e retornado para o cliente.
//Este token será usado para autenticar as requisições subsequentes, sem a necessidade de enviar login e senha novamente.
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
	//não haverá a necessidade de login novamente, mas para requisilçoes subsequentes haverá novas autenticações (usando JWTTokenAutenticacaoService)

	
	// Construtor recebe uma URL do endpoint e um AuthenticationManager 
	// que é um componente do Spring Security responsável por validar as credenciais do usuário (login e senha).
 public JWTLoginFilter(String url, AuthenticationManager authenticationManager) {

	// Define a URL específica que será monitorada para autenticação
	// Exemplo: "/login" seria a rota onde o filtro atuará para processar login
     super(new AntPathRequestMatcher(url));

     // Configura o gerenciador de autenticação que será responsável por validar as credenciais do usuário (como login e senha)
     // e determinar se ele está autorizado a acessar os recursos protegidos
     setAuthenticationManager(authenticationManager);
 }
 

 // Método que tenta autenticar o usuário com as informações fornecidas na requisição
 @Override
 public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
         throws AuthenticationException, IOException, ServletException {

	 //getInputStream(): Obtém o fluxo de entrada (input stream) da requisição HTTP, ou seja, o JSON enviado no corpo da requisição.
	 // A linha em si Transforma o JSON enviado pelo cliente (como { "login": "user", "senha": "123" }) em uma instância de Usuario.
     Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

     // Cria um token de autenticação contendo o login (nome de usuário) e a senha recebidos no JSON.
     //Esse token será usado para que o Spring Security processe e valide as credenciais do usuário.
     // getAuthenticationManager().authenticate -> Chama o gerenciador de autenticação configurado anteriormente.
     // Ele verifica as credenciais (normalmente consultando o banco de dados ou outra fonte) e, se válidas, retorna um objeto Authentication contendo as informações do usuário
     return getAuthenticationManager()
             .authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));
 }

 // Método chamado quando a autenticação é bem-sucedida...
 // JWTLoginFilter chama a JWTTokenAutenticacaoService para gerar o token JWT.
 @Override
 protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
         Authentication authResult) throws IOException, ServletException {

     try {
         // Gera um token JWT para o usuário autenticado e envia ao cliente na resposta
         new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
     } catch (Exception e) {
         // Caso ocorra um erro ao gerar o token, ele será impresso no console
         e.printStackTrace();
     }
 }
}


//Diferenças entre as 3 classes

//JWTLoginFilter: Intercepta apenas o login inicial (login/senha), 
//valida as credenciais e chama JWTTokenAutenticacaoService  para gerar o token JWT para o cliente.

//JWTTokenAutenticacaoService: gerencia tokens JWT: 
//gerar tokens na autenticação inicial e validar/extrair informações do token em requisições subsequentes.

//JwtApiAutenticacaoFilter: função principal é interceptar todas as requisições após o login 
//e chamar a JWTTokenAutenticacaoService para validar o token JWT enviado pelo cliente. 
//Se o token for válido, autentica o usuário no contexto de segurança do Spring.


