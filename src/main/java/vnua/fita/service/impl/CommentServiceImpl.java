package vnua.fita.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import util.FormatDate;
import vnua.fita.entity.Comment;
import vnua.fita.model.DBConnection;
import vnua.fita.service.CommentService;


public class CommentServiceImpl implements CommentService{

	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection conn;
	
	// Constructor với các tham số để kết nối 
	public CommentServiceImpl(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}
	
	public List<Comment> findAll(String postid){
		List<Comment> result = new LinkedList<Comment>();
		PreparedStatement pst = null;
		String sqlCommand ="Select c.*, s.name, s.profileImageUrl, b.creationDate as creationDateParent, b.text as textParent, r.name as nameParent, b.ownerStudentCode as ownerStudentCodeParent, r.profileImageUrl as profileImageUrlParent"
				+ " from tblcomment c"
				+ " inner join tblstudent s ON c.ownerStudentCode=s.student_code AND c.postId=" + postid
				+ " left join tblcomment b ON b.id=c.parentId"
				+ " left join tblstudent r ON b.ownerStudentCode = r.student_code"
				+ " ORDER BY creationDate";
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		int count=0;
		try {
			pst = conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String id = rs.getString(1);
				//Integer postId = rs.getInt(2);
				LocalDateTime creationDate = rs.getObject (3, LocalDateTime.class);
				String creationDateFormat = FormatDate.relativize(creationDate);
				Integer score = rs.getInt(4);
				String text = rs.getString(5);
				Integer commentCount = rs.getInt(6);
				//LocalDateTime lastEditDate = rs.getObject (6, LocalDateTime.class);
				//String lastEditDateFormat = FormatDate.relativize(lastEditDate);
				String ownerStudentCode = rs.getString(8);
				String ownerDisplayName = rs.getString(10);
				String profileImageUrl = rs.getString(11);
				
				LocalDateTime creationDateParent = rs.getObject (12, LocalDateTime.class);
				String creationDateFormatParent=null;
				if(creationDateParent!=null)
					creationDateFormatParent = FormatDate.relativize(creationDateParent);
				String textParent = rs.getString(13);
				String ownerDisplayNameParent = rs.getString(14);
				String ownerStudentCodeParent = rs.getString(15);
				String profileImageUrlParent = rs.getString(16);
				count++;
				System.out.println("STT: "+count);
				System.out.println("creationDate: "+creationDateFormat);
				System.out.println("text: "+text);
				System.out.println("ownerDisplayName: "+ownerDisplayName);
				System.out.println("ownerStudentCode: "+ownerStudentCode);
				System.out.println("profileImageUrl: "+profileImageUrl);
				System.out.println("creationDateP: "+creationDateFormatParent);
				System.out.println("---------------------");
				System.out.println("textP: "+textParent);
				System.out.println("ownerDisplayNameP: "+ownerDisplayNameParent);
				System.out.println("ownerStudentCodeP: "+ownerStudentCodeParent);
				System.out.println("profileImageUrlP: "+profileImageUrlParent);
				System.out.println("---------------------------------------------");
				Comment comment = new Comment(id, creationDateFormat, score, text, commentCount, ownerStudentCode, ownerDisplayName, profileImageUrl, creationDateFormatParent, textParent, ownerDisplayNameParent, ownerStudentCodeParent, profileImageUrlParent);
				result.add(comment);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closeStatement(pst);
		}
		return result;
	}
	
	public List<Comment> findAll(){
		List<Comment> result = new LinkedList<Comment>();
		PreparedStatement pst = null;
		String sqlCommand ="SELECT c.id, c.creationDate, c.text, c.ownerStudentCode, s.name"
				+ " FROM tblcomment c, tblstudent s "
				+ " WHERE c.ownerStudentCode=s.student_code"
				+ " ORDER BY creationDate";
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		try {
			pst = conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String id = rs.getString(1);
				LocalDateTime creationDate = rs.getObject (2, LocalDateTime.class);
				String creationDateFormat = FormatDate.relativize(creationDate);
				String text = rs.getString(3);
				//LocalDateTime lastEditDate = rs.getObject (6, LocalDateTime.class);
				//String lastEditDateFormat = FormatDate.relativize(lastEditDate);
				String ownerStudentCode = rs.getString(4);
				String ownerDisplayName = rs.getString(5);
				Comment comment = new Comment(id, creationDateFormat, text, ownerStudentCode, ownerDisplayName);
				result.add(comment);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closeStatement(pst);
		}
		return result;
	}
	
	public Comment selectComment(String id) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "SELECT * FROM tblcomment WHERE id='" + id +"'";
		PreparedStatement pst = null;
		Comment comment = null;
		try {
			pst = conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String postId = rs.getString(2);
				Integer commentCount = rs.getInt(6);
				String ownerStudentCode = rs.getString(8);
				comment = new Comment(id, postId, commentCount, ownerStudentCode);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}
		return comment;
	}
	
	public boolean insertComment(Comment comment) {		
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "INSERT IGNORE INTO tblcomment(postId, creationDate, score, text, commentCount, ownerStudentCode, parentId) values(?,NOW(),0,?,0,?,?)";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst=conn.prepareStatement(sqlCommand);
			pst.setString(1, comment.getPostId());
			pst.setString(2, comment.getText());
			pst.setString(3, comment.getOwnerStudentCode());
			pst.setString(4, comment.getParentId());
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
	
	public boolean updateComment(Comment comment) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "UPDATE tblcomment SET text=?, lastEditDate=NOW() WHERE id='"+comment.getId()+"'";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst=conn.prepareStatement(sqlCommand);
			pst.setString(1, comment.getText());
			System.out.println("lkkkk: "+comment.getId());
			System.out.println("lkkkk: "+comment.getText());
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

	public boolean deleteComment(String id, String postid) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "DELETE FROM tblcomment WHERE";
		if(id!=null)
			sqlCommand += " id='"+ id + "'";
		if(postid !=null)
			sqlCommand += " postId='"+ postid +"'";
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
	
	public boolean commentCount(String id, int commentCount) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "UPDATE tblcomment SET commentCount='" + commentCount + "' WHERE id="+ id;
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
//	
//	public boolean score(String commentId, int score) {
//		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
//		String sqlCommand = "UPDATE tblcomment SET score='"+ score +"' WHERE id=" + commentId;
//		PreparedStatement pst = null;
//		int result = 0;
//		try {
//			pst=conn.prepareStatement(sqlCommand);
//			result = pst.executeUpdate();
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//		}finally {
//			DBConnection.closeConnect(conn);
//			DBConnection.closePreparedStatement(pst);
//		}
//		
//		if(result == 1) {
//			return true;
//		}
//		return false;
//	}
}
