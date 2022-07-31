package vnua.fita.controller;

import java.util.List;

import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import vnua.fita.entity.Comment;
import vnua.fita.entity.Notification;
import vnua.fita.entity.Post;
import vnua.fita.entity.Student;
import vnua.fita.entity.UserCredential;
import vnua.fita.service.AuthenticationService;
import vnua.fita.service.CommentService;
import vnua.fita.service.NotificationService;
import vnua.fita.service.PostService;
import vnua.fita.service.PostTagService;
import vnua.fita.service.StudentService;
import vnua.fita.service.VoteService;
import vnua.fita.service.impl.AuthenticationServiceImpl;
import vnua.fita.service.impl.CommentServiceImpl;
import vnua.fita.service.impl.NotificationServiceImpl;
import vnua.fita.service.impl.PostServiceImpl;
import vnua.fita.service.impl.PostTagServiceImpl;
import vnua.fita.service.impl.StudentServiceImpl;
import vnua.fita.service.impl.VoteServiceImpl;

public class ApprovalController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	@WireVariable
	private List<Comment> comments;
	@WireVariable
	private List<Post> posts;
	@Wire
	private Image postAvatar;
	@Wire
	private A postProfile;
	@Wire
	private Label postTimestamp;
	@Wire
	private Label postTitle;
	@Wire
	private Html postBody;
	@Wire
	private CKeditor ed;
	@Wire
	private Textbox lydo;
	private String postId = null;
	@WireVariable
	AuthenticationService authService = new AuthenticationServiceImpl();
	@WireVariable
	StudentService studentService = new StudentServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");

	private NotificationService notificationService = new NotificationServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");
	private VoteService voteService = new VoteServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");
	private CommentService commentService = new CommentServiceImpl("jdbc:mysql://localhost:3306/training", "root",
			"123456");
	private PostService postService = new PostServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");
	private PostTagService postTagService = new PostTagServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			Executions.getCurrent().getDesktop().getFirstPage().setTitle("Admin phê duyệt - Fita's Forum");
		} catch(Exception e) {}
	}

	// --- Truyền danh sách comments sang listbox
	public ListModel<Post> getPosts() {
		posts = postService.search(null,null,null);
		return new ListModelList<Post>(posts);
	}
	
	// --- Truyền danh sách comments sang listbox
	public ListModel<Comment> getComments() {
		comments = commentService.findAll();
		return new ListModelList<Comment>(comments);
	}
	
	@Listen("onDelete=#commentListbox")
	public void deleteComment(ForwardEvent evt) {	
		Button deleteComment = (Button) evt.getOrigin().getTarget();
		String parentId = deleteComment.getId();
		String commentId = parentId.replaceAll("delete", "");
		System.out.println("comid: "+commentId);
		Student student = null;
		UserCredential cre = authService.getUserCredential();
		if (!cre.getAccount().equals("anonymous")) {
			student = studentService.findStudent(cre.getAccount());
		}

		Comment com = commentService.selectComment(commentId);
		if (student != null && student.getStudent_code().contains("admin")){
			boolean result = commentService.deleteComment(commentId, null);
			if (result) {
				Messagebox.show("Bạn đã xoá bình luận thành công", new Messagebox.Button[0], null);
				Post post = postService.selectPost(postId, null);
				postService.commentCount(postId, post.getCommentCount() - 1);
				commentService.commentCount(com.getId(), com.getCommentCount() - 1);
			}
			else Messagebox.show("Xoá thất bại", new Messagebox.Button[0], null);
		} else Messagebox.show("Bạn không phải chủ nhân của bình luận này", new Messagebox.Button[0], null);
	}
	
	@Listen("onDelete=#postListbox")
	public void deletePost(ForwardEvent evt) {	
		Button deleteComment = (Button) evt.getOrigin().getTarget();
		String parentId = deleteComment.getId();
		postId = parentId.replaceAll("delete", "");
		System.out.println("postid: "+postId);
		Student student = null;
		UserCredential cre = authService.getUserCredential();
		if (!cre.getAccount().equals("anonymous")) {
			student = studentService.findStudent(cre.getAccount());
		}

		if (student != null && student.getStudent_code().contains("admin")) {
//			Window window = (Window)Executions.createComponents("/components/editor/delete.zul", null, null);
//			window.doModal();
			Post post = postService.selectPost(postId, null);
			System.out.println("title: "+ post.getTitle());
			System.out.println("owner: "+ post.getOwnerStudentCode());
			Notification noti = new Notification();
			noti.setPostTitle(post.getTitle());
			noti.setCommentText("hello");
			noti.setText(null);
			noti.setStudent_Code(post.getOwnerStudentCode());
			notificationService.insert(noti);
			commentService.deleteComment(null, postId);
			voteService.delete(postId, null, null);
			postTagService.delete(postId, null);
			boolean result = postService.deletePost(Integer.parseInt(postId));
			if (result) {
				Messagebox.show("Bạn đã xoá bài đăng thành công", new Messagebox.Button[0], null);
				Executions.sendRedirect("/admin/post.zul");
			}
			else Messagebox.show("Xoá thất bại", new Messagebox.Button[0], null);
		} else Messagebox.show("Bạn không phải chủ nhân của bình luận này", new Messagebox.Button[0], null);
	}
	
//	@Listen("onClick=#btnDelete")
//	public void btnDeletePost() {
//		Student student = null;
//		UserCredential cre = authService.getUserCredential();
//		if (!cre.getAccount().equals("anonymous")) {
//			student = studentService.findStudent(cre.getAccount());
//		}
//
//		Notification noti = new Notification();
////		notificationService
//		if (student != null && student.getStudent_code().contains("admin")) {
//			
//			noti.setPostId(postId);
//			noti.setText(lydo.getValue());
//			notificationService.insert(noti);
//			System.out.println("tsstasdk pos: "+postId);
//			System.out.println(lydo.getValue());
//			commentService.deleteComment(null, postId);
//			voteService.delete(postId, null, null);
//			postTagService.delete(postId, null);
//			boolean result = postService.deletePost(Integer.parseInt(postId));
//			if (result)	{
//				Messagebox.show("Bạn đã xoá bài đăng thành công", new Messagebox.Button[0], null);
//				Executions.sendRedirect("/admin/post.zul");
//			}
//		}
//	}
}
