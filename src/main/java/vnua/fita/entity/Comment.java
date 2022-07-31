package vnua.fita.entity;

public class Comment {
	
	private String id;
	private String postId;
	private String creationDate;
	private int score;
	private String text;
	private int commentCount;
	private String lastEditDate;
	private String ownerStudentCode;
	private String parentId;
	private String ownerDisplayName;
	private String profileImageUrl;
	
	private String creationDateParent;
	private String textParent;
	private String ownerDisplayNameParent;
	private String ownerStudentCodeParent;
	private String profileImageUrlParent;

	public Comment() {
		
	}
	
	public Comment(String id, String postId, int commentCount, String ownerStudentCode) {
		this.id = id;
		this.postId = postId;
		this.commentCount = commentCount;
		this.ownerStudentCode = ownerStudentCode;
	}
	
	public Comment(String id, String creationDate, String text, String ownerStudentCode, String ownerDisplayName) {
		this.id = id;
		this.creationDate = creationDate;
		this.text = text;
		this.ownerStudentCode = ownerStudentCode;
		this.ownerDisplayName = ownerDisplayName;
	}

	public Comment(String id, String creationDate, int score, String text, int commentCount, String ownerStudentCode, String ownerDisplayName, String profileImageUrl, String creationDateParent, String textParent, String ownerDisplayNameParent, String ownerStudentCodeParent, String profileImageUrlParent) {
		this.id = id;
		this.creationDate = creationDate;
		this.score = score;
		this.text = text;
		this.commentCount = commentCount;
		//this.parentId = parentId;
		//this.lastEditDate = lastEditDate;
		this.ownerStudentCode = ownerStudentCode;
		this.ownerDisplayName = ownerDisplayName;
		this.profileImageUrl = profileImageUrl;
		
		this.creationDateParent = creationDateParent;
		this.textParent = textParent;
		this.ownerDisplayNameParent = ownerDisplayNameParent;
		this.ownerStudentCodeParent = ownerStudentCodeParent;
		this.profileImageUrlParent = profileImageUrlParent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public String getCreationDateParent() {
		return creationDateParent;
	}

	public void setCreationDateParent(String creationDateParent) {
		this.creationDateParent = creationDateParent;
	}

	public String getTextParent() {
		return textParent;
	}

	public void setTextParent(String textParent) {
		this.textParent = textParent;
	}
	
	public String getOwnerDisplayNameParent() {
		return ownerDisplayNameParent;
	}

	public void setOwnerDisplayNameParent(String ownerDisplayNameParent) {
		this.ownerDisplayNameParent = ownerDisplayNameParent;
	}
	
	public String getOwnerStudentCodeParent() {
		return ownerStudentCodeParent;
	}

	public void setOwnerStudentCodeParent(String ownerStudentCodeParent) {
		this.ownerStudentCodeParent = ownerStudentCodeParent;
	}

	public String getProfileImageUrlParent() {
		return profileImageUrlParent;
	}

	public void setProfileImageUrlParent(String profileImageUrlParent) {
		this.profileImageUrlParent = profileImageUrlParent;
	}


}
