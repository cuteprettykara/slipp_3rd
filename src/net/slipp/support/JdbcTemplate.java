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
	
	public Object executeQuery(String sql, PreparedStatementSetter pss, RowMapper rm) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Object obj = null;
		
		try {
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pss.setParameters(pstmt);
			
			rs = pstmt.executeQuery();
			
			if (!rs.next()) {
				return null;
			}

			obj = rm.mapRow(rs);
			
		} catch (Exception e) {
			
		} finally {
			if (rs != null) rs.close();
			if (conn != null) conn.close();
			if (conn != null) conn.close();
		}
		
		return obj;
	}

}
