package jdev.mentoria.lojavirtual;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class ApplicationContextLoad implements ApplicationContextAware {
//a ApplicationContextLoad é uma alternativa para casos onde o @Autowired não pode ser usado diretamente.
	
	@Autowired
	private static ApplicationContext applicationContext;


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}


}

/*
 * Esta classe, ApplicationContextLoad, é usada para fornecer acesso manual ao ApplicationContext do Spring 
 * em qualquer parte do projeto. Isso é útil quando precisamos obter beans que não podem ser injetados diretamente 
 * com @Autowired. Ela atua como um "facilitador" para acessar objetos e recursos gerenciados pelo Spring.
 *
 * Um bean é qualquer objeto que o Spring conhece, cria e gerencia. Ele pode ser usado facilmente em outras partes 
 * do sistema e é essencial para implementar a injeção de dependência e controlar o ciclo de vida dos objetos no framework.
 */


