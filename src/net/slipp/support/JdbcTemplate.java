package net.slipp.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {
	public void executeUpdate(String sql, PreparedStatementSetter pss) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pss.setParameters(pstmt);
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			
		} finally {
			if (pstmt != null) pstmt.close();
			if (conn != null) conn.close();
		}
	}
	
	public void executeUpdate(String sql, Object... parameters) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			for (int i = 0; i < parameters.length; i++) {
				pstmt.setObject(i+1, parameters[i]);
			}
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			
		} finally {
			if (pstmt != null) pstmt.close();
			if (conn != null) conn.close();
		}
	}
	
	public <T> T executeQuery(String sql, RowMapper<T> rm,  PreparedStatementSetter pss) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		T t = null;
		
		try {
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pss.setParameters(pstmt);
			
			rs = pstmt.executeQuery();
			
			if (!rs.next()) {
				return null;
			}

			t = rm.mapRow(rs);
			
		} catch (Exception e) {
			
		} finally {
			if (rs != null) rs.close();
			if (pstmt != null) conn.close();
			if (conn != null) conn.close();
		}
		
		return t;
	}
	
	public <T> T executeQuery(String sql, RowMapper<T> rm, Object... parameters) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		T t = null;
		
		try {
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			for (int i = 0; i < parameters.length; i++) {
				pstmt.setObject(i+1, parameters[i]);
			}
			
			rs = pstmt.executeQuery();
			
			if (!rs.next()) {
				return null;
			}
			
			t = rm.mapRow(rs);
			
		} catch (Exception e) {
			
		} finally {
			if (rs != null) rs.close();
			if (pstmt != null) conn.close();
			if (conn != null) conn.close();
		}
		
		return t;
	}

}
