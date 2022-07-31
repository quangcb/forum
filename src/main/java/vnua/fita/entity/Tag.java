package vnua.fita.entity;

public class Tag {
	
	private String id;
	private String postId;
	private String tagName;

	public Tag() {
		
	}
	
	public Tag(String id, String postId, String tagName) {
		this.id= id;
		this.postId = postId;
		this.tagName = tagName;
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

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
}
