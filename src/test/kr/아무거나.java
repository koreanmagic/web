package kr;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import org.junit.Test;


public class 아무거나 {

	@Test
	public void test() throws Exception {
		
		String name = "/admin/work/list";
		
		
	}
	
	//@Test
	public void less() throws Exception {
		Path path = Paths.get("G:/app/Projects/web2/webapp/webresource");
		walk(path);
	}
	
	
	private void walk(Path dir) throws Exception {
		Path less = null;
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir);) {
			
			for(Path p : stream) {
				if(Files.isDirectory(p)) walk(p);
				if(p.getFileName().toString().endsWith(".sass")) {
					less = p.getParent().resolve( p.getFileName().toString().replace("sass", "scss") );
					Files.move(p, less);
				}
			}
			
			
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
