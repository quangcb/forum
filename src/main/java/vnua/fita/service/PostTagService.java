package vnua.fita.service;

public interface PostTagService {
	
	public boolean check(String tagName);
	
	public boolean insertPostTag(String postId, String tagId, int count);
	
	public boolean insertTag(String tagName);
	
	public boolean delete(String postId, String tagId);
	
	public Integer getId(String tagName);
}
