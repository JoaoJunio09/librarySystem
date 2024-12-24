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
import model.entities.Categoria;
import model.entities.Livro;

public class LivroDaoJDBC implements CRUD<Livro> {
	
	private Connection conn = null;
	
	public LivroDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public boolean insert(Livro obj) {
		PreparedStatement stmt = null;
		String sql = "INSERT INTO tb_livro "
					+ "(Nome, Descrição, Autor, Estoque, Disponibilidade, CategoriaId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?)";
		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, obj.getNome());
			stmt.setString(2, obj.getDescricao());
			stmt.setString(3, obj.getAutor());
			stmt.setInt(4, obj.getEstoque());
			stmt.setString(5, obj.getDisponibilidade());
			stmt.setInt(6, obj.getCategoria().getId());
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
	public boolean update(Livro obj) {
		PreparedStatement stmt = null;
		String sql = "UPDATE tb_livro "
					+ "SET Nome = ?, Descrição = ?, Autor = ?, Estoque = ?, Disponibilidade = ?, CategoriaId = ? "
					+ "WHERE Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, obj.getNome());
			stmt.setString(2, obj.getDescricao());
			stmt.setString(3, obj.getAutor());
			stmt.setInt(4, obj.getEstoque());
			stmt.setString(5, obj.getDisponibilidade());
			stmt.setInt(6, obj.getCategoria().getId());
			stmt.setInt(7, obj.getId());
			
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
		String sql = "DELETE FROM tb_livro WHERE Id = ?";
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
	public Livro findById(Integer id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT l.*,c.*, c.Nome AS Categoria_nome, c.Descrição AS Categoria_descrição, c.Id AS Categoria_id "
					+ "FROM tb_livro l "
					+ "JOIN tb_categoria c ON l.CategoriaId = c.Id "
					+ "WHERE l.Id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Categoria cat = instanciaCategoria(rs);
				Livro livro = instanciaLivro(rs, cat);
				return livro;
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
	public List<Livro> findAll() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT l.*,c.*, c.Nome AS Categoria_nome, c.Descrição AS Categoria_descrição "
				+ "FROM tb_livro l "
				+ "JOIN tb_categoria c ON l.CategoriaId = c.Id ";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			List<Livro> livros = new ArrayList<Livro>();
			Map<Integer, Categoria> map = new HashMap<>();
			
			while (rs.next()) {
				Categoria cat = map.get(rs.getInt("CategoriaId"));
				if (cat == null) {
					cat = instanciaCategoria(rs);
					map.put(rs.getInt("CategoriaID"), cat);
				}
				Livro livro = instanciaLivro(rs, cat);
				livros.add(livro);
			}
			return livros;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(stmt);
			DB.closeResultSet(rs);
		}
	}
	
	private Livro instanciaLivro(ResultSet rs, Categoria cat) throws SQLException {
		Livro livro = new Livro();
		livro.setId(rs.getInt("Id"));
		livro.setNome(rs.getString("Nome"));
		livro.setDescricao(rs.getString("Descrição"));
		livro.setAutor(rs.getString("Autor"));
		livro.setEstoque(rs.getInt("Estoque"));
		livro.setDisponibilidade(rs.getString("Disponibilidade"));
		livro.setCategoria(cat);
		return livro;
	}
	
	private Categoria instanciaCategoria(ResultSet rs) throws SQLException {
		Categoria cat = new Categoria();
		cat.setId(rs.getInt("Categoria_id"));
		cat.setNome(rs.getString("Categoria_nome"));
		cat.setDescricao(rs.getString("Categoria_descrição"));
		return cat;
	}

}
