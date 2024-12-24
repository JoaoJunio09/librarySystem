package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.CRUD;
import model.entities.Cidade;
import model.entities.Estado;

public class CidadeDaoJDBC implements CRUD<Cidade> {
	
	private Connection conn = null;
	
	public CidadeDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public boolean insert(Cidade obj) {
		PreparedStatement stmt = null;
		String sql = "INSERT INTO tb_cidade "
					+ "(Nome, Cep, EstadoId) "
					+ "VALUES "
					+ "(?, ?, ?)";
		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, obj.getNome());
			stmt.setString(2, obj.getCep());
			stmt.setInt(3, obj.getEstado().getId());
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
				throw new DbException("Erro: nenhuma linha afetada.");
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
	public boolean update(Cidade obj) {
		PreparedStatement stmt = null;
		String sql = "UPDATE tb_cidade "
				 	+ "SET Nome = ?, Cep = ?, EstadoId = ? "
				 	+ "WHERE Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, obj.getNome());
			stmt.setString(2, obj.getCep());
			stmt.setInt(3, obj.getEstado().getId());
			stmt.setInt(4, obj.getId());
			
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
		String sql = "DELETE FROM tb_cidade WHERE Id = ?";
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
	public Cidade findById(Integer id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT c.*,e.*, e.Id AS estado_id, e.Nome AS estado_nome "
					+ "FROM tb_cidade c "
					+ "JOIN tb_estado e ON c.EstadoId = e.Id "
					+ "WHERE c.Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Estado estado = instanciaEstado(rs);
				Cidade cidade = instanciaCidade(rs, estado);
				return cidade;
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
	public List<Cidade> findAll() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT c.*,e.*, e.Id AS estado_id, e.Nome AS estado_nome "
					+ "FROM tb_cidade c "
					+ "JOIN tb_estado e ON c.EstadoId = e.Id ";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			List<Cidade> cidades = new ArrayList<Cidade>();
			Map<Integer, Estado> map = new HashMap<>();
			
			while (rs.next()) {
				Estado estado = map.get(rs.getInt("EstadoId"));
				
				if (estado == null) {
					estado = instanciaEstado(rs);
					map.put(rs.getInt("EstadoId"), estado);
				}
				
				Cidade cidade = instanciaCidade(rs, estado);
				cidades.add(cidade);
			}
			return cidades;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(stmt);
			DB.closeResultSet(rs);
		}
	}
	
	private Cidade instanciaCidade(ResultSet rs, Estado estado) throws SQLException {
		Cidade cidade = new Cidade();
		cidade.setId(rs.getInt("Id"));
		cidade.setNome(rs.getString("Nome"));
		cidade.setCep(rs.getString("Cep"));
		cidade.setEstado(estado);
		return cidade;
	}
	
	private Estado instanciaEstado(ResultSet rs) throws SQLException {
		Estado estado = new Estado();
		estado.setId(rs.getInt("estado_id"));
		estado.setNome(rs.getString("estado_nome"));
		estado.setSigla(rs.getString("Sigla"));
		return estado;
	}
}
