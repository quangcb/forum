package vnua.fita.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import vnua.fita.entity.Student;
import vnua.fita.model.DBConnection;


/**
 * Dummy DAO class sample.
 * @author Hawk
 *
 */
public class RegistrationDao {
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection conn;
	
	public RegistrationDao(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}
	
	public boolean add(Student student){
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "INSERT IGNORE INTO tblstudent(student_code, password, name, class, address, phone, email, gender, birthday, creationDate, profileImageUrl) values(?,?,?,?,?,?,?,?,?, NOW(),?)";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst=conn.prepareStatement(sqlCommand);
			pst.setString(1, student.getStudent_code());
			pst.setString(2, student.getPassword());
			pst.setString(3, student.getName());
			pst.setString(4, student.getStudent_class());
			pst.setString(5, student.getAddress());
			pst.setString(6, student.getPhone());
			pst.setString(7, student.getEmail());
			pst.setBoolean(8, student.isGender());
			pst.setDate(9, new java.sql.Date(student.getBirthday().getTime()));
			pst.setString(10, student.getProfileImageUrl());			
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
