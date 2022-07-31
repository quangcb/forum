package vnua.fita.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Iframe;

public class StudentController extends SelectorComposer<Component>{
	private static final long serialVersionUID = 1L;
	
	@Wire
	private Iframe originTimetable;
	@Wire
	private Iframe originTranscript;
	@Wire
	private Iframe originTuition;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		String keyword = (String) Executions.getCurrent().getParameter("msv_mgv");
		Executions.getCurrent().getDesktop().getFirstPage().setTitle("Fita's Forum Dashboard");
		originTimetable.setSrc("http://daotao.vnua.edu.vn/Default.aspx?page=thoikhoabieu&sta=1&id=" + keyword);
		originTranscript.setSrc("http://daotao.vnua.edu.vn/Default.aspx?page=xemdiemthi&id=" + keyword);
		originTuition.setSrc("http://daotao.vnua.edu.vn/Default.aspx?page=xemhocphi&id=" + keyword);
	}
}
