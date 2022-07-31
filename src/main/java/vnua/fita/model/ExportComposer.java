package vnua.fita.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;

import io.keikai.api.Exporter;
import io.keikai.api.Exporters;
import io.keikai.api.Ranges;
import io.keikai.api.model.Book;
import io.keikai.api.model.Sheet;
import io.keikai.ui.Spreadsheet;
import util.BookUtil;
import vnua.fita.entity.Student;
import vnua.fita.service.AuthenticationService;
import vnua.fita.service.StudentService;
import vnua.fita.service.impl.AuthenticationServiceImpl;
import vnua.fita.service.impl.StudentServiceImpl;

/**
 * This class shows exporter API
 * 
 * @author dennis
 *
 */
public class ExportComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@Wire
	private Spreadsheet ss;

	public static int MSV = 0;
	public static int PARENT_PHONE = 1;
	public static int LINK = 2;

	@WireVariable
	AuthenticationService authService = new AuthenticationServiceImpl();
	@WireVariable
	StudentService studentService = new StudentServiceImpl(
			"jdbc:mysql://localhost:3306/training", "root", "123456");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			Sheet sheet = ss.getSelectedSheet();
	
			List<Student> listStudent = studentService.findStudentParent();

			for (int row = 0; row < listStudent.size(); row++) {
				Ranges.range(sheet, row+1, MSV).setCellValue(listStudent.get(row).getStudent_code());
				Ranges.range(sheet, row+1, PARENT_PHONE).setCellValue(listStudent.get(row).getParent_phone());
				Ranges.range(sheet, row+1, LINK)
						.setCellValue("http://localhost:8080/fita/home/tkb_vnua.zul?msv_mgv=" + listStudent.get(row).getStudent_code());
			}
			
		} catch(Exception e){}
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
}