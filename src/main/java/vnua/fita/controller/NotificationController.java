package vnua.fita.controller;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;

import vnua.fita.entity.Notification;
import vnua.fita.entity.Student;
import vnua.fita.entity.UserCredential;
import vnua.fita.service.AuthenticationService;
import vnua.fita.service.NotificationService;
import vnua.fita.service.StudentService;
import vnua.fita.service.impl.AuthenticationServiceImpl;
import vnua.fita.service.impl.NotificationServiceImpl;
import vnua.fita.service.impl.StudentServiceImpl;

public class NotificationController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	@Wire
	private Button notify;
	@Wire(".input")
	private Textbox input;
	@Wire
	private List<Notification> notifications;
	
	private NotificationService notificationService = new NotificationServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");
	@WireVariable
	AuthenticationService authService = new AuthenticationServiceImpl();
	@WireVariable
	StudentService studentService = new StudentServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UserCredential cre = authService.getUserCredential();
		if(!cre.getAccount().equals("anonymous")) {
			Student student = studentService.findStudent(cre.getAccount());
			notificationService.updateNotification(student.getStudent_code());
		}
		
	}
	
	//--- Truyền danh sách posts sang listbox
	 public ListModel<Notification> getNotifications() {
		 UserCredential cre = authService.getUserCredential();
			if(!cre.getAccount().equals("anonymous")) {
				Student student = studentService.findStudent(cre.getAccount());
				notifications = notificationService.notify(student.getStudent_code());
			}
	     return new ListModelList<Notification>(notifications);
	 }
}
