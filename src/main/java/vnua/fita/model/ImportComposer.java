package vnua.fita.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import io.keikai.api.Exporter;
import io.keikai.api.Exporters;
import io.keikai.api.Importer;
import io.keikai.api.Importers;
import io.keikai.api.Ranges;
import io.keikai.api.model.Book;
import io.keikai.api.model.Sheet;
import io.keikai.ui.Spreadsheet;
import util.BookUtil;
import util.RandomUtil;
import vnua.fita.entity.Student;
import vnua.fita.service.AuthenticationService;
import vnua.fita.service.StudentService;
import vnua.fita.service.impl.AuthenticationServiceImpl;
import vnua.fita.service.impl.RegistrationDao;
import vnua.fita.service.impl.StudentServiceImpl;

/**
 * This class shows exporter API
 * 
 * @author dennis
 *
 */
public class ImportComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@Wire
	private Spreadsheet ss;

	public static int MSV = 0;
	public static int PASSWORD = 1;
	public static int NAME = 2;
	public static int CLASS = 3;
	public static int ADDRESS = 4;
	public static int PHONE = 5;
	public static int EMAIL = 6;
	public static int GENDER = 7;
	public static int BIRTHDAY = 8;
	
	private RegistrationDao registrationDao = new RegistrationDao(
			"jdbc:mysql://localhost:3306/training", "root", "123456");
	@WireVariable
	AuthenticationService authService = new AuthenticationServiceImpl();
	@WireVariable
	StudentService studentService = new StudentServiceImpl("jdbc:mysql://localhost:3306/training", "root", "123456");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		try {
			Importer importer = Importers.getImporter();
			Book book = importer.imports(getFile(), "sample");
			ss.setBook(book);
		} catch (Exception e) {
		}
	}

	@Listen("onClick = #exportExcel; onExport=#ss")
	public void doExport() throws IOException {
		Exporter exporter = Exporters.getExporter();
		Book book = ss.getBook();

		File file = File.createTempFile(Long.toString(System.currentTimeMillis()), "temp");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			exporter.export(book, fos);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		// generate file name upon book type (2007,2003)
		String fileName = BookUtil.suggestName(book);
		Filedownload.save(new AMedia(fileName, "xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", file, true));
	}

	@Listen("onClick = #importExcel; onExport=#ss_import")
	public void doImport() throws IOException {

	}

	@Listen("onClick = #storeData")
	public void doStoreData() throws IOException {
		Sheet sheet = ss.getSelectedSheet();
		Student student = new Student();
		try {
			for (int row = 0; Ranges.range(sheet, 1, MSV).getCellValue().toString() != null; row++) {
				student.setStudent_code(Ranges.range(sheet, row + 1, MSV).getCellValue().toString().replace(".0", ""));
				student.setPassword(Ranges.range(sheet, row + 1, PASSWORD).getCellValue().toString().replace(".0", ""));
				student.setName(Ranges.range(sheet, row + 1, NAME).getCellValue().toString());
				student.setStudent_class(Ranges.range(sheet, row + 1, CLASS).getCellValue().toString());
				student.setAddress(Ranges.range(sheet, row + 1, ADDRESS).getCellValue().toString());
				student.setPhone(Ranges.range(sheet, row + 1, PHONE).getCellValue().toString());
				student.setEmail(Ranges.range(sheet, row + 1, EMAIL).getCellValue().toString());
				student.setGender(Boolean.parseBoolean(Ranges.range(sheet, row + 1, GENDER).getCellValue().toString().replace(".0", "")));

				System.out.println("row: " + row);
				System.out.println(Ranges.range(sheet, row + 1, MSV).getCellValue().toString().replace(".0", ""));
				System.out.println(Ranges.range(sheet, row + 1, NAME).getCellValue().toString().replace(".0", ""));
				System.out.println(Ranges.range(sheet, row + 1, PHONE).getCellValue().toString());
				System.out.println(Ranges.range(sheet, row + 1, EMAIL).getCellValue().toString());
				System.out.println(Boolean.parseBoolean(Ranges.range(sheet, row + 1, GENDER).getCellValue().toString().replace(".0", "")));
				System.out.println("datet: "+Ranges.range(sheet, row + 1, BIRTHDAY).getCellData().getDateValue().toString());

				student.setBirthday(Ranges.range(sheet, row + 1, BIRTHDAY).getCellData().getDateValue());
				String[] imgMale = {"/images/profileImage/male1.svg", "/images/profileImage/male2.svg", "/images/profileImage/male3.svg", "/images/profileImage/male4.svg", "/images/profileImage/male5.svg", "/images/profileImage/male6.svg"};
				String[] imgFemale = {"/images/profileImage/female1.svg", "/images/profileImage/female2.svg", "/images/profileImage/female3.png", "/images/profileImage/female4.svg", "/images/profileImage/female5.svg", "/images/profileImage/female6.svg"};
				if(student.isGender())
					student.setProfileImageUrl(RandomUtil.getRandom(imgMale));
				else
					student.setProfileImageUrl(RandomUtil.getRandom(imgFemale));
				boolean result = registrationDao.add(student);
				if (result) {
					System.out.println("tạo thành công");
					
				} else {
					System.out.println("đã tồn tại");
				}
			}
			
		} catch (Exception e) {
			Messagebox.show("Thành công!");
		}
	}

	private File getFile() {
		// get a file
		return new File(WebApps.getCurrent().getRealPath("/WEB-INF/books/demo_sample_import.xlsx"));
	}
}