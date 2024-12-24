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
import model.entities.Cliente;
import model.entities.Estado;

public class ClienteDaoJDBC implements CRUD<Cliente> {
	
	private Connection conn = null;
	
	public ClienteDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public boolean insert(Cliente obj) {
		PreparedStatement stmt = null;
		String sql = "INSERT INTO tb_cliente "
					+ "(Nome, Sobrenome, DataNascimento, Telefone, Email, Endereco, CidadeId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?)";
		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, obj.getNome());
			stmt.setString(2, obj.getSobrenome());
			stmt.setDate(3, new java.sql.Date(obj.getDataNascimento().getTime()));
			stmt.setString(4, obj.getTelefone());
			stmt.setString(5, obj.getEmail());
			stmt.setString(6, obj.getEndereco());
			stmt.setInt(7, obj.getCidade().getId());
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
	public boolean update(Cliente obj) {
		PreparedStatement stmt = null;
		String sql = "UPDATE tb_cliente "
					+ "SET Nome = ?, Sobrenome = ?, DataNascimento = ?, Telefone = ?, Email = ?, Endereco = ?, CidadeId = ? "
					+ "WHERE Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, obj.getNome());
			stmt.setString(2, obj.getSobrenome());
			stmt.setDate(3, new java.sql.Date(obj.getDataNascimento().getTime()));
			stmt.setString(4, obj.getTelefone());
			stmt.setString(5, obj.getEmail());
			stmt.setString(6, obj.getEndereco());
			stmt.setInt(7, obj.getCidade().getId());
			stmt.setInt(8, obj.getId());
			
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
		String sql = "DELETE FROM tb_cliente WHERE Id = ?";
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
	public Cliente findById(Integer id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT c.*,ci.*,e.*, ci.Nome AS CidadeNome, e.Nome AS EstadoNome, "
					+ "ci.Id AS cidade_id, e.Id AS estado_id "
					+ "FROM tb_cliente c "
					+ "JOIN tb_cidade ci ON c.CidadeId = ci.Id "
					+ "JOIN tb_estado e ON ci.EstadoId = e.Id "
					+ "WHERE c.Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Estado estado = instanciaEstado(rs);
				Cidade cidade = instanciaCidade(rs, estado);
				Cliente cliente = instanciaCliente(rs, cidade, estado);
				return cliente;
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
	public List<Cliente> findAll() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT c.*,ci.*,e.*, ci.Nome AS CidadeNome, e.Nome AS EstadoNome, "
				+ "ci.Id AS cidade_id, e.Id AS estado_id "
				+ "FROM tb_cliente c "
				+ "JOIN tb_cidade ci ON c.CidadeId = ci.Id "
				+ "JOIN tb_estado e ON ci.EstadoId = e.Id ";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			List<Cliente> clientes = new ArrayList<>();
			Map<Integer, Cidade> map = new HashMap<>();
			
			while (rs.next()) {
				Estado estado = instanciaEstado(rs);
				Cidade cidade = map.get(rs.getInt("CidadeId"));
				
				if (cidade == null) {
					cidade = instanciaCidade(rs, estado);
				}
				
				Cliente cliente = instanciaCliente(rs, cidade, estado);
				clientes.add(cliente);
			}
			return clientes;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(stmt);
			DB.closeResultSet(rs);
		}
	}
	
	private Cliente instanciaCliente(ResultSet rs, Cidade cidade, Estado estado) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.setId(rs.getInt("Id"));
		cliente.setNome(rs.getString("Nome"));
		cliente.setSobrenome(rs.getString("Sobrenome"));
		cliente.setDataNascimento(rs.getDate("DataNascimento"));
		cliente.setTelefone(rs.getString("Telefone"));
		cliente.setEmail(rs.getString("Email"));
		cliente.setEndereco(rs.getString("Endereco"));
		cliente.setCidade(cidade);
		return cliente;
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
