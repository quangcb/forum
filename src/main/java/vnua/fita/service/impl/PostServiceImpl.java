package vnua.fita.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import util.FormatDate;
import vnua.fita.entity.Post;
import vnua.fita.model.DBConnection;
import vnua.fita.service.PostService;


public class PostServiceImpl implements PostService{

	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection conn;
	
	// Constructor với các tham số để kết nối 
	public PostServiceImpl(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}
	
	public List<Post> search(String studentCode, String tagName, String keyword){
		List<Post> result = new LinkedList<Post>();
		PreparedStatement pst = null;
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand ="SELECT p.*, s.name, s.profileImageUrl, t1.tagName as num1, t2.tagName as num2, t3.tagName as num3, t4.tagName as num4 "
				+ " from tblpost p "
				+ " INNER JOIN tblstudent s ON p.ownerStudentCode=s.student_code"
				+ " LEFT JOIN training.tblposttag pt1 on p.postId = pt1.postId AND pt1.count =1 or pt1.count is null"
				+ " LEFT JOIN training.tblposttag pt2 on p.postId = pt2.postId AND pt2.count =2 or pt2.count is null"
				+ " LEFT JOIN training.tblposttag pt3 on p.postId = pt3.postId AND pt3.count =3 or pt3.count is null"
				+ " LEFT JOIN training.tblposttag pt4 on p.postId = pt4.postId AND pt4.count =4 or pt4.count is null"
				+ " LEFT JOIN training.tbltag t1 ON t1.id = pt1.tagId"
				+ " LEFT JOIN training.tbltag t2 ON t2.id = pt2.tagId"
				+ " LEFT JOIN training.tbltag t3 ON t3.id = pt3.tagId"
				+ " LEFT JOIN training.tbltag t4 ON t4.id = pt4.tagId";
		try {
			
			// Mệnh đề Where chỉ xuất hiện trong câu sql khi keyword khác null, khác rỗng
			if ((studentCode!=null && !"".equals(studentCode))){
				sqlCommand += " WHERE p.ownerStudentCode = " + studentCode;
			}
			
			if ((tagName!=null && !"".equals(tagName))){
				sqlCommand += " WHERE t1.tagName='"+ tagName +"' or t2.tagName='"+ tagName +"' or t3.tagName='"+ tagName +"' or t4.tagName='"+ tagName +"'";
			}
			
			if ((keyword!=null && !"".equals(keyword))){
				sqlCommand += " WHERE p.title LIKE  '%" + keyword +"%'";
			}
			
			sqlCommand += " ORDER BY creationDate DESC";
		
			pst = conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String postId = rs.getString(1);
				LocalDateTime creationDate = rs.getObject (2, LocalDateTime.class);
				String creationDateFormat = FormatDate.relativize(creationDate);
				
				Integer score = rs.getInt(3);
				String title = rs.getString(4);
				String body = rs.getString(5);
				Integer commentCount = rs.getInt(6);
				
				//LocalDateTime lastEditDate = rs.getObject (7, LocalDateTime.class);
				//String lastEditDateFormat = FormatDate.relativize(lastEditDate);
				String ownerStudentCode = rs.getString(8);
				String displayName = rs.getString(9);
				String profileImageUrl = rs.getString(10);
				String tag1 = rs.getString(11);
				String tag2 = rs.getString(12);
				String tag3 = rs.getString(13);
				String tag4 = rs.getString(14);
				Post post = new Post(postId, creationDateFormat, score, title,  body, commentCount, ownerStudentCode, displayName, profileImageUrl, tag1, tag2, tag3, tag4);
				result.add(post);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closeStatement(pst);
		}
		return result;
	}
	
	public Post selectPost(String postId, String studentCode) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand ="SELECT p.*, t1.tagName as num1, t2.tagName as num2, t3.tagName as num3, t4.tagName as num4 "
				+ " from tblpost p "
				+ " INNER JOIN tblstudent s ON p.ownerStudentCode=s.student_code"
				+ " LEFT JOIN training.tblposttag pt1 on p.postId = pt1.postId AND pt1.count =1 or pt1.count is null"
				+ " LEFT JOIN training.tblposttag pt2 on p.postId = pt2.postId AND pt2.count =2 or pt2.count is null"
				+ " LEFT JOIN training.tblposttag pt3 on p.postId = pt3.postId AND pt3.count =3 or pt3.count is null"
				+ " LEFT JOIN training.tblposttag pt4 on p.postId = pt4.postId AND pt4.count =4 or pt4.count is null"
				+ " LEFT JOIN training.tbltag t1 ON t1.id = pt1.tagId"
				+ " LEFT JOIN training.tbltag t2 ON t2.id = pt2.tagId"
				+ " LEFT JOIN training.tbltag t3 ON t3.id = pt3.tagId"
				+ " LEFT JOIN training.tbltag t4 ON t4.id = pt4.tagId";
		if(postId!=null)		
			sqlCommand+= " WHERE p.postId='" + postId +"'";
		if(studentCode!=null)
			sqlCommand+= " WHERE ownerStudentCode='" + studentCode +"' ORDER BY creationDate DESC LIMIT 1";
		PreparedStatement pst = null;
		Post post = null;
		try {
			pst = conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String id = rs.getString(1);
				LocalDateTime creationDate = rs.getObject (2, LocalDateTime.class);
				String creationDateFormat = FormatDate.relativize(creationDate);
				Integer score = rs.getInt(3);
				String title = rs.getString(4);
				String body = rs.getString(5);
				Integer commentCount = rs.getInt(6);
				//LocalDateTime lastEditDate = rs.getObject (7, LocalDateTime.class);
				//String lastEditDateFormat = FormatDate.relativize(lastEditDate);
				
				String ownerStudentCode = rs.getString(8);
				String tag1 = rs.getString(9);
				String tag2 = rs.getString(10);
				String tag3 = rs.getString(11);
				String tag4 = rs.getString(12);
				post = new Post(id, creationDateFormat, score, title, body, commentCount, ownerStudentCode, tag1, tag2, tag3, tag4);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}
		return post;
	}
	
	public boolean insertPost(Post post) {		
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "INSERT IGNORE INTO tblpost(creationDate, score, title, body, commentCount, ownerStudentCode) values(NOW(),0,?,?,0,?)";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst=conn.prepareStatement(sqlCommand);
			pst.setString(1, post.getTitle());
			pst.setString(2, post.getBody());
			pst.setString(3, post.getOwnerStudentCode());
			//pst.setString(4, post.getOwnerDisplayName());
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
	
	public boolean updatePost(Post post) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "UPDATE tblpost SET title=?, body=?, lastEditDate=NOW()  WHERE postId='"+ post.getPostId() + "'";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst=conn.prepareStatement(sqlCommand);
			pst.setString(1, post.getTitle());
			pst.setString(2, post.getBody());
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

	public boolean deletePost(int postId) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "DELETE FROM tblpost WHERE postid='"+ postId +"'";
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

	public boolean commentCount(String postId, int commentCount) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "UPDATE tblpost SET commentCount='" + commentCount + "' WHERE postId="+ postId;
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
	
	public boolean score(String postId, int score) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "UPDATE tblpost SET score='"+ score +"' WHERE postId="+ postId;
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
