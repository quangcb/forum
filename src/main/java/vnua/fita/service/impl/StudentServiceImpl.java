package vnua.fita.service.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vnua.fita.entity.Student;
import vnua.fita.model.DBConnection;
import vnua.fita.service.StudentService;

public class StudentServiceImpl  implements StudentService{
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection conn;

	public StudentServiceImpl(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}

	public Student findStudent(String account) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "SELECT * FROM tblstudent WHERE student_code='" + account + "'";
		PreparedStatement pst = null;
		Student student = null;
		try {
			pst = conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			rs.next();
			String student_code = rs.getString(1);
			String password = rs.getString(2);
			String name = rs.getString(3);
			String phone = rs.getString(6);
			String email = rs.getString(7);
			Boolean gender = rs.getBoolean(8);
			Date birthday = rs.getDate(9);
			Date creationDate = rs.getDate(12);
			String about = rs.getString(13);
			String profileImageUrl = rs.getString(14);
			student = new Student(student_code, password, name, phone, email, gender, birthday, creationDate, about, profileImageUrl);
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}
		return student;
	}

	public List<Student> findStudentParent() {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "SELECT * FROM tblstudent";
		PreparedStatement pst = null;
		Student student = null;
		List<Student> students = new ArrayList<Student>();
		try {
			pst = conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String student_code = rs.getString(1);
				String parent_name = rs.getString(10);
				String parent_phone = rs.getString(11);
				student = new Student(student_code, parent_name, parent_phone);
				students.add(student);
				System.out.println("user: " + student_code);
				System.out.println("list user: " + students);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}
		return students;
	}

	public boolean changePassword(Student student) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "UPDATE tblstudent SET password=? WHERE student_code='" + student.getStudent_code() + "'";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst = conn.prepareStatement(sqlCommand);
			pst.setString(1, student.getPassword());
			result = pst.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}

		if (result == 1) {
			return true;
		}
		return false;
	}

	public boolean updateStudent(Student student) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "UPDATE tblstudent SET name=?, class=?, address=?, phone=?, email=?, parent_name=?, parent_phone=? WHERE student_code='"
				+ student.getStudent_code() + "'";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst = conn.prepareStatement(sqlCommand);
			pst.setString(1, student.getName());
			pst.setString(2, student.getStudent_class());
			pst.setString(3, student.getAddress());
			pst.setString(4, student.getPhone());
			pst.setString(5, student.getEmail());
			pst.setString(6, student.getParent_name());
			pst.setString(7, student.getParent_phone());
			result = pst.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}

		if (result == 1) {
			return true;
		}
		return false;
	}

	public boolean updateImage(Student student) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "UPDATE tblstudent SET profileImageUrl=? WHERE student_code='"
				+ student.getStudent_code() + "'";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst = conn.prepareStatement(sqlCommand);
			pst.setString(1, student.getProfileImageUrl());
			result = pst.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}

		if (result == 1) {
			return true;
		}
		return false;
	}
	
	public boolean delete(String student) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "DELETE FROM tblstudent WHERE student_code='" + student + "'";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst = conn.prepareStatement(sqlCommand);
//			pst.setString(1, student.getPassword());
			result = pst.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}

		if (result == 1) {
			System.out.println("delete: true");
			return true;
		}
		System.out.println("delete: false");
		return false;
	}
	
	public Student count(String studentCode) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "SELECT (select count(*) from tblpost p where p.ownerStudentCode =s.student_code) as countPost,"
						+" (select count(*) from training.tblcomment c where c.ownerStudentCode =s.student_code) as countComment"
						+" from training.tblstudent s where s.student_code="+ studentCode;
		PreparedStatement pst = null;
		Student student = null;
		try {
			pst = conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String countPost = rs.getString(1);
				String countComment = rs.getString(2);
				student = new Student(countPost, countComment);
				System.out.println("count post: " + countPost);
				System.out.println("count comment: " + countComment);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}
		
		return student;
	}
}
