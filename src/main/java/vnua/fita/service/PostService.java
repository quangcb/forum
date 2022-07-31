package vnua.fita.service;

import java.util.List;

import vnua.fita.entity.Post;

public interface PostService {
	
	public List<Post> search(String studentCode, String tagName, String keyword);

	public Post selectPost(String postId, String studentCode);
	
	public boolean insertPost(Post post);
	
	public boolean updatePost(Post post);
	
	public boolean deletePost(int postId);
	
	public boolean commentCount(String postId, int commentCount);	//update comment count
	
	public boolean score(String postId, int score);	//update score
	
}
