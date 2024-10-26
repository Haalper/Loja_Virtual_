package jdev.mentoria.lojavirtual.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "seq_pessoa", sequenceName = "seq_pessoa", initialValue = 1, allocationSize = 1)
public abstract class Pessoa implements Serializable {
	
	//Como funciona TABLE_PER_CLASS:
//	Tabelas separadas: Para cada subclasse de Pessoa, será criada uma tabela separada no banco de dados com suas próprias colunas. Essas tabelas vão incluir todas as colunas da superclasse Pessoa e suas próprias colunas específicas (se houver).
//	Não há tabela para a classe Pessoa: Como Pessoa é uma classe abstrata, não será criada uma tabela diretamente para ela. Apenas as subclasses concretas gerarão tabelas.

	//ou seja, as tabelas pessoa física e pessoa jurífica, terão suas próprias colunas específicas >> além das colunas herdadas da classe pessoa (id, nome, endereço. telefone)


	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pessoa")
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String telefone;
	
	@OneToMany(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Endereco> enderecos = new ArrayList<Endereco>();
	
	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}
	
	public List<Endereco> getEnderecos() {
		return enderecos;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		return Objects.equals(id, other.id);
	}

	
	
}

////1. mappedBy = "pessoa"
//O que significa?:
//O atributo mappedBy indica o lado "não proprietário" do relacionamento. Neste caso, a entidade Pessoa não possui diretamente a chave estrangeira que conecta as duas entidades. A chave estrangeira está na entidade Endereco.
//O valor "pessoa" refere-se ao nome do atributo na classe Endereco que mapeia a relação com Pessoa.
//Ou seja, na classe Endereco, você tem o atributo private Pessoa pessoa;, e esse é o lado proprietário do relacionamento.
//Por que isso é necessário?:
//Isso evita que duas tabelas de relacionamento sejam criadas (uma para cada lado), assegurando que a chave estrangeira esteja apenas em uma das tabelas (no caso, na tabela endereco).

//2. orphanRemoval = true
//O que significa?:
//Isso indica que, se um endereço for removido da lista de enderecos em Pessoa, ele será automaticamente removido do banco de dados.
//Em outras palavras, um "órfão" (um registro de Endereco que não está mais vinculado a uma Pessoa) será excluído.
//Por que usar?:
//Isso é útil quando você quer garantir que, ao remover um elemento de um relacionamento, ele não fique "órfão" no banco de dados e seja excluído automaticamente.

//3. cascade = CascadeType.ALL
//O que significa?:
//O atributo cascade define quais operações realizadas na entidade Pessoa devem ser propagadas para a entidade relacionada Endereco.
//CascadeType.ALL significa que todas as operações — persist, merge, remove, refresh, e detach — serão aplicadas também aos endereços relacionados.
//Por que usar?:
//Isso facilita o gerenciamento do ciclo de vida das entidades relacionadas. Por exemplo, ao salvar uma nova Pessoa, todos os endereços associados a essa pessoa também serão automaticamente salvos.

//4. fetch = FetchType.LAZY
//O que significa?:
//O atributo fetch define a estratégia de carregamento dos dados. FetchType.LAZY significa que os endereços relacionados não serão carregados imediatamente quando a entidade Pessoa for carregada.
//Em vez disso, os endereços serão carregados sob demanda, quando o método getEnderecos() for chamado pela primeira vez.
//Por que usar?:
//Isso é útil para melhorar a performance, evitando o carregamento desnecessário de dados relacionados quando eles não forem imediatamente necessários.
//Outros pontos importantes:
//Lista de endereços: A relação é mapeada como uma List<Endereco>, o que significa que uma pessoa pode ter vários endereços associados. Por padrão, o JPA usa uma coleção como List, Set, etc., para modelar o relacionamento de "um para muitos".
//
//Inicialização da lista: A lista enderecos é inicializada com um new ArrayList<>(). Isso garante que o objeto não seja nulo quando você começar a adicionar endereços à pessoa, evitando possíveis erros de NullPointerException.