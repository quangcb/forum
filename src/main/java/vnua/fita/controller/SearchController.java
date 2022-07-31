package vnua.fita.controller;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;

import vnua.fita.entity.Post;
import vnua.fita.service.PostService;
import vnua.fita.service.impl.PostServiceImpl;

public class SearchController extends SelectorComposer<Component> {
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
		String pageTitle = (String) Executions.getCurrent().getParameter("q");
		try {
			if(pageTitle==null || pageTitle.equals(""))
				Executions.getCurrent().getDesktop().getFirstPage().setTitle("Fita's Forum Tìm kiếm");
			else
				Executions.getCurrent().getDesktop().getFirstPage().setTitle(pageTitle + " - Fita's Forum Tìm kiếm");
		} catch(Exception e) {}
	}
	
	//--- Truyền danh sách posts sang listbox
	 public ListModel<Post> getPosts() {
		 String keyword = (String) Executions.getCurrent().getParameter("q");
		 posts = postService.search(null, null, keyword);
	     return new ListModelList<Post>(posts);
	 }
	
	@Listen("onClick = #btnSearch; onOK = .search-container")
    public void search() {
		String keyword = input.getValue();
		Executions.sendRedirect("/search.zul?q="+keyword);
    }
}
