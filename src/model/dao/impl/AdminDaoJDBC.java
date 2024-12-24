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
import model.entities.Admin;

public class AdminDaoJDBC implements CRUD<Admin> {
	
	private Connection conn = null;
	
	public AdminDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public boolean insert(Admin obj) {
		PreparedStatement stmt = null;
		String sql = "INSERT INTO tb_login "
					+ "(Login, Senha) "
					+ "VALUES "
					+ "(?, ?)";
		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, obj.getLogin());
			stmt.setString(2, obj.getSenha());
			
			int linhasAfetadas = stmt.executeUpdate();
			
			if (linhasAfetadas > 0) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
					return true;
				}
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
			DB.closeResultSet(null);
		}
	}

	@Override
	public boolean update(Admin obj) {
		PreparedStatement stmt = null;
		String sql = "UPDATE tb_login "
					+ "SET Nome = ?, Sigla = ? "
					+ "WHERE Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, obj.getLogin());
			stmt.setString(2, obj.getSenha());
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
		String sql = "DELETE FROM tb_login WHERE Id = ?";
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
	public Admin findById(Integer id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM tb_login WHERE Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Admin obj = instanciaAdmin(rs);
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
	public List<Admin> findAll() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM tb_login";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			List<Admin> list = new ArrayList<Admin>();
			while (rs.next()) {
				Admin obj = instanciaAdmin(rs);
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
	
	private Admin instanciaAdmin(ResultSet rs) throws SQLException {
		Admin obj = new Admin();
		obj.setId(rs.getInt("Id"));
		obj.setLogin(rs.getString("Login"));
		obj.setSenha(rs.getString("Senha"));
		return obj;
	}
}
