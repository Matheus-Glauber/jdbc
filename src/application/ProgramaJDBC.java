package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import java.text.ParseException;

import db.DB;
import db.DbException;

public class ProgramaJDBC {

	public static void main(String[] args) {
		
		System.out.println("--== Recuperando Dados ==--");
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery("select * from department");
			
			while(rs.next()) {
				System.out.println(rs.getInt("Id") + ", " + rs.getString("Name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
			DB.closeConnection();
		}
		
		System.out.println("");
		System.out.println("--== Inserindo Dados ==--");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Connection conn2 = null;
		PreparedStatement ps = null;
		
		try {
			conn2 = DB.getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO seller ");
			sb.append("(Name, Email, BirthDate, BaseSalary, DepartmentId) ");
			sb.append("values ");
			sb.append("(?, ?, ?, ?, ?)");
			ps = conn2.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, "Matheus Glauber");
			ps.setString(2, "glauber.jordao@gmail.com");
			ps.setDate(3, new Date(sdf.parse("21/08/1995").getTime()));
			ps.setDouble(4, 1850.70);
			ps.setInt(5, 2);
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs1 = ps.getGeneratedKeys();
				while(rs.next()) {
					int id = rs1.getInt(1);
					System.out.println("Pronto! Id = " + id);
				}
			}else {
				System.out.println("Nenhuma linha foi alterada!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(ps);
			DB.closeConnection();
		}
		
		Connection conn1 = null;
		PreparedStatement ps1 = null;
		
		try {
			conn1 = DB.getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE seller ");
			sb.append("SET BaseSalary= BaseSalary + ? ");
			sb.append("WHERE name= ?");
			
			ps = conn.prepareStatement(sb.toString());
			ps.setDouble(1, 200.00);
			ps.setString(2, "Matheus Glauber");
			System.out.println(ps.toString());
			
			int rowsAffected = ps1.executeUpdate();
			System.out.println("Pronto! Linhas afetadas: " + rowsAffected);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DB.closeStatement(ps1);
			DB.closeConnection();
		}
		
		Connection conn3 = null;
		PreparedStatement ps2 = null;
		
		try {
			conn3 = DB.getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM department ");
			sb.append("WHERE ");
			sb.append("Id= ?");
			
			ps = conn3.prepareStatement(sb.toString());
			
			ps2.setInt(1, 2);
			
			System.out.println(ps2.toString());
			
			int rowsAffected = ps2.executeUpdate();
			System.out.println("Pronto! Linhas afetadas: " + rowsAffected);
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps2);
			DB.closeConnection();
		}	
		
	}

}
