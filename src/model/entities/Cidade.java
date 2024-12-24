package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Cidade implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	private String cep;
	private Estado estado;
	
	public Cidade() {
	}

	public Cidade(Integer id, String nome, String cep, Estado estadoId) {
		this.id = id;
		this.nome = nome;
		this.cep = cep;
		this.estado = estadoId;
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

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estadoId) {
		this.estado = estadoId;
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
		Cidade other = (Cidade) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Cidade [id=" + id + ", nome=" + nome + ", cep=" + cep + ", estado=" + estado + "]";
	}
}
