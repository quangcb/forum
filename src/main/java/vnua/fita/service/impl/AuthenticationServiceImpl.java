package vnua.fita.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import vnua.fita.entity.Student;
import vnua.fita.entity.UserCredential;
import vnua.fita.service.AuthenticationService;
import vnua.fita.service.StudentService;

@Service("authService")
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	StudentService studentService = new StudentServiceImpl(
			"jdbc:mysql://localhost:3306/training", "root", "123456");

	public UserCredential getUserCredential() {
		Session sess = Sessions.getCurrent();
		UserCredential superAdminCre = (UserCredential) sess.getAttribute("superAdminCredential");
		if (superAdminCre == null) {
			UserCredential adminCre = (UserCredential) sess.getAttribute("adminCredential");
			if (adminCre == null) {
				UserCredential cre = (UserCredential) sess.getAttribute("userCredential");
				if (cre == null) {
					cre = new UserCredential();// new a anonymous user and set to session
					sess.removeAttribute("userCredential");
				}
				return cre;
			}
			return adminCre;
		}
		return superAdminCre;
	}

	public boolean login(String nm, String pd) {
		Student student = studentService.findStudent(nm);
		// a simple plan text password verification
		if (student == null || !student.getPassword().equals(pd)) {
			return false;
		}

		UserCredential cre = new UserCredential(student.getStudent_code(), student.getName());
		Session sess = Sessions.getCurrent();
		if (nm.equals("admin")) {
			sess.setAttribute("superAdminCredential", cre);
		} else if (nm.contains("admin")) {
			sess.setAttribute("adminCredential", cre);
		} else {
			sess.setAttribute("userCredential", cre);
		}
		return true;
	}

	public void logout() {
		Session sess = Sessions.getCurrent();
		sess.invalidate();
	}
}
