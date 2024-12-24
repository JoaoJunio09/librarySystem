package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Admin implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String login;
	private String senha;
	
	public Admin() {
	}

	public Admin(Integer id, String login, String senha) {
		this.id = id;
		this.login = login;
		this.senha = senha;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
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
		Admin other = (Admin) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Admin [id=" + id + ", login=" + login + ", senha=" + senha + "]";
	}
}
