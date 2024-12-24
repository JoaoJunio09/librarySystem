package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Fornecedor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	private String endereco;
	private String cnpj;
	private String telefone;
	private String email;
	private Cidade cidade;
	
	public Fornecedor() {
	}

	public Fornecedor(Integer id, String nome, String endereco, String cnpj, String telefone, String email,
			Cidade cidade) {
		this.id = id;
		this.nome = nome;
		this.endereco = endereco;
		this.cnpj = cnpj;
		this.telefone = telefone;
		this.email = email;
		this.cidade = cidade;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
		Fornecedor other = (Fornecedor) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Fornecedor [id=" + id + ", nome=" + nome + ", endereco=" + endereco + ", cep=" + cnpj + ", telefone="
				+ telefone + ", email=" + email + ", cidade=" + cidade + "]";
	}
}
