package vnua.fita.controller;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import util.RandomUtil;
import vnua.fita.entity.Student;
import vnua.fita.service.impl.RegistrationDao;

// Lớp RegistrationComposer muốn ZK hỗ trợ quản lý trang ZUL
// cần khai báo thừa kế từ SelectorComposer<Component>
// Div là thành phần khai báo apply ở trên trang ZUL
@SuppressWarnings("serial")
public class RegistrationController extends SelectorComposer<Div> {
// KHAI BÁO CÁC THÀNH PHẦN TRÊN TRANG CẦN TƯƠNG TÁC
	@Wire("#submitButton")
	private Button submitButton;
	@Wire("#usernameBox")
	private Textbox usernameBox;
	@Wire("#passwordBox")
	private Textbox passwordBox;
	@Wire("#nameBox")
	private Textbox nameBox;
	@Wire("#phoneBox")
	private Textbox phoneBox;
	@Wire("#emailBox")
	private Textbox emailBox;
	@Wire("#genderRadio")
	private Radiogroup genderRadio;
	@Wire("#birthdayBox")
	private Datebox birthdayBox;
	@Wire(".acceptTermBox")
	private Checkbox acceptTermCheckbox;
	@Wire("#nameRow")
	private Row nameRow;
	@Wire("#helpPopup")
	private Popup helpPopup;
	private RegistrationDao registrationDao = new RegistrationDao(
			"jdbc:mysql://localhost:3306/training", "root", "123456");

// LẮNG NGHE SỰ KIỆN TRÊN CHECKBOX
// Tra cứu tên sự kiện trên tài liệu reference của ZK về CheckBox
	@Listen("onCheck = .acceptTermBox")
	public void changeSubmitStatus() {
// Nếu checkbox được check
		if (acceptTermCheckbox.isChecked()) {
// Enable nút Submit
			submitButton.setDisabled(false);
// Đặt icon sclass cho nút Submit
// Đây là thư viện icon được tích hợp sẵn
			submitButton.setIconSclass("z-icon-check");
		} else {
			submitButton.setDisabled(true);
			submitButton.setIconSclass("");
		}
	}

	public void reset() {
//set raw value để tránh validate cấu hình trên registration.zul
		usernameBox.setRawValue(null);
		passwordBox.setRawValue(null);
		nameBox.setRawValue(null);
		phoneBox.setRawValue(null);
		emailBox.setRawValue(null);
		genderRadio.setSelectedIndex(0);
		birthdayBox.setRawValue(null);
	}

	@Listen("onClick = #submitButton")
	public void submit() {
// Insert database
		try {
			Student student = new Student();
			student.setStudent_code(usernameBox.getValue());
			student.setPassword(passwordBox.getValue());
			student.setName(nameBox.getValue());
			student.setStudent_class(null);
			student.setAddress(null);
			student.setPhone(phoneBox.getValue());
			student.setEmail(emailBox.getValue());
			boolean gender = genderRadio.getSelectedIndex() == 0 ? true : false;
			student.setGender(gender);
			String[] imgMale = {"/images/profileImage/male1.svg", "/images/profileImage/male2.svg", "/images/profileImage/male3.svg", "/images/profileImage/male4.svg", "/images/profileImage/male5.svg", "/images/profileImage/male6.svg"};
			String[] imgFemale = {"/images/profileImage/female1.svg", "/images/profileImage/female2.svg", "/images/profileImage/female3.svg", "/images/profileImage/female4.svg", "/images/profileImage/female5.svg", "/images/profileImage/female6.svg"};
			if(gender==true)
				student.setProfileImageUrl(RandomUtil.getRandom(imgMale));
			else
				student.setProfileImageUrl(RandomUtil.getRandom(imgFemale));
			student.setBirthday(birthdayBox.getValue());
			boolean result = registrationDao.add(student);
			if (result) {
				Messagebox.show("Xin chúc mừng! Bạn đã đăng ký thành công tài khoản \"" + usernameBox.getValue() + "\"");
				reset();
				Executions.sendRedirect("/login");
			} else {
				Messagebox.show("Tài khoản đã tồn tại!");
			}
		} catch (Exception e) {
			
		}
	}

	// Bắt sự kiện phím Enter trên Grid, thực hiện submit
	@Listen("onOK = #formGrid")
	public void onOK(){
		submit();
	}
}