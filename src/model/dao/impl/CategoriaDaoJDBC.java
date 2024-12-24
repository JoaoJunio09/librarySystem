package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.CRUD;
import model.entities.Categoria;

public class CategoriaDaoJDBC implements CRUD<Categoria> {
	
	private Connection conn = null;
	
	public CategoriaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public boolean insert(Categoria obj) {
		PreparedStatement stmt = null;
		String sql = "INSERT INTO tb_categoria "
					+ "(Nome, Descrição) "
					+ "VALUES "
					+ "(?, ?)";
		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, obj.getNome());
			stmt.setString(2, obj.getDescricao());
			
			int linhasAfetadas = stmt.executeUpdate();
			
			if (linhasAfetadas > 0) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
					return true;
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Erro: nenhuma linha foi afetada.");
			}
			return false;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(stmt);
		}
	}

	@Override
	public boolean update(Categoria obj) {
		PreparedStatement stmt = null;
		String sql = "UPDATE tb_categoria "
					+ "SET Nome = ?, Descrição = ? "
					+ "WHERE Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, obj.getNome());
			stmt.setString(2, obj.getDescricao());
			stmt.setInt(3, obj.getId());
			
			stmt.executeUpdate();
			return true;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(stmt);
		}
	}

	@Override
	public boolean deleteById(Integer id) {
		PreparedStatement stmt = null;
		String sql = "DELETE FROM tb_categoria WHERE Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, id);
			
			stmt.executeUpdate();
			
			return true;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(stmt);
		}
	}

	@Override
	public Categoria findById(Integer id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM tb_categoria WHERE Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Categoria obj = instanciaCategoria(rs);
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(stmt);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Categoria> findAll() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM tb_categoria";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			List<Categoria> list = new ArrayList<Categoria>();
			while (rs.next()) {
				Categoria obj = instanciaCategoria(rs);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(stmt);
			DB.closeResultSet(rs);
		}
	}
	
	private Categoria instanciaCategoria(ResultSet rs) throws SQLException {
		Categoria cat = new Categoria();
		cat.setId(rs.getInt("Id"));
		cat.setNome(rs.getString("Nome"));
		cat.setDescricao(rs.getString("Descrição"));
		return cat;
	}
}
