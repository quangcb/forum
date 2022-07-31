package vnua.fita.service;

import java.util.List;

import vnua.fita.entity.Comment;

public interface CommentService {
	
	public List<Comment> findAll(String postid);
	
	public List<Comment> findAll(); // findAll by admin
	
	public Comment selectComment(String id);
	
	public boolean insertComment(Comment comment);
	
	public boolean updateComment(Comment comment);
	
	public boolean deleteComment(String id, String postid);
	
	public boolean commentCount(String postId, int commentCount);	//update comment count
//	
//	public boolean score(String postId, int score);	//update score
}
