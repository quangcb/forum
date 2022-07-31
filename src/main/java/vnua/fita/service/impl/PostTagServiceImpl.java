package vnua.fita.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import vnua.fita.model.DBConnection;
import vnua.fita.service.PostTagService;


public class PostTagServiceImpl implements PostTagService{

	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection conn;
	
	// Constructor với các tham số để kết nối 
	public PostTagServiceImpl(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}
	
	public boolean check(String tagName) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "SELECT EXISTS(SELECT * FROM tbltag WHERE tagName ='"+ tagName +"')" ;
		PreparedStatement pst = null;
//		int result = 0;
		try {
			pst=conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Integer score = rs.getInt(1);
				if(score!=0)
					return true;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}
		
//		if(result == 1) {
//			return true;
//		}
		return false;
	}
	
	public boolean insertPostTag(String postId, String tagId, int count) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "INSERT IGNORE INTO tblpostTag(postId, tagId, count) values('"+postId+"','"+tagId+"','"+count+"')";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst=conn.prepareStatement(sqlCommand);
//			pst.setString(1, tag.getTagName());
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
	
	public boolean insertTag(String tagName) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "INSERT IGNORE INTO tbltag(tagName) values('"+tagName+"')";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst=conn.prepareStatement(sqlCommand);
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
	
	public boolean delete(String postId, String tagId) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "DELETE FROM tblposttag WHERE postId='"+ postId +"'";
		if(tagId!=null)
			sqlCommand += " AND tagId='"+tagId+"'";
//		else 	
//			sqlCommand	+= " tagId is null";
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
	
	public Integer getId(String tagName) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "SELECT id FROM tbltag WHERE tagName='" + tagName +"'";
		PreparedStatement pst = null;
		Integer id = null;
		try {
			pst = conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}
		return id;
	}
}
