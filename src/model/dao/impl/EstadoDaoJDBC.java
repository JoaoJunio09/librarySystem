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
import model.entities.Estado;

public class EstadoDaoJDBC implements CRUD<Estado> {
	
	private Connection conn = null;
	
	public EstadoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public boolean insert(Estado obj) {
		PreparedStatement stmt = null;
		String sql = "INSERT INTO tb_estado "
					+ "(Nome, Sigla) "
					+ "VALUES "
					+ "(?, ?)";
		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, obj.getNome());
			stmt.setString(2, obj.getSigla());
			
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
	public boolean update(Estado obj) {
		PreparedStatement stmt = null;
		String sql = "UPDATE tb_estado "
					+ "SET Nome = ?, Sigla = ? "
					+ "WHERE Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, obj.getNome());
			stmt.setString(2, obj.getSigla());
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
		String sql = "DELETE FROM tb_estado WHERE Id = ?";
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
	public Estado findById(Integer id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM tb_estado WHERE Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Estado obj = instanciaEstado(rs);
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
	public List<Estado> findAll() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM tb_estado";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			List<Estado> list = new ArrayList<Estado>();
			while (rs.next()) {
				Estado obj = instanciaEstado(rs);
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
	
	private Estado instanciaEstado(ResultSet rs) throws SQLException {
		Estado obj = new Estado();
		obj.setId(rs.getInt("Id"));
		obj.setNome(rs.getString("Nome"));
		obj.setSigla(rs.getString("Sigla"));
		return obj;
	}
}
