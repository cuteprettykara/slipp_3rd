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
		String sql = "insert into USERS values(?,?,?,?)";
		jdbcTemplate.executeUpdate(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
	}

	public void removeUser(String userId) throws SQLException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql ="delete from USERS where userId = ?";
		jdbcTemplate.executeUpdate(sql, userId);
	}

	public void update(User user) throws SQLException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		
		String sql = "update USERS" +
				 	 "   set password = ?" + 
				 	 "     , name     = ?" + 
				 	 "     , email    = ?" +
				 	 " where userId   = ?";
		jdbcTemplate.executeUpdate(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
	}
	
	public User findById(String userId) throws SQLException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		
		RowMapper<User> rm = new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs) throws SQLException {
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
		return jdbcTemplate.executeQuery(sql, rm, userId);
	}
}
