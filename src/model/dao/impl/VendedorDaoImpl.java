package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
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
import model.dao.VendedorDAO;
import model.entities.Departamento;
import model.entities.Vendedor;

public class VendedorDaoImpl implements VendedorDAO {

	private Connection conn;

	public VendedorDaoImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Vendedor vendedor) {
		PreparedStatement ps = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO vendedor");
		sb.append("(Nome, Email, DataNascimento, SalarioBase, DepartmentoId)");
		sb.append("VALUES(?, ?, ?, ?, ?);");
		
		try {
			ps = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, vendedor.getNome());
			ps.setString(2, vendedor.getEmail());
			ps.setDate(3, new Date(vendedor.getDataAniversario().getTime()));
			ps.setDouble(4, vendedor.getSalarioBase());
			ps.setInt(5, vendedor.getDepartamento().getid());
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				
				if (rs.next()) {
					int id = rs.getInt(1);
					vendedor.setId(id);
				}
				
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Erro inesperado, inserção falhou!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(ps);
		}

	}

	@Override
	public void update(Vendedor vendedor) {
		PreparedStatement ps = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE vendedor ");
		sb.append("SET Nome=?, Email=?, DataNascimento=?, SalarioBase=?, DepartmentoId=? ");
		sb.append("WHERE Id=?;");
		
		try {
			ps = conn.prepareStatement(sb.toString());
			
			ps.setString(1, vendedor.getNome());
			ps.setString(2, vendedor.getEmail());
			ps.setDate(3, new Date(vendedor.getDataAniversario().getTime()));
			ps.setDouble(4, vendedor.getSalarioBase());
			ps.setInt(5, vendedor.getDepartamento().getid());
			ps.setInt(6, vendedor.getId());
			
			ps.executeUpdate();
					
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(ps);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM vendedor ");
		sb.append("WHERE Id=?;");
		
		try {
			ps = conn.prepareStatement(sb.toString());
			ps.setInt(1, id);
			
			ps.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(ps);
		}

	}

	@Override
	public Vendedor findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		Vendedor vendedor = null;

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT vendedor.*, departamento.Nome DepNome ");
		sb.append("FROM vendedor INNER JOIN departamento ");
		sb.append("ON vendedor.DepartmentoId = departamento.Id ");
		sb.append("WHERE vendedor.Id = ?");

		try {
			ps = conn.prepareStatement(sb.toString());
			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
				Departamento dep = instanciandoDepartamento(rs);

				vendedor = instanciandoVendedor(rs, dep);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		return vendedor;

	}

	private Vendedor instanciandoVendedor(ResultSet rs, Departamento dep) throws SQLException {
		Vendedor vendedor = new Vendedor();
		vendedor.setId(rs.getInt("Id"));
		vendedor.setNome(rs.getString("Nome"));
		vendedor.setEmail(rs.getString("Email"));
		vendedor.setSalarioBase(rs.getDouble("SalarioBase"));
		vendedor.setDataAniversario(rs.getDate("DataNascimento"));
		vendedor.setDepartamento(dep);
		return vendedor;
	}

	private Departamento instanciandoDepartamento(ResultSet rs) throws SQLException {
		Departamento dep = new Departamento();
		dep.setid(rs.getInt("DepartmentoId"));
		dep.setName(rs.getNString("DepNome"));
		return dep;
	}

	@Override
	public List<Vendedor> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT vendedor.*, departamento.Nome DepNome ");
		sb.append("FROM vendedor INNER JOIN departamento ");
		sb.append("ON vendedor.DepartmentoId = departamento.Id ");
		sb.append("ORDER BY Nome");
		
		List<Vendedor> vendedores = new ArrayList<Vendedor>();
		
		try {
			ps = conn.prepareStatement(sb.toString());
			
			rs = ps.executeQuery();
			
			Map<Integer, Departamento> map = new HashMap<Integer, Departamento>();
						
			while (rs.next()) {
				Departamento dep1 = map.get(rs.getInt("DepartmentoId"));
				if(dep1 == null) {
					dep1 = instanciandoDepartamento(rs);
					map.put(rs.getInt("DepartmentoId"), dep1);
				}
				Vendedor vendedor = instanciandoVendedor(rs, dep1);
				
				vendedores.add(vendedor);	
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		
		return vendedores;
	}

	@Override
	public List<Vendedor> findByDepartamento(Departamento dep) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT vendedor.*,departamento.Nome as DepNome ");
		sb.append("FROM vendedor INNER JOIN departamento ");
		sb.append("ON vendedor.DepartmentoId = departamento.Id ");
		sb.append("WHERE DepartmentoId = ? ");
		sb.append("ORDER BY Nome");
		List<Vendedor> vendedores = new ArrayList<Vendedor>();

		try {
			ps = conn.prepareStatement(sb.toString());
			ps.setInt(1, dep.getid());

			rs = ps.executeQuery();
			Map<Integer, Departamento> map = new HashMap<Integer, Departamento>();
						
			while (rs.next()) {
				Departamento dep1 = map.get(rs.getInt("DepartmentoId"));
				if(dep1 == null) {
					dep1 = instanciandoDepartamento(rs);
					map.put(rs.getInt("DepartmentoId"), dep1);
				}
				Vendedor vendedor = instanciandoVendedor(rs, dep1);
				
				vendedores.add(vendedor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		return vendedores;
	}

}
