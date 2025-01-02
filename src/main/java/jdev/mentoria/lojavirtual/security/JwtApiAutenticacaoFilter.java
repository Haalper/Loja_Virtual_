package jdev.mentoria.lojavirtual.security;

import java.io.IOException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;


public class JwtApiAutenticacaoFilter extends GenericFilterBean {
// GenericFilterBean, que permite implementar lógica personalizada para interceptar e processar requisições HTTP antes que elas alcancem os endpoints da aplicação.

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//doFilter: Esse é o principal método do filtro, chamado automaticamente pelo Spring Security para cada requisição que chega à aplicação.
		//recebe a requisição e uma resposta
		//O FilterChain permite que o filtro atual decida se a requisição deve ser passada para o próximo filtro na cadeia (se o token JWT for válido) ou se a execução será interrompida.
		
//		exemplo -> usuário já logado no site clica no Histórico de Compras, o que gera uma requisição HTTP do tipo GET para a URL /historico-compras
//		O servidor recebe essa requisição e cria os objetos request e response:
//		Request: Representa a requisição HTTP, incluindo o token JWT no cabeçalho.
//		Response: Um objeto vazio que será preenchido ao longo do processamento.
		
		
		Authentication authentication = new JWTTokenAutenticacaoService().
				getAuthetication((HttpServletRequest) request, (HttpServletResponse) response);
		//O método doFilter chama o JWTTokenAutenticacaoService para validar o token JWT e, com base no token validado,
		//cria um objeto Authentication que conterá as informações do usuário autenticado
		
		//Portanto, podemos dizer que authentication é uma instância que representa o usuário autenticado no Spring Security.
		
		//Authentication contém informações como o nome de usuário, as credenciais (como senha) e as permissões (ou roles) do usuário.
		//Esse objeto é usado pelo sistema para determinar se o usuário tem permissão para acessar determinados recursos ou executar ações.
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
        //essa linha representa o contexto de segurança do spring
		//que recebe a instancia do usuário autenticado (authentication)
		//Assim, autentica o usuário no contexto de segurança do Spring.
		
		chain.doFilter(request, response);
		//chain.doFilter: Passa a requisição e a resposta para o próximo filtro na cadeia, permitindo que outros filtros ou componentes processem a requisição.
		
	}
	
	


}
