package vnua.fita.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import vnua.fita.entity.Student;
import vnua.fita.entity.UserCredential;
import vnua.fita.service.AuthenticationService;
import vnua.fita.service.StudentService;
import vnua.fita.service.impl.AuthenticationServiceImpl;
import vnua.fita.service.impl.StudentServiceImpl;

public class UpdateProfileController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 286213887912714397L;
	@Wire
	private Textbox passwordBox;
	@Wire
	private Textbox passwordNewBox;
	@Wire
	private Textbox passwordNew2Box;
	@Wire
	private Textbox nameBox;
	@Wire
	private Textbox classBox;
	@Wire
	private Textbox addressBox;
	@Wire
	private Textbox phoneBox;
	@Wire
	private Textbox emailBox;
	@Wire
	private Textbox parentNameBox;
	@Wire
	private Textbox parentPhoneBox;
	@Wire
	private Textbox aboutBox;
	@Wire
	private Image previewImage;

	@WireVariable
	AuthenticationService authService = new AuthenticationServiceImpl();
	@WireVariable
	StudentService studentService = new StudentServiceImpl(
			"jdbc:mysql://localhost:3306/training", "root", "123456");
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Executions.getCurrent().getDesktop().getFirstPage().setTitle("Fita's Forum Cài đặt");
	}
	
	@Listen("onClick=#saveProfile")
	public void doSaveProfile() {
		try {
			UserCredential cre = authService.getUserCredential();
			Student student = studentService.findStudent(cre.getAccount());
			student.setName(nameBox.getValue());
			student.setStudent_class(classBox.getValue());
			student.setAddress(addressBox.getValue());
			student.setPhone(phoneBox.getValue());
			student.setEmail(emailBox.getValue());
			student.setParent_name(parentNameBox.getValue());
			student.setParent_phone(parentPhoneBox.getValue());
			student.setAbout(aboutBox.getValue());

			studentService.updateStudent(student);
			Clients.showNotification("Bạn đã cập nhật thành công!");
		} catch (Exception e) {

		}
	}

	@Listen("onClick=#changePassword")
	public void doChangePassword() {
		try {
			UserCredential cre = authService.getUserCredential();
			Student student = studentService.findStudent(cre.getAccount());
			if (student.getPassword().equals(passwordBox.getValue())
					&& passwordNewBox.getValue().equals(passwordNew2Box.getValue())) {
				student.setPassword(passwordNewBox.getValue());
				studentService.changePassword(student);
				Clients.showNotification("Thay đổi mật khẩu thành công!");
			} else
				Clients.showNotification("Nhập sai hết rồi");
		} catch (Exception e){

		}
	}
	
	@Listen("onClick = #updateImageBtn")
	public void updateImage() {
		// Thêm ảnh vào thư mục img
		saveImage();
		// Gọi phương thức của lớp tầng model để insert các trường dữ liệu vào database
		UserCredential cre = authService.getUserCredential();
		Student student = studentService.findStudent(cre.getAccount());
		student.setProfileImageUrl("/images/profileImage/" + previewImage.getContent().getName());
		System.out.println("preview: " + "/images/profileImage/" + previewImage.getContent().getName());
		studentService.updateImage(student);

		Messagebox.show("Thành công!");
		Executions.sendRedirect("/settings");
	}
	
	private void saveImage() {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		String imgDir = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/images/profileImage/");
		try {
			InputStream fin = previewImage.getContent().getStreamData();
			in = new BufferedInputStream(fin);

			File file = new File(imgDir + previewImage.getContent().getName());

			OutputStream fout = new FileOutputStream(file);
			out = new BufferedOutputStream(fout);
			byte buffer[] = new byte[1024];
			int ch = in.read(buffer);
			while (ch != -1) {
				out.write(buffer, 0, ch);
				ch = in.read(buffer);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null)
					out.close();

				if (in != null)
					in.close();

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
