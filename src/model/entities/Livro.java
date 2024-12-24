package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Livro implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nome;
	private String descricao;
	private String autor;
	private Integer estoque;
	private String disponibilidade;
	private Categoria categoria;
	
	public Livro() {
	}

	public Livro(Integer id, String nome, String descricao, String autor, Integer estoque,
			String disponibilidade, Categoria categoria) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.autor = autor;
		this.estoque = estoque;
		this.disponibilidade = disponibilidade;
		this.categoria = categoria;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public Integer getEstoque() {
		return estoque;
	}

	public void setEstoque(Integer estoque) {
		this.estoque = estoque;
	}

	public void setDisponibilidade(String disponibilidade) {
		this.disponibilidade = disponibilidade;
	}

	public String getDisponibilidade() {
		return disponibilidade;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
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
		Livro other = (Livro) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Livro [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", autor=" + autor + ", estoque="
				+ estoque + ", disponibilidade=" + disponibilidade + ", categoria=" + categoria + "]";
	}
}
