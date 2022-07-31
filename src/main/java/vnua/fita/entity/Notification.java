package vnua.fita.entity;

public class Notification {
	private int id;
	private String postTitle;
	private String commentText;
	private String text;
	private String student_Code;
	
	public Notification() {
		
	}

	public Notification(String postTitle, String commentText, String text) {
		this.postTitle = postTitle;
		this.commentText = commentText;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getStudent_Code() {
		return student_Code;
	}

	public void setStudent_Code(String student_Code) {
		this.student_Code = student_Code;
	}

}
