package jdev.mentoria.lojavirtual.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import jdev.mentoria.lojavirtual.enums.TipoEndereco;

@Entity
@Table(name = "endereco")
@SequenceGenerator(name = "seq_endereco", sequenceName = "seq_endereco", allocationSize = 1, initialValue = 1)
public class Endereco implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_endereco")
	private Long id;
	
	@Column(nullable = false)
	private String ruaLogra;
	
	@Column(nullable = false)
	private String cep;
	
	@Column(nullable = false)
	private String numero;
	
	private String complemento;
	
	@Column(nullable = false)
	private String bairro;
	
	@Column(nullable = false)
	private String uf;
	
	@Column(nullable = false)
	private String cidade;


	@ManyToOne(targetEntity = Pessoa.class)  //indica que o relacionamento entre as entidades Endereço e Pessoa é do tipo "muitos para um". Isso significa que muitos endereços podem estar associados a uma única pessoa.
	@JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
	private Pessoa pessoa;
	//a anotação @JoinColumn(name = "pessoa_id") está configurando uma chave estrangeira (foreign key) na tabela endereco que vai referenciar o ID da tabela pessoa.
	//pessoa_id é o nome da coluna na tabela endereco. Essa coluna será usada para armazenar o ID da pessoa que está associada ao endereço.

	//foreignKey = @ForeignKey(...): Define explicitamente a chave estrangeira para essa coluna. No caso:

//value = ConstraintMode.CONSTRAINT: Indica que o JPA deve criar a restrição de chave estrangeira no banco de dados.
//name = "pessoa_fk": Especifica o nome da restrição de chave estrangeira (neste caso, "pessoa_fk"), que vai garantir que o valor da coluna pessoa_id esteja presente na tabela Pessoa.

	@Column(nullable = false)
	@Enumerated(EnumType.STRING) //mapeia o Enum TipoEndereco para ser armazenado no banco de dados na classe Endereço
	private TipoEndereco tipoEndereco;
	
	public void setTipoEndereco(TipoEndereco tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}
	
	public TipoEndereco getTipoEndereco() {
		return tipoEndereco;
	}

	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getRuaLogra() {
		return ruaLogra;
	}


	public void setRuaLogra(String ruaLogra) {
		this.ruaLogra = ruaLogra;
	}


	public String getCep() {
		return cep;
	}


	public void setCep(String cep) {
		this.cep = cep;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getComplemento() {
		return complemento;
	}


	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}


	public String getBairro() {
		return bairro;
	}


	public void setBairro(String bairro) {
		this.bairro = bairro;
	}


	public String getUf() {
		return uf;
	}


	public void setUf(String uf) {
		this.uf = uf;
	}


	public String getCidade() {
		return cidade;
	}


	public void setCidade(String cidade) {
		this.cidade = cidade;
	}


	public Pessoa getPessoa() {
		return pessoa;
	}


	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
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
		Endereco other = (Endereco) obj;
		return Objects.equals(id, other.id);
	}
	
	

}
