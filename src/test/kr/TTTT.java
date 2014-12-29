package kr;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class TTTT {

	@Test
	public void test() throws Exception {
		Path path = Paths.get("G:/request.txt");
		InputStream is = Files.newInputStream(path);

		int read = -1;
		while( (read = is.read()) != -1 )
			log((char)read);
		
	}
	
	
	private<T> T log(T t) {
		System.out.println(t);
		return t;
	}

}
