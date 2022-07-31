package vnua.fita.controller;

import java.util.List;

import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import util.ModalUtil;
import vnua.fita.entity.Comment;
import vnua.fita.entity.Post;
import vnua.fita.entity.Student;
import vnua.fita.entity.UserCredential;
import vnua.fita.entity.Vote;
import vnua.fita.service.AuthenticationService;
import vnua.fita.service.CommentService;
import vnua.fita.service.PostService;
import vnua.fita.service.StudentService;
import vnua.fita.service.VoteService;
import vnua.fita.service.impl.AuthenticationServiceImpl;
import vnua.fita.service.impl.CommentServiceImpl;
import vnua.fita.service.impl.PostServiceImpl;
import vnua.fita.service.impl.StudentServiceImpl;
import vnua.fita.service.impl.VoteServiceImpl;

public class PostController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	@WireVariable
	private List<Comment> comments;
	@Wire
	private Image postAvatar;
	@Wire
	private A postProfile;
	@Wire
	private Label postTimestamp;
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
	private Label postTitle;
	@Wire("#btnLike")
	private Button btnLike;
	@Wire("#btnUnlike")
	private Button btnUnlike;
	@Wire
	private Html postBody;
	@Wire
	private CKeditor ed;

	private String postId = Executions.getCurrent().getParameter("postid");
	@WireVariable
	AuthenticationService authService = new AuthenticationServiceImpl();
	@WireVariable
	StudentService studentService = new StudentServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");

	private CommentService commentService = new CommentServiceImpl("jdbc:mysql://localhost:3306/training", "root",
			"123456");
	private PostService postService = new PostServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");
	private VoteService voteService = new VoteServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		showPost();
		btnUnlike.setVisible(false); // --- ktra vote
		UserCredential cre = authService.getUserCredential();
		Student student = null;
		if (!cre.getAccount().equals("anonymous")) {
			student = studentService.findStudent(cre.getAccount());
			Boolean result1 = voteService.check(postId, null, student.getStudent_code());
			System.out.println("result: " + result1);
			if(result1){
				btnLike.setVisible(false);
				btnUnlike.setVisible(true);
			}
		}
	}

	// --- Truyền danh sách comments sang listbox
	public ListModel<Comment> getComments() {
		comments = commentService.findAll(postId);
		return new ListModelList<Comment>(comments);
	}
	
	// --- Hiển thị bài post
	public void showPost() {
		String postId = (String) Executions.getCurrent().getParameter("postid");
		System.out.println("post id: " + postId);
		if (postId != null) {
			Post post = postService.selectPost(postId, null);

			// --- Hiển thị thông tin bài post
			if (post != null) {
				// --- Đặt title cho trang web
				String pageTitle = post.getTitle();
				Executions.getCurrent().getDesktop().getFirstPage().setTitle(pageTitle + " - Fita's Forum");

				Student student = studentService.findStudent(post.getOwnerStudentCode());
				try {
					postAvatar.setSrc(student.getProfileImageUrl());
					postProfile.setLabel(student.getName());
					postProfile.setHref("profile.zul?id="+student.getStudent_code());
					postTimestamp.setValue(post.getCreationDate());
					postTitle.setValue(post.getTitle());
					postBody.setContent(post.getBody());
					if(post.getTag1()!=null) {
						tag1.setValue("#"+post.getTag1());
						boxtag1.removeSclass("tag-close");
					}
					if(post.getTag2()!=null) {
						tag2.setValue("#"+post.getTag2());
						boxtag2.removeSclass("tag-close");
					}
					if(post.getTag3()!=null) {
						tag3.setValue("#"+post.getTag3());
						boxtag3.removeSclass("tag-close");
					}
					if(post.getTag4()!=null) {
						tag4.setValue("#"+post.getTag4());
						boxtag4.removeSclass("tag-close");
					}
				} catch (Exception e) {

				}
			} else {
				System.out.println("---------------    ko tìm thấy    ---------------------");
			}
		}
	}
	
//	@Listen("onSave=#editPost")
//	public void showModalEditPost() {
//		Student student = null;
//		UserCredential cre = authService.getUserCredential();
//		if (!cre.getAccount().equals("anonymous")) {
//			student = studentService.findStudent(cre.getAccount());
//		}
//
//		Post post = postService.selectPost(postId, null);
//		if (student != null && student.getStudent_code().equals(post.getOwnerStudentCode())) {
//			Window window = (Window)Executions.createComponents(
//          "/components/editor/editComment.zul", null, null);
//			window.doModal();
//			
//		}
//	}
	
	//--- Hiển thị phần reply comment
	@Listen("onSave=#commentListbox")
	public void showReply(ForwardEvent evt) {		
		Button replyComment = (Button) evt.getOrigin().getTarget();
		String parentId = replyComment.getId();
		String replyId = parentId.replace("btnReply","");
		System.out.println("parent-ID-reply: " + replyId);
		Div div = (Div) Path.getComponent("/reply"+replyId);
		String style = div.getStyle();
		if(style.equals("display: none;")) {
			div.setStyle("display: block;");
		}
		if(style.equals("display: block;"))
			div.setStyle("display: none;");
	}
	
	// --- Tạo comment
	@Listen("onClick=.createCommentBtn")
	public void doCreateComment() {
		String edt = ed.getValue();
		System.out.println("input: " + edt);
		Student student = null;
		Comment comment = new Comment();
		comment.setPostId(postId);
		comment.setText(ed.getValue());
		comment.setParentId(null);

		UserCredential cre = authService.getUserCredential();
		if (!cre.getAccount().equals("anonymous")) {
			student = studentService.findStudent(cre.getAccount());
		}
		if (student != null) {
			comment.setOwnerStudentCode(student.getStudent_code());
			boolean result = commentService.insertComment(comment);
			if (result) {
				Messagebox.show("Bạn đã bình luận thành công", new Messagebox.Button[0], null);
				Post post = postService.selectPost(postId, null);
				postService.commentCount(postId, post.getCommentCount() + 1);
				Executions.sendRedirect("/post.zul?postid=" + comment.getPostId() + "#comment");
				System.out.println(
						"--- Tạo bình luận: [postid] = " + postId + " - [parentid] = " + comment.getParentId());
			} else {
				Messagebox.show("Thất bại!");
			}
		} else {
			ModalUtil.showModalLogin(null);
		}
	}

	// --- Tạo comment reply
	@Listen("onEdit=#commentListbox")
	public void doCreateChildComment(ForwardEvent evt) {
		Button createComment = (Button) evt.getOrigin().getTarget();
		String parentId = createComment.getId();
		System.out.println("parent-ID: " + parentId);
		CKeditor ck = (CKeditor) Path.getComponent("/ed"+parentId);
		System.out.println("---text-ckeditor: "+ck.getValue());
		String text = ck.getValue();
		Student student = null;
		Comment comment = new Comment();
		comment.setPostId(postId);
		comment.setText(text);
		comment.setParentId(parentId);

		// nếu là trả lời bình luận thì tăng commentCount của comment
		if (parentId != null) {
			Comment com = commentService.selectComment(parentId); // ---dòng 98
			commentService.commentCount(com.getId(), com.getCommentCount() + 1);
		}

		UserCredential cre = authService.getUserCredential();
		if (!cre.getAccount().equals("anonymous")) {
			student = studentService.findStudent(cre.getAccount());
		}
		if (student != null) {
			comment.setOwnerStudentCode(student.getStudent_code());
			boolean result = commentService.insertComment(comment);
			if (result) {
				Messagebox.show("Bạn đã bình luận thành công", new Messagebox.Button[0], null);
				Post post = postService.selectPost(postId, null);
				postService.commentCount(postId, post.getCommentCount() + 1);
				Executions.sendRedirect("/post.zul?postid=" + comment.getPostId() + "#comment");
				System.out.println(
						"--- Tạo bình luận: [postid] = " + postId + " - [parentid] = " + comment.getParentId());
			} else {
				Messagebox.show("Thất bại!");
			}
		} else {
			ModalUtil.showModalLogin(null);
		}
	}
		
	@Listen("onClick=#btnLike")
	public void clickButtonLike() {
		UserCredential cre = authService.getUserCredential();
		Student student = null;
		if (!cre.getAccount().equals("anonymous"))
			student = studentService.findStudent(cre.getAccount());
		if (student != null) {
			Post post = postService.selectPost(postId, null);
			Boolean result = voteService.check(postId, null, student.getStudent_code());
			if(!result) {
				Vote vote = new Vote();
				vote.setPostId(postId);;
				vote.setCommentId(null);
				vote.setStudent_code(student.getStudent_code());
				voteService.insert(vote);
				btnUnlike.setVisible(true);
				btnLike.setVisible(false);
				postService.score(postId, post.getScore() + 1);
				System.out.println("Like: " + postService.score(postId, post.getScore() + 1));
			}
		} else {
			ModalUtil.showModalLogin(null);
		}
	}

	@Listen("onClick=#btnUnlike")
	public void clickButtonUnlike() {
		UserCredential cre = authService.getUserCredential();
		Student student = null;
		if (!cre.getAccount().equals("anonymous"))
			student = studentService.findStudent(cre.getAccount());
		if (student != null) {
			Post post = postService.selectPost(postId, null);
			Boolean result = voteService.check(postId, null, student.getStudent_code());
			if(result) {
				voteService.delete(postId, null, student.getStudent_code());
				btnLike.setVisible(true);
				btnUnlike.setVisible(false);
				postService.score(postId, post.getScore() - 1);
				System.out.println("Unlike: " + postService.score(postId, post.getScore() - 1));
			}
		} else {
			ModalUtil.showModalLogin(null);
		}
	}
	
//	@Listen("onChange=#commentListbox")
//	public void editComment(ForwardEvent evt) {		
//		System.out.println("ok");
//		Button replyComment = (Button) evt.getOrigin().getTarget();
//		String parentId = replyComment.getId();
//		String replyId = parentId.replace("btnReply","");
//		System.out.println("parent-ID-reply: " + replyId);
////		Window window = (Window)Executions.createComponents(
////                "/components/editComment.zul", null, null);
////        window.doModal();
//	}
//	
//	@Listen("onDelete=#commentListbox")
//	public void deleteComment(ForwardEvent evt) {	
//		Button deleteComment = (Button) evt.getOrigin().getTarget();
//		String parentId = deleteComment.getId();
//		String replyId = parentId.replace("btnDelete: ","");
//		System.out.println("parent-ID-reply: " + replyId);
//		String commentId = replyId.replaceAll("more-btn-delete", "");
//		Student student = null;
//		UserCredential cre = authService.getUserCredential();
//		if (!cre.getAccount().equals("anonymous")) {
//			student = studentService.findStudent(cre.getAccount());
//		}
//
//		Comment com = commentService.selectComment(commentId);
//		if (student != null && student.getStudent_code().equals(com.getOwnerStudentCode())) {
//			boolean result = commentService.deleteComment(commentId, null);
//			if (result) {
//				Messagebox.show("Bạn đã xoá bình luận thành công", new Messagebox.Button[0], null);
//				Post post = postService.selectPost(postId, null);
//				postService.commentCount(postId, post.getCommentCount() - 1);
//				commentService.commentCount(com.getId(), com.getCommentCount() - 1);
//			}
//			else Messagebox.show("Xoá thất bại", new Messagebox.Button[0], null);
//			Executions.sendRedirect("/post.zul?postid=" + postId + "#comment");
//		} else Messagebox.show("Bạn không phải chủ nhân của bình luận này", new Messagebox.Button[0], null);
//	}
//	
//	@Listen("onClick=#btnUpdatePost")
//	public void editPost() {		
//		Student student = null;
//		UserCredential cre = authService.getUserCredential();
//		if (!cre.getAccount().equals("anonymous")) {
//			student = studentService.findStudent(cre.getAccount());
//		}
//
//		Post post = postService.selectPost(postId, null);
//		
//		if (student != null && student.getStudent_code().equals(post.getOwnerStudentCode())) {
//			
//			
//			boolean result = postService.updatePost(post);
//			if (result) {
//				Messagebox.show("Bạn đã sửa bài đăng thành công", new Messagebox.Button[0], null);
//				Executions.sendRedirect("/post.zul?postid=" + postId);
//			}
//			else Messagebox.show("Sửa thất bại", new Messagebox.Button[0], null);
//		} else Messagebox.show("Bạn không phải chủ nhân của bài đăng này", new Messagebox.Button[0], null);
//
//	}
//	
//	@Listen("onClick=#more-btn-delete-post")
//	public void deletePost() {	
//		Student student = null;
//		UserCredential cre = authService.getUserCredential();
//		if (!cre.getAccount().equals("anonymous")) {
//			student = studentService.findStudent(cre.getAccount());
//		}
//
//		Post post = postService.selectPost(postId, null);
//		
//		if (student != null && student.getStudent_code().equals(post.getOwnerStudentCode())) {
//			commentService.deleteComment(null, postId);
//			voteService.delete(postId, null, null);
//			posttagService.delete(postId);
//			boolean result = postService.deletePost(Integer.parseInt(postId));
//			if (result) {
//				Messagebox.show("Bạn đã xoá bài đăng thành công", new Messagebox.Button[0], null);
//				Executions.sendRedirect("/");
//			}
//			else Messagebox.show("Xoá thất bại", new Messagebox.Button[0], null);
//		} else Messagebox.show("Bạn không phải chủ nhân của bài đăng này", new Messagebox.Button[0], null);
//	}
}
