package vnua.fita.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import vnua.fita.entity.Vote;
import vnua.fita.model.DBConnection;
import vnua.fita.service.VoteService;


public class VoteServiceImpl implements VoteService{

	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection conn;
	
	// Constructor với các tham số để kết nối 
	public VoteServiceImpl(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}
	
	public boolean check(String postId, String commentId, String studentCode) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "SELECT EXISTS(SELECT * FROM tblvote WHERE postId='"+ postId;
		if(commentId == null)
			sqlCommand += "' AND commentId is "+ commentId;
		else
			sqlCommand += "' AND commentId ="+ commentId;
		sqlCommand += " AND student_code='"+ studentCode+"')";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst=conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Integer score = rs.getInt(1);
				if(score!=0)
					result = 1;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}
		
		if(result == 1) {
			return true;
		}
		return false;
	}
	
	public boolean insert(Vote vote) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "INSERT IGNORE INTO tblvote(postId, commentId, student_code) values(?,?,?)";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst=conn.prepareStatement(sqlCommand);
			pst.setString(1, vote.getPostId());
			pst.setString(2, vote.getCommentId());
			pst.setString(3, vote.getStudent_code());
			result = pst.executeUpdate();
		} catch (SQLException e) {
			
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}

		if(result == 1) {
			return true;
		}
		return false;
	}
	
	public boolean delete(String postId, String commentId, String studentCode) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "DELETE FROM tblvote WHERE postId='"+ postId;
		if(commentId == null)
			sqlCommand += "' AND commentId is "+ commentId;
		else
			sqlCommand += "' AND commentId ="+ commentId;
		if(studentCode != null)
			sqlCommand += " AND student_code='"+ studentCode +"'";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst=conn.prepareStatement(sqlCommand);
			result = pst.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}
		
		if(result == 1) {
			return true;
		}
		return false;
	}
}
