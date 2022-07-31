package vnua.fita.controller;

import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import vnua.fita.entity.Comment;
import vnua.fita.entity.Post;
import vnua.fita.entity.Student;
import vnua.fita.entity.UserCredential;
import vnua.fita.service.AuthenticationService;
import vnua.fita.service.CommentService;
import vnua.fita.service.PostService;
import vnua.fita.service.PostTagService;
import vnua.fita.service.StudentService;
import vnua.fita.service.VoteService;
import vnua.fita.service.impl.AuthenticationServiceImpl;
import vnua.fita.service.impl.CommentServiceImpl;
import vnua.fita.service.impl.PostServiceImpl;
import vnua.fita.service.impl.PostTagServiceImpl;
import vnua.fita.service.impl.StudentServiceImpl;
import vnua.fita.service.impl.VoteServiceImpl;

public class UpdatePostController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	@Wire
	private Label tag1;
	@Wire
	private Label tag2;
	@Wire
	private Label tag3;
	@Wire
	private Label tag4;
	@Wire
	private Div boxtag1;
	@Wire
	private Div boxtag2;
	@Wire
	private Div boxtag3;
	@Wire
	private Div boxtag4;
	@Wire
	private Textbox title; 
	@Wire
	private CKeditor editor;
	@Wire
	private Button btnUpdatePost;
	@Wire
	private CKeditor editorComment;
	@Wire
	private Button btnUpdateComment;

	private String postId = Executions.getCurrent().getParameter("postid");
	@WireVariable
	AuthenticationService authService = new AuthenticationServiceImpl();
	@WireVariable
	StudentService studentService = new StudentServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");

	private CommentService commentService = new CommentServiceImpl("jdbc:mysql://localhost:3306/training", "root",
			"123456");
	private PostService postService = new PostServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");
	private VoteService voteService = new VoteServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");
	private PostTagService postTagService = new PostTagServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");

	private static String commentId;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UserCredential cre = authService.getUserCredential();
		if (!cre.getAccount().equals("anonymous")) {
			Post post = postService.selectPost(postId, null);
			try {
				title.setValue(post.getTitle());
				editor.setValue(post.getBody());
				if(post.getTag1()!=null) {
					tag1.setValue(post.getTag1());
					boxtag1.removeSclass("tag-close");
				}
				if(post.getTag2()!=null) {
					tag2.setValue(post.getTag2());
					boxtag2.removeSclass("tag-close");
				}
				if(post.getTag3()!=null) {
					tag3.setValue(post.getTag3());
					boxtag3.removeSclass("tag-close");
				}
				if(post.getTag4()!=null) {
					tag4.setValue(post.getTag4());
					boxtag4.removeSclass("tag-close");
				}
			} catch(Exception e) {}
		}
	}

	@Listen("onClick=#more-btn-edit-post")
	public void showModalEditPost() {
		Student student = null;
		UserCredential cre = authService.getUserCredential();
		if (!cre.getAccount().equals("anonymous")) {
			student = studentService.findStudent(cre.getAccount());
		}
		Post post = postService.selectPost(postId, null);
		if (student != null && student.getStudent_code().equals(post.getOwnerStudentCode())) {
			Window window = (Window)Executions.createComponents("/components/editor/editPost.zul", null, null);
			window.doModal();
		} else Messagebox.show("Bạn không phải chủ nhân của bài đăng này", new Messagebox.Button[0], null);
	}
	
	@Listen("onChange=#commentListbox")
	public void showModalEditComment(ForwardEvent evt) {	
		Student student = null;
		UserCredential cre = authService.getUserCredential();
		if (!cre.getAccount().equals("anonymous")) {
			student = studentService.findStudent(cre.getAccount());
		}
//		Post post = postService.selectPost(postId, null);
		Button replyComment = (Button) evt.getOrigin().getTarget();
		String parentId = replyComment.getId();
		String replyId = parentId.replace("btnReply","");
		System.out.println("parent-ID-reply: " + replyId);
		commentId = replyId.replaceAll("more-btn-edit", "");
		Comment com = commentService.selectComment(commentId);
		if (student != null && student.getStudent_code().equals(com.getOwnerStudentCode())) {
			Window window = (Window)Executions.createComponents("/components/editor/editComment.zul", null, null);
	        window.doModal();
		}
        else Messagebox.show("Bạn không phải chủ nhân của bình luận này", new Messagebox.Button[0], null);
	}
	
	@Listen("onClick=#btnUpdateComment")
	public void editComment() {
		Student student = null;
		UserCredential cre = authService.getUserCredential();
		if (!cre.getAccount().equals("anonymous")) {
			student = studentService.findStudent(cre.getAccount());
		}

		System.out.println("commentId:"+commentId);
		Comment com = commentService.selectComment(commentId);
		System.out.println("123123: "+com.getId());
		if (student != null && student.getStudent_code().equals(com.getOwnerStudentCode())) {
			com.setText(editorComment.getValue());
			boolean result = commentService.updateComment(com);
			if (result)	{
				Messagebox.show("Bạn đã sửa bình luận thành công", new Messagebox.Button[0], null);
				Executions.sendRedirect("/post.zul?postid=" + com.getPostId());
			} else Messagebox.show("Sửa thất bại", new Messagebox.Button[0], null);
		}
	}
	
	@Listen("onDelete=#commentListbox")
	public void deleteComment(ForwardEvent evt) {	
		Button deleteComment = (Button) evt.getOrigin().getTarget();
		String parentId = deleteComment.getId();
		String replyId = parentId.replace("btnDelete: ","");
		System.out.println("parent-ID-reply: " + replyId);
		String commentId = replyId.replaceAll("more-btn-delete", "");
		Student student = null;
		UserCredential cre = authService.getUserCredential();
		if (!cre.getAccount().equals("anonymous")) {
			student = studentService.findStudent(cre.getAccount());
		} 

		Comment com = commentService.selectComment(commentId);
		if (student != null && student.getStudent_code().equals(com.getOwnerStudentCode())) {
			boolean result = commentService.deleteComment(commentId, null);
			if (result) {
				Messagebox.show("Bạn đã xoá bình luận thành công", new Messagebox.Button[0], null);
				Post post = postService.selectPost(postId, null);
				postService.commentCount(postId, post.getCommentCount() - 1);
				commentService.commentCount(com.getId(), com.getCommentCount() - 1);
			}
			else Messagebox.show("Xoá thất bại", new Messagebox.Button[0], null);
			Executions.sendRedirect("/post.zul?postid=" + postId + "#comment");
		} else Messagebox.show("Bạn không phải chủ nhân của bình luận này", new Messagebox.Button[0], null);
	}
	
	@Listen("onClick=#btnUpdatePost")
	public void editPost() {		
		Student student = null;
		UserCredential cre = authService.getUserCredential();
		if (!cre.getAccount().equals("anonymous")) {
			student = studentService.findStudent(cre.getAccount());
		}

		Post post = postService.selectPost(postId, null);
		System.out.println("1111: "+ post.getPostId());
		if (student != null && student.getStudent_code().equals(post.getOwnerStudentCode())) {
			post.setTitle(title.getValue());
			
			Post ownerPost = postService.selectPost(null, student.getStudent_code());
			String addTag1 = tag1.getValue().replaceAll("#", "").toLowerCase();
			String addTag2 = tag2.getValue().replaceAll("#", "").toLowerCase();
			String addTag3 = tag3.getValue().replaceAll("#", "").toLowerCase();
			String addTag4 = tag4.getValue().replaceAll("#", "").toLowerCase();

			
			if(!postTagService.check(addTag1) && !addTag1.equals("") && postTagService.getId(addTag1)!=null) {
				postTagService.insertTag(addTag1);
			}
			
			if(!postTagService.check(addTag2) && !addTag2.equals("")) {
				postTagService.insertTag(addTag2);
			}
			if(!postTagService.check(addTag3) && !addTag3.equals("")) {
				postTagService.insertTag(addTag3);
			}
			if(!postTagService.check(addTag4) && !addTag4.equals("")) {
				postTagService.insertTag(addTag4);
			}
			
			postTagService.delete(post.getPostId(), null);
			if(postTagService.check(addTag1) && !addTag1.equals(""))
				postTagService.insertPostTag(ownerPost.getPostId(), Integer.toString(postTagService.getId(addTag1)), 1);
			if(postTagService.check(addTag2) && !addTag2.equals(""))
				postTagService.insertPostTag(ownerPost.getPostId(), Integer.toString(postTagService.getId(addTag2)), 2);
			if(postTagService.check(addTag3) && !addTag3.equals(""))
				postTagService.insertPostTag(ownerPost.getPostId(), Integer.toString(postTagService.getId(addTag3)), 3);
			if(postTagService.check(addTag4) && !addTag4.equals(""))
				postTagService.insertPostTag(ownerPost.getPostId(), Integer.toString(postTagService.getId(addTag4)), 4);
			boolean result = postService.updatePost(post);
			
			if (result) {
				Messagebox.show("Bạn đã sửa bài đăng thành công", new Messagebox.Button[0], null);
				Executions.sendRedirect("/post.zul?postid=" + post.getPostId());
			}
			else Messagebox.show("Sửa thất bại", new Messagebox.Button[0], null);
		} else Messagebox.show("Bạn không phải chủ nhân của bài đăng này", new Messagebox.Button[0], null);

	}
	
	@Listen("onClick=#more-btn-delete-post")
	public void deletePost() {	
		Student student = null;
		UserCredential cre = authService.getUserCredential();
		if (!cre.getAccount().equals("anonymous")) {
			student = studentService.findStudent(cre.getAccount());
		}

		Post post = postService.selectPost(postId, null);
		
		if (student != null && student.getStudent_code().equals(post.getOwnerStudentCode())) {
			commentService.deleteComment(null, postId);
			voteService.delete(postId, null, null);
			postTagService.delete(postId, null);
			boolean result = postService.deletePost(Integer.parseInt(postId));
			if (result) {
				Messagebox.show("Bạn đã xoá bài đăng thành công", new Messagebox.Button[0], null);
				Executions.sendRedirect("/");
			}
			else Messagebox.show("Xoá thất bại", new Messagebox.Button[0], null);
		} else Messagebox.show("Bạn không phải chủ nhân của bài đăng này", new Messagebox.Button[0], null);
	}
}
