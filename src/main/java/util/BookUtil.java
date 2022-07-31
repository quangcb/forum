package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.zkoss.lang.SystemException;
import org.zkoss.zk.ui.WebApps;

import io.keikai.api.Exporter;
import io.keikai.api.Exporters;
import io.keikai.api.Importer;
import io.keikai.api.Importers;
import io.keikai.api.Ranges;
import io.keikai.api.model.Book;
import io.keikai.api.model.Book.BookType;
import io.keikai.api.model.Sheet;



public class BookUtil {

	static public Book newBook(String bookName, BookType type) {
		try {
			return loadBlankBook(bookName, type);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	static private Book loadBlankBook(String bookName, BookType type)
			throws IOException {
		
		Importer importer = Importers.getImporter();
		if(importer==null){
			throw new RuntimeException("Không tìm thấy tệp Excel");
		}
		InputStream is = null;
		switch (type) {
		case XLS:
			is = WebApps.getCurrent().getResourceAsStream("/WEB-INF/books/blank.xls");
			break;
		case XLSX:
			is = WebApps.getCurrent().getResourceAsStream("/WEB-INF/books/blank.xlsx");
			break;
		default :
			throw new IllegalArgumentException("Unknow book type" + type);
		}
		return importer.imports(is,bookName);
	}

	static File workingFolder;

	static public File getWorkingFolder() {
		if (workingFolder == null) {
			synchronized (BookUtil.class) {
				if (workingFolder == null) {
					workingFolder = new File(
							System.getProperty("java.io.tmpdir"), "zsswrk");
					if (!workingFolder.exists()) {
						if (!workingFolder.mkdirs()) {
							throw new SystemException(
									"Can't get working folder:"
											+ workingFolder.getAbsolutePath());
						}
					}
				}
			}
		}
		return workingFolder;
	}

	
	static public String suggestName(Book book) {
		String bn = book.getBookName();
		BookType type = book.getType();
		
		String ext = type==BookType.XLS?".xls":".xlsx";
		int i = bn.lastIndexOf('.');
		if(i==0){
			bn = "book";
		}else if(i>0){
			bn = bn.substring(0,i);
		}
		return bn+ext;
	}

	static public File saveBookToTemp(Book book) throws IOException{
		Exporter exporter = Exporters.getExporter("excel");
		String bn = book.getBookName();
		String ext = book.getType()==BookType.XLS?".xls":".xlsx";
		int i = bn.lastIndexOf('.');
		if(i==0){
			bn = "book";
		}else if(i>0){
			bn = bn.substring(0,i);
		}
		
		File f = File.createTempFile(Long.toString(System.currentTimeMillis()),ext,getWorkingFolder());
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			exporter.export(book, fos);
		}finally{
			if(fos!=null){
				fos.close();
			}
		}
		return f;
	}
	
	static public Book copySheetToNewBook(String bookName, Sheet sheet){
		Book newBook = newBook(bookName, BookType.XLSX);
		Ranges.range(newBook).cloneSheetFrom(sheet.getSheetName(), sheet);
		Ranges.range(newBook.getSheetAt(0)).deleteSheet();
		return newBook;
	}
}