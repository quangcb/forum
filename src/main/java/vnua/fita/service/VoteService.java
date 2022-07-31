package vnua.fita.service;

import vnua.fita.entity.Vote;

public interface VoteService {
	
	public boolean check(String postId, String commentId, String student);
	
	public boolean insert(Vote vote);
	
	public boolean delete(String postId, String commentId, String studentCode);
	
}
