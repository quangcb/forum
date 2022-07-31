package vnua.fita.controller;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;

import vnua.fita.entity.Post;
import vnua.fita.service.PostService;
import vnua.fita.service.impl.PostServiceImpl;

public class TagController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;

	@Wire(".input")
	private Textbox input;
	@Wire
	private List<Post> posts;
	
	private PostService postService = new PostServiceImpl(
			"jdbc:mysql://localhost:3306/training", "root", "123456");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		String pageTitle = (String) Executions.getCurrent().getParameter("t");
		try {
			Executions.getCurrent().getDesktop().getFirstPage().setTitle("Tag "+pageTitle + " - Fita's Forum");
		} catch(Exception e) {}
	}
	
	//--- Truyền danh sách posts sang listbox
	 public ListModel<Post> getPosts() {
		 String keyword = (String) Executions.getCurrent().getParameter("t");
		 posts = postService.search(null, keyword, null);
	     return new ListModelList<Post>(posts);
	 }
	
}
