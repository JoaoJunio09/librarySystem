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
import model.entities.Fornecedor;
import model.entities.Estado;

public class FornecedorDaoJDBC implements CRUD<Fornecedor> {
	
	private Connection conn = null;
	
	public FornecedorDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public boolean insert(Fornecedor obj) {
		PreparedStatement stmt = null;
		String sql = "INSERT INTO tb_fornecedor "
					+ "(Nome, Endereco, CNPJ, Telefone, Email, CidadeId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?)";
		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, obj.getNome());
			stmt.setString(2, obj.getEndereco());
			stmt.setString(3, obj.getCnpj());
			stmt.setString(4, obj.getTelefone());
			stmt.setString(5, obj.getEmail());
			stmt.setInt(6, obj.getCidade().getId());
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
			DB.closeConnection();
		}
		
	}

	@Override
	public boolean update(Fornecedor obj) {
		PreparedStatement stmt = null;
		String sql = "UPDATE tb_fornecedor "
					+ "SET Nome = ?, Endereco = ?, CNPJ = ?, Telefone = ?, Email = ?, CidadeId = ? "
					+ "WHERE Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, obj.getNome());
			stmt.setString(2, obj.getTelefone());
			stmt.setString(3, obj.getEndereco());
			stmt.setString(4, obj.getCnpj());
			stmt.setString(5, obj.getEmail());
			stmt.setInt(6, obj.getCidade().getId());
			stmt.setInt(7, obj.getId());
			
			int linhasAfetadas = stmt.executeUpdate();
			
			if (linhasAfetadas > 0) {
				return true;
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
	public boolean deleteById(Integer id) {
		PreparedStatement stmt = null;
		String sql = "DELETE FROM tb_fornecedor WHERE Id = ?";
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
	public Fornecedor findById(Integer id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT f.*,ci.*,e.*, ci.Nome AS CidadeNome, e.Nome AS EstadoNome, "
					+ "ci.Id AS cidade_id, e.Id AS estado_id "
					+ "FROM tb_fornecedor f "
					+ "JOIN tb_cidade ci ON f.CidadeId = ci.Id "
					+ "JOIN tb_estado e ON ci.EstadoId = e.Id "
					+ "WHERE f.Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Estado estado = instanciaEstado(rs);
				Cidade cidade = instanciaCidade(rs, estado);
				Fornecedor fornecedor = instanciaFornecedor(rs, cidade, estado);
				return fornecedor;
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
	public List<Fornecedor> findAll() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT c.*,ci.*,e.*, ci.Nome AS CidadeNome, e.Nome AS EstadoNome, "
				+ "ci.Id AS cidade_id, e.Id AS estado_id "
				+ "FROM tb_fornecedor c "
				+ "JOIN tb_cidade ci ON c.CidadeId = ci.Id "
				+ "JOIN tb_estado e ON ci.EstadoId = e.Id ";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			List<Fornecedor> fornecedors = new ArrayList<>();
			Map<Integer, Cidade> map = new HashMap<>();
			
			while (rs.next()) {
				Estado estado = instanciaEstado(rs);
				Cidade cidade = map.get(rs.getInt("CidadeId"));
				
				if (cidade == null) {
					cidade = instanciaCidade(rs, estado);
				}
				
				Fornecedor fornecedor = instanciaFornecedor(rs, cidade, estado);
				fornecedors.add(fornecedor);
			}
			return fornecedors;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(stmt);
			DB.closeResultSet(rs);
		}
	}
	
	private Fornecedor instanciaFornecedor(ResultSet rs, Cidade cidade, Estado estado) throws SQLException {
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setId(rs.getInt("Id"));
		fornecedor.setNome(rs.getString("Nome"));
		fornecedor.setEndereco(rs.getString("Endereco"));
		fornecedor.setCnpj(rs.getString("CNPJ"));
		fornecedor.setTelefone(rs.getString("Telefone"));
		fornecedor.setEmail(rs.getString("Email"));
		fornecedor.setCidade(cidade);
		return fornecedor;
	}
	
	private Cidade instanciaCidade(ResultSet rs, Estado estado) throws SQLException {
		Cidade cidade = new Cidade();
		cidade.setId(rs.getInt("cidade_id"));
		cidade.setNome(rs.getString("CidadeNome"));
		cidade.setCep(rs.getString("Cep"));
		cidade.setEstado(estado);
		return cidade;
	}
	
	private Estado instanciaEstado(ResultSet rs) throws SQLException {
		Estado estado = new Estado();
		estado.setId(rs.getInt("estado_id"));
		estado.setNome(rs.getString("EstadoNome"));
		estado.setSigla(rs.getString("Sigla"));
		return estado;
	}

}
