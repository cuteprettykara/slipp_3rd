package net.slipp.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.slipp.support.JdbcTemplate;
import net.slipp.support.PreparedStatementSetter;
import net.slipp.support.RowMapper;

public class UserDAO {

	public void addUser(User user) throws SQLException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		
		PreparedStatementSetter pss = new PreparedStatementSetter() {
			@Override
			public void setParameters(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, user.getUserId());
				pstmt.setString(2, user.getPassword());
				pstmt.setString(3, user.getName());
				pstmt.setString(4, user.getEmail());
			}
		};
		
		String sql = "insert into USERS values(?,?,?,?)";
		jdbcTemplate.executeUpdate(sql, pss);
	}

	public void removeUser(String userId) throws SQLException {
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		
		PreparedStatementSetter pss = new PreparedStatementSetter() {
			@Override
			public void setParameters(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, userId);
			}
		};
		 
		String sql ="delete from USERS where userId = ?";
		jdbcTemplate.executeUpdate(sql, pss);

	}

	public void update(User user) throws SQLException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		
		PreparedStatementSetter pss = new PreparedStatementSetter() {
			@Override
			public void setParameters(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, user.getPassword());
				pstmt.setString(2, user.getName());
				pstmt.setString(3, user.getEmail());
				pstmt.setString(4, user.getUserId());
			}
		};
		
		String sql = "update USERS" +
				 	 "   set password = ?" + 
				 	 "     , name     = ?" + 
				 	 "     , email    = ?" +
				 	 " where userId   = ?";
		jdbcTemplate.executeUpdate(sql, pss);
	}
	
	public User findById(String userId) throws SQLException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		
		PreparedStatementSetter pss = new PreparedStatementSetter() {
			@Override
			public void setParameters(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, userId);
			}
		};
		
		RowMapper rm = new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs) throws SQLException {
				User user = new User(
						rs.getString("userId"),
						rs.getString("password"),
						rs.getString("name"),
						rs.getString("email")
						);
				return user;
			}
			
		};
		
		String sql = "select * from USERS where userId = ?"; 
		return (User) jdbcTemplate.executeQuery(sql, pss, rm);
	}
}
