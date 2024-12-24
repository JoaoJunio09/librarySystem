package model.dao;

import db.DB;
import model.dao.impl.AdminDaoJDBC;
import model.dao.impl.CategoriaDaoJDBC;
import model.dao.impl.CidadeDaoJDBC;
import model.dao.impl.ClienteDaoJDBC;
import model.dao.impl.EmprestimoDaoJDBC;
import model.dao.impl.EstadoDaoJDBC;
import model.dao.impl.FornecedorDaoJDBC;
import model.dao.impl.LivroDaoJDBC;
import model.entities.Admin;
import model.entities.Categoria;
import model.entities.Cidade;
import model.entities.Cliente;
import model.entities.Emprestimo;
import model.entities.Estado;
import model.entities.Fornecedor;
import model.entities.Livro;

public class DaoFactory {

	public static CRUD<Cliente> createClienteDaoJDBC() {
		return new ClienteDaoJDBC(DB.getConnection());
	}
	
	public static CRUD<Cidade> createCidadeDaoJDBC() {	
		return new CidadeDaoJDBC(DB.getConnection());
	}
	
	public static CRUD<Estado> createEstadoDaoJDBC() {
		return new EstadoDaoJDBC(DB.getConnection());
	}
	
	public static CRUD<Categoria> createCategoriaDaoJDBC() {
		return new CategoriaDaoJDBC(DB.getConnection());
	}
	
	public static CRUD<Livro> createLivroDaoJDBC() {
		return new LivroDaoJDBC(DB.getConnection());
	}
	
	public static CRUD<Fornecedor> createFornecedorDaoJDBC() {
		return new FornecedorDaoJDBC(DB.getConnection());
	}
	
	public static CRUD<Emprestimo> createEmprestimoDaoJDBC() {
		return new EmprestimoDaoJDBC(DB.getConnection());
	}
	
	public static CRUD<Admin> createAdminDaoJDBC() {
		return new AdminDaoJDBC(DB.getConnection());
	}
}