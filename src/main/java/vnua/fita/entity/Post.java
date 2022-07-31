package vnua.fita.entity;

public class Post {
	
	private String postId;
	private String creationDate;
	private int score;
	private String title;
	private String body;
	private int commentCount;
	private String lastEditDate;
	private String ownerStudentCode;
	private String ownerDisplayName;
	private String profileImageUrl;
	private String tag1;
	private String tag2;
	private String tag3;
	private String tag4;

	public Post() {
		
	}

	public Post(String postId, String creationDate, int score, String title, String body, int commentCount,
			String ownerStudentCode, String ownerDisplayName, String profileImageUrl) {
		this.postId = postId;
		this.creationDate = creationDate;
		this.score = score;
		this.title = title;
		this.body = body;
		this.commentCount = commentCount;
		//this.lastEditDate = lastEditDate;
		this.ownerStudentCode = ownerStudentCode;
		this.ownerDisplayName = ownerDisplayName;
		this.profileImageUrl = profileImageUrl;
	}
	
	public Post(String postId, String creationDate, int score, String title, String body, int commentCount,
			String ownerStudentCode, String ownerDisplayName, String profileImageUrl, String tag1, String tag2, String tag3, String tag4) {
		this.postId = postId;
		this.creationDate = creationDate;
		this.score = score;
		this.title = title;
		this.body = body;
		this.commentCount = commentCount;
		//this.lastEditDate = lastEditDate;
		this.ownerStudentCode = ownerStudentCode;
		this.ownerDisplayName = ownerDisplayName;
		this.profileImageUrl = profileImageUrl;
		this.tag1 =	tag1;
		this.tag2 =	tag2;
		this.tag3 =	tag3;
		this.tag4 =	tag4;
	}
	
	public Post(String postId, String creationDate, int score, String title, String body, int commentCount,
			String ownerStudentCode, String tag1, String tag2, String tag3, String tag4) {
		this.postId = postId;
		this.creationDate = creationDate;
		this.score = score;
		this.title = title;
		this.body = body;
		this.commentCount = commentCount;
		//this.lastEditDate = lastEditDate;
		this.ownerStudentCode = ownerStudentCode;
		this.tag1 = tag1;
		this.tag2 = tag2;
		this.tag3 = tag3;
		this.tag4 = tag4;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postid) {
		this.postId = postid;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getLastEditDate() {
		return lastEditDate;
	}

	public void setLastEditDate(String lastEditDate) {
		this.lastEditDate = lastEditDate;
	}

	public String getOwnerStudentCode() {
		return ownerStudentCode;
	}

	public void setOwnerStudentCode(String ownerStudentCode) {
		this.ownerStudentCode = ownerStudentCode;
	}

	public String getOwnerDisplayName() {
		return ownerDisplayName;
	}

	public void setOwnerDisplayName(String ownerDisplayName) {
		this.ownerDisplayName = ownerDisplayName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getTag1() {
		return tag1;
	}

	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}

	public String getTag2() {
		return tag2;
	}

	public void setTag2(String tag2) {
		this.tag2 = tag2;
	}

	public String getTag3() {
		return tag3;
	}

	public void setTag3(String tag3) {
		this.tag3 = tag3;
	}

	public String getTag4() {
		return tag4;
	}

	public void setTag4(String tag4) {
		this.tag4 = tag4;
	}

}
