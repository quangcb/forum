package vnua.fita.controller;

import java.util.List;

import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import vnua.fita.entity.Post;
import vnua.fita.entity.Student;
import vnua.fita.entity.UserCredential;
import vnua.fita.service.AuthenticationService;
import vnua.fita.service.NotificationService;
import vnua.fita.service.PostService;
import vnua.fita.service.PostTagService;
import vnua.fita.service.StudentService;
import vnua.fita.service.impl.AuthenticationServiceImpl;
import vnua.fita.service.impl.NotificationServiceImpl;
import vnua.fita.service.impl.PostServiceImpl;
import vnua.fita.service.impl.PostTagServiceImpl;
import vnua.fita.service.impl.StudentServiceImpl;

public class HomeController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	@Wire
	private Button notify;
	@Wire
	private Listbox postListbox;
	@Wire
	private Image avatar;
	@Wire
	private A displayName;
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
	private Textbox tags;
	@Wire
	private A dashboard;
	@Wire
	private A users;
	@Wire
	private A dashboards;
	@Wire
	private Textbox title;
	@Wire(".image")
	private Div image;
	@Wire
	private CKeditor ed;
	@WireVariable
    private List<Post> posts;
	
	@WireVariable
	AuthenticationService authService = new AuthenticationServiceImpl();
	@WireVariable
	StudentService studentService = new StudentServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");
	private NotificationService notificationService = new NotificationServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");
	private PostService postService = new PostServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");
	private PostTagService postTagService = new PostTagServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			Executions.getCurrent().getDesktop().getFirstPage().setTitle("Fita's Forum");
		} catch(Exception e){}
		UserCredential cre = authService.getUserCredential();
		if(!cre.getAccount().equals("anonymous")) {
			Student student = studentService.findStudent(cre.getAccount());
			try {
				String count = notificationService.count(student.getStudent_code());
				System.out.println("count: "+count);
				if(!count.equals("0"))
					notify.setLabel(count);
			}catch(Exception e) {}
			try {
				avatar.setSrc(student.getProfileImageUrl());
				displayName.setHref("/profile.zul?id="+student.getStudent_code());
				dashboard.setHref("/tkb_vnua.zul?msv_mgv="+ student.getStudent_code());
				users.setHref("/profile.zul?id="+student.getStudent_code());
				dashboards.setHref("/tkb_vnua.zul?msv_mgv="+ student.getStudent_code());
			}catch(Exception e) {}
		}
	}
	
	//--- Truyền danh sách posts sang listbox
	 public ListModel<Post> getPosts() {
		 	posts = postService.search(null,null,null);
	        return new ListModelList<Post>(posts);
	 }
	
	 //--- Tạo bài đăng
	@Listen("onClick=#btnCreatePost")
	public void doCreatePost(){
		String edt = ed.getValue();
		String tt = title.getValue();
		System.out.println("input: "+ tt);
		System.out.println("input: "+ edt);
		String addTag1 = tag1.getValue().replaceAll("#", "");
		String addTag2 = tag2.getValue().replaceAll("#", "");
		String addTag3 = tag3.getValue().replaceAll("#", "");
		String addTag4 = tag4.getValue().replaceAll("#", "");
		try {
			Post post = new Post();
			post.setTitle(title.getValue());
			post.setBody(ed.getValue());
			
			UserCredential cre = authService.getUserCredential();
			Student student = studentService.findStudent(cre.getAccount());
			post.setOwnerStudentCode(student.getStudent_code());
			boolean result = postService.insertPost(post);
			Post ownerPost = postService.selectPost(null, student.getStudent_code());
			System.out.println("postId: "+ownerPost.getPostId());
			if(!postTagService.check(addTag1) && !addTag1.equals(""))
				postTagService.insertTag(addTag1);
			if(!postTagService.check(addTag2) && !addTag2.equals("")) 
				postTagService.insertTag(addTag2);
			if(!postTagService.check(addTag3) && !addTag3.equals("")) 
				postTagService.insertTag(addTag3);
			if(!postTagService.check(addTag4) && !addTag4.equals("")) 
				postTagService.insertTag(addTag4);
			
			if(postTagService.check(addTag1) && !addTag1.equals(""))
				postTagService.insertPostTag(ownerPost.getPostId(), Integer.toString(postTagService.getId(addTag1)), 1);
			if(postTagService.check(addTag1) && !addTag2.equals(""))
				postTagService.insertPostTag(ownerPost.getPostId(), Integer.toString(postTagService.getId(addTag2)), 2);
			if(postTagService.check(addTag1) && !addTag3.equals(""))
				postTagService.insertPostTag(ownerPost.getPostId(), Integer.toString(postTagService.getId(addTag3)), 3);
			if(postTagService.check(addTag1) && !addTag4.equals(""))
				postTagService.insertPostTag(ownerPost.getPostId(), Integer.toString(postTagService.getId(addTag4)), 4);
			if (result) {
				Messagebox.show("Bạn đã đăng bài thành công",
					 	new Messagebox.Button[0], null);
				
				Executions.sendRedirect("/");
			} else {
				Messagebox.show("Thất bại!");
			}
		} catch (Exception e) {}
	}
	
	//--- Sự kiện Click tạo bài đăng hiện ra modal
	@Listen("onClick = #createPost")
    public void showModal(Event e) {
        //create a window programmatically and use it as a modal dialog.
        Window window = (Window)Executions.createComponents(
                "/components/new.zul", null, null);
        window.doModal();
    }
	
	@Listen("onOK = #editor-tags")
    public void addTag(Event e) {
		if(tag1.getValue().equals("") && tag2.getValue().equals("") && tag3.getValue().equals("") && tag4.getValue().equals("")) {
			tag1.setValue("#" + tags.getValue().toLowerCase());
			boxtag1.removeSclass("tag-close");
			tags.setValue(null);
		} else if(tag2.getValue().equals("") && tag3.getValue().equals("") && tag4.getValue().equals("") && !tags.getValue().equals(tag1.getValue().replaceAll("#", ""))) {
			boolean sc = !tags.getValue().equals(tag1.getValue().replaceAll("#", ""));
			System.out.println("nnn: "+sc);
			tag2.setValue("#" + tags.getValue().toLowerCase());
			boxtag2.removeSclass("tag-close");
			tags.setValue(null);
		} else if(tag3.getValue().equals("") && tag4.getValue().equals("") && !tags.getValue().equals(tag1.getValue().replaceAll("#", ""))  && !tags.getValue().equals(tag2.getValue().replaceAll("#", ""))) {
			tag3.setValue("#" + tags.getValue().toLowerCase());
			boxtag3.removeSclass("tag-close");
			tags.setValue(null);
		} else if(tag4.getValue().equals("") && !tags.getValue().equals(tag1.getValue().replaceAll("#", "")) && !tags.getValue().equals(tag2.getValue().replaceAll("#", "")) && !tags.getValue().equals(tag3.getValue().replaceAll("#", ""))) {
			tag4.setValue("#" + tags.getValue().toLowerCase());
			boxtag4.removeSclass("tag-close");
			tags.setValue(null);
			tags.setPlaceholder("Thêm tối đa 4 thẻ.");
		}
    }
	
	@Listen("onClick = #delete1")
    public void closeTag1() {
		if(!tag2.getValue().equals("") && !tag3.getValue().equals("") && !tag4.getValue().equals("")) {
			tag1.setValue(tag2.getValue());
			tag2.setValue(tag3.getValue());
			tag3.setValue(tag4.getValue());
			tag4.setValue("");
			boxtag4.setClass("tag-close");
		} else if(!tag2.getValue().equals("") && !tag3.getValue().equals("")) {
			tag1.setValue(tag2.getValue());
			tag2.setValue(tag3.getValue());
			tag3.setValue("");
			boxtag3.setClass("tag-close");
		} else if(!tag2.getValue().equals("")){
			tag1.setValue(tag2.getValue());
			tag2.setValue("");
			boxtag2.setClass("tag-close");
		} else {
			tag1.setValue("");
			boxtag1.setClass("tag-close");
		}
	}
	@Listen("onClick = #delete2")
    public void closeTag2() {
		if(!tag3.getValue().equals("") && !tag4.getValue().equals("")) {
			tag2.setValue(tag3.getValue());
			tag3.setValue(tag4.getValue());
			tag4.setValue("");
			boxtag4.setClass("tag-close");
		} else {
			
			tag2.setValue("");
			boxtag2.setClass("tag-close");
		}
	}
	@Listen("onClick = #delete3")
    public void closeTag3() {
		if(!tag4.getValue().equals("")) {
			tag3.setValue(tag4.getValue());
			tag4.setValue("");
			boxtag4.setClass("tag-close");
		} else {
			tag3.setValue("");
			boxtag3.setClass("tag-close");
		}
	}
	@Listen("onClick = #delete4")
    public void closeTag4() {
		tag4.setValue("");
		boxtag4.setClass("tag-close");
	}
}
