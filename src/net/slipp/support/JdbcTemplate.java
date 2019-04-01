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
	
	public <T> T executeQuery(String sql, PreparedStatementSetter pss, RowMapper<T> rm) throws SQLException {
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
			if (conn != null) conn.close();
			if (conn != null) conn.close();
		}
		
		return t;
	}

}
