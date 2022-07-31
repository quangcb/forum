package vnua.fita.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import vnua.fita.service.AuthenticationService;
import vnua.fita.service.impl.AuthenticationServiceImpl;

@VariableResolver(DelegatingVariableResolver.class)
public class LoginController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	//wire components
	@Wire
	private Textbox account;
	@Wire
	private Textbox password;
	@Wire
	Label message;
	
	private AuthenticationService authService = new AuthenticationServiceImpl();
	 
	@Listen("onClick=#login; onOK=#loginForm")
	public void doLogin(){
		String nm = account.getValue();
		String pd = password.getValue();
		System.out.println("input: "+ pd + "-"+nm);
		if(!authService.login(nm,pd)){
			message.setValue("Tài khoản hoặc mật khẩu không đúng.");
			System.out.println("not login");
			return;
		}
		if(nm.contains("admin"))
			Executions.sendRedirect("/admin/post.zul");
		else 
			Executions.sendRedirect("/");
	}
	
	@Listen("onClick=#modalLogin; onOK=#modalLoginForm")
	public void doModalLogin(){
		String nm = account.getValue();
		String pd = password.getValue();
		System.out.println("input: "+ pd + "-"+nm);
		if(!authService.login(nm,pd)){
			message.setValue("Tài khoản hoặc mật khẩu không đúng.");
			System.out.println("not login");
			return;
		}
		Executions.getCurrent().sendRedirect("");
	}
	
	@Listen("onClick = #orderBtn")
    public void showModal(Event e) {
        Window window = (Window)Executions.createComponents(
                "/login/registration.zul", null, null);
        window.doModal();
    }
	
	@Listen("onClick = #pageLogin")
    public void doPageLogin(Event e) {
		authService.logout();
		Executions.sendRedirect("/login/");
	}
	
	@Listen("onClick = #pageRegistration")
    public void doPageRegistration(Event e) {
		authService.logout();
		Executions.sendRedirect("/login/registration.zul");
	}
}
