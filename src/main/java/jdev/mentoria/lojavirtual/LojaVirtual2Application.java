package jdev.mentoria.lojavirtual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "jdev.mentoria.lojavirtual.model")  //mapeia onde estão as classes do model, que por sua vez gera as tabelas do bd
@ComponentScan(basePackages = {"jdev.*"})  //varrer todas as pastas encontrando recursos
@EnableJpaRepositories(basePackages = {"jdev.mentoria.lojavirtual.repository"})
@EnableTransactionManagement   //gerenciar transações com BD - pode acontecer delas não ficarem salvas, ou ficarem presas, bd travado etc
public class LojaVirtual2Application {

	public static void main(String[] args) {
		SpringApplication.run(LojaVirtual2Application.class, args);
	}

}
