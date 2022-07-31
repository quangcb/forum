package vnua.fita.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import vnua.fita.entity.Notification;
import vnua.fita.model.DBConnection;
import vnua.fita.service.NotificationService;

public class NotificationServiceImpl  implements NotificationService{
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection conn;

	public NotificationServiceImpl(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}

	public String count(String studentCode) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "SELECT count(*) from tblnotification WHERE status=0 and student_code="+ studentCode;
		PreparedStatement pst = null;
		String countNoti=null;
		try {
			pst = conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				countNoti = rs.getString(1);
				return countNoti;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}
		return countNoti;
	}

	
	public boolean insert(Notification notification) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "INSERT IGNORE INTO tblnotification(postTitle, commentText, text, status, student_code) values(?,?,?,0,?)";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst=conn.prepareStatement(sqlCommand);
			pst.setString(1, notification.getPostTitle());
			pst.setString(2, notification.getCommentText());
			pst.setString(3, notification.getText());
			pst.setString(4, notification.getStudent_Code());
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
	
	public List<Notification> notify(String studentCode) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "SELECT * FROM tblnotification where student_code="+ studentCode;
		PreparedStatement pst = null;
		List<Notification> result = new LinkedList<Notification>();
		Notification noti = null;
		try {
			pst = conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String postTitle = rs.getString(2);
				String commentText = rs.getString(3);
				String text = rs.getString(4);
				noti = new Notification(postTitle, commentText, text);
				System.out.println("postTitle: " + postTitle);
				System.out.println("commentText: " + commentText);
				result.add(noti);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}
		
		return result;
	}
	
	public boolean updateNotification(String studentCode) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "UPDATE tblnotification SET status=1 WHERE student_code='"+ studentCode + "'";
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
