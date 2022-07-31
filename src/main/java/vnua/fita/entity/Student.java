package vnua.fita.entity;

import java.util.Date;

public class Student {
	
	private String student_code;
	private String password;
	private String name;
	private String student_class;
	private String address;
	private String phone;
	private String email;
	private boolean gender;
	private Date birthday;
	private String parent_name;
	private String parent_phone;
	private Date creationDate;
	private String about;
	private String profileImageUrl;

	private String countPost;
	private String countComment;
	public Student() {
		
	}
	
	public Student(String countPost, String countComment) {
		this.countPost = countPost;
		this.countComment = countComment;
	}

	public Student(String student_code, String parent_name, String parent_phone) {
		this.student_code = student_code;
		this.parent_name = parent_name;
		this.parent_phone = parent_phone;
	}

	public Student(String student_code, String password, String name, String phone, String email,
			boolean gender, Date birthday, Date creationDate, String about, String profileImageUrl) {
		this.student_code = student_code;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.gender = gender;
		this.birthday = birthday;
		this.creationDate = creationDate;
		this.about = about;
		this.profileImageUrl = profileImageUrl;
	}

	public String getStudent_code() {
		return student_code;
	}

	public void setStudent_code(String student_code) {
		this.student_code = student_code;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStudent_class() {
		return student_class;
	}

	public void setStudent_class(String student_class) {
		this.student_class = student_class;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getParent_name() {
		return parent_name;
	}

	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}

	public String getParent_phone() {
		return parent_phone;
	}

	public void setParent_phone(String parent_phone) {
		this.parent_phone = parent_phone;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getCountPost() {
		return countPost;
	}

	public void setCountPost(String countPost) {
		this.countPost = countPost;
	}

	public String getCountComment() {
		return countComment;
	}

	public void setCountComment(String countComment) {
		this.countComment = countComment;
	}

}
