package vnua.fita.controller;

import java.time.LocalDate;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import vnua.fita.entity.Post;
import vnua.fita.entity.Student;
import vnua.fita.service.PostService;
import vnua.fita.service.StudentService;
import vnua.fita.service.impl.PostServiceImpl;
import vnua.fita.service.impl.StudentServiceImpl;

public class ProfileController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	@Wire
	private Label countPost;
	@Wire
	private Image avatar;
	@Wire
	private Label displayName;
	@Wire
	private Label countComment;
	@Wire
	private Label creationDate;
	@WireVariable
    private List<Post> posts;
	
	@WireVariable
	StudentService studentService = new StudentServiceImpl(
			"jdbc:mysql://localhost:3306/training", "root", "123456");
	@WireVariable
	private PostService postService = new PostServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		String studentCode = (String) Executions.getCurrent().getParameter("id");
		try {
			String pageTitle = studentService.findStudent(studentCode).getName();
			Executions.getCurrent().getDesktop().getFirstPage().setTitle(pageTitle + " - Fita's Forum");
		} catch (Exception e) {
			Executions.getCurrent().getDesktop().getFirstPage().setTitle("Không tìm thấy trang - Fita's Forum");
		}
		
		Student student = studentService.findStudent(studentCode);
		avatar.setSrc(student.getProfileImageUrl());
		displayName.setValue(student.getName());
		LocalDate localD = LocalDate.parse(student.getCreationDate().toString());
		String creation = "Đã tham gia "+ localD.getDayOfMonth() +" tháng "+ localD.getMonthValue() + ", " + localD.getYear();
		creationDate.setValue(creation);
		countPost.setValue(studentService.count(studentCode).getCountPost() +" bài đăng được công khai");
		countComment.setValue(studentService.count(studentCode).getCountComment() +" bình luận được viết");
	}
	
	//--- Truyền danh sách posts sang listbox
		 public ListModel<Post> getPosts() {
		 	String studentCode = (String) Executions.getCurrent().getParameter("id");
		 	posts = postService.search(studentCode,null,null);
	        return new ListModelList<Post>(posts);
		 }
}
