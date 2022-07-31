package vnua.fita.entity;

public class Vote {
	
	private String student_code;
	private String postId;
	private String commentId;

	public Vote() {
		
	}

	public Vote(String student_code, String postId, String commentId) {
		super();
		this.student_code = student_code;
		this.postId = postId;
		this.commentId = commentId;
	}

	public String getStudent_code() {
		return student_code;
	}

	public void setStudent_code(String student_code) {
		this.student_code = student_code;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	
}
