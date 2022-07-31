package vnua.fita.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import vnua.fita.service.AuthenticationService;
import vnua.fita.service.impl.AuthenticationServiceImpl;

@VariableResolver(DelegatingVariableResolver.class)
public class LogoutController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;

	private AuthenticationService authService = new AuthenticationServiceImpl();
	
	@Listen("onClick=.logout")
	public void doLogout(){
		authService.logout();
		Executions.sendRedirect("/login");
	}
}
