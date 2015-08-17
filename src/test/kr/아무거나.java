package kr;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import kr.co.koreanmagic.commons.DateUtils;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

import org.junit.Test;


public class 아무거나 {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	DateFormat d = new SimpleDateFormat("yyyy-MM-dd");
	@Test
	public void test() throws Exception {
		
		tour( Files.newDirectoryStream( Paths.get("G:/test") ) );
		
		
	}
	
	private void list( Object msg ) throws Exception {
		Files.write(Paths.get("G:/test/list.log"), msg.toString().concat("\r\n").getBytes(), StandardOpenOption.APPEND) ;
	}
	
	
	private void tour(DirectoryStream<Path> stream) throws Exception {
		
		for(Path path : stream) {
			if(path.getFileName().toString().equals("imgs") || path.getFileName().toString().equals("files")) continue;
			if(Files.isDirectory(path)) {
				tour( Files.newDirectoryStream(path) );
			}
			if( Files.isRegularFile(path)) {
				if( DateUtils.toString( new Date(Files.getLastModifiedTime(path).toMillis()) ).equals("2015-06-07") ) {
					//Files.delete(path);
					System.out.println(path);
				}
			}
		}
		
	}
	
	private static int index = 0;
	
	//@Test
	public void dd() throws Exception {
		
		Thumbnails.of(Paths.get("G:/work/resource/2014-10/16/img/141016010_세로토닌_리플렛(스노우)_[1-1000]_600-280mm.jpg").toFile())
		.height(150)
		.outputFormat("jpg")
		.toFile(Paths.get("g:/imgs/" + index++).toFile());
		
	}
	
	
	private void tour0(DirectoryStream<Path> stream) throws Exception {
		Path debug = null;
		try {
		for(Path path : stream) {
			if(Files.isDirectory(path)) {
				tour0( Files.newDirectoryStream(path) );
			}
			if( Files.isRegularFile(path)) {
				debug = path;
				if( path.getFileName().toString().endsWith("jpg") ) {
					Thumbnails.of(path.toFile())
					.height(150)
					.outputFormat("jpg")
					.toFile(Paths.get("g:/imgs/" + index++).toFile());
				}
			}
		}
		} catch(Exception e) {
			System.out.println(debug);
		}
		
	}
	
	private void log(Object obj) {
		System.out.println(obj);
	}
	
	abstract class A<T> {
		abstract T get();
	}
	
	class B extends A<String> {
		String get() {
			return "천재";
		}
	}
}
