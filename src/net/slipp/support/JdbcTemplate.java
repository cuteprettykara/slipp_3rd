
package net.slipp.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcTemplate {
	private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

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
		executeUpdate(sql, createPreparedStatementSetter(parameters));
	}
	
	public <T> T executeQuery(String sql, RowMapper<T> rm, Object... parameters) throws SQLException {
		List<T> list = executeQueries(sql, rm, createPreparedStatementSetter(parameters));
		
		if (list.isEmpty()) {
			return null;
		}
		
		return list.get(0);
	}
	
	
	public <T> List<T> executeQueries(String sql, RowMapper<T> rm, PreparedStatementSetter pss) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<T> list = new ArrayList<T>();
		
		try {
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pss.setParameters(pstmt);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				T t = rm.mapRow(rs);
				list.add(t);				
			}
			
		} catch (Exception e) {
			
		} finally {
			if (rs != null) rs.close();
			if (pstmt != null) conn.close();
			if (conn != null) conn.close();
		}
		
		return list;
	}
	
	public <T> List<T> executeQueries(String sql, RowMapper<T> rm, Object... parameters) throws SQLException {
		return executeQueries(sql, rm, createPreparedStatementSetter(parameters));
	}
	
	private PreparedStatementSetter createPreparedStatementSetter(Object... parameters) {
		return new PreparedStatementSetter() {
			@Override
			public void setParameters(PreparedStatement pstmt) throws SQLException {
				for (int i = 0; i < parameters.length; i++) {
					pstmt.setObject(i+1, parameters[i]);
				}
			}
		};
	}
}
