package vnua.fita.service;

import java.util.List;

import vnua.fita.entity.Student;

public interface StudentService {

	/** find user by account **/
	public Student findStudent(String account);
	
	/** find info parent user by account **/
	public List<Student> findStudentParent();
	
	/** update user **/
	public boolean updateStudent(Student student);

	/** update image student profile **/
	public boolean updateImage(Student student);
	
	/** change the password **/
	public boolean changePassword(Student student);
	
	/** delete student **/
	public boolean delete(String student);

	public Student count(String student);
}
