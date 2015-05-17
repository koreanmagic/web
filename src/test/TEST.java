import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;


public class TEST {

	
	@Test
	public void test() throws Exception {
		
		/*ResourceFileManager manager = ResourceFileManager.build("G:/app/Projects/web2/webapp");
		log( manager
					.getJSText("/admin/work/list/list") );*/
		
		Path path  = Paths.get("G:/app/Projects/web2/webapp/js/");
		Path p = Paths.get("/admin/");
		path = path.resolve( p.subpath(0, p.getNameCount() -1) );
		log(path);
	}
	
	
	private static void log(Object obj) {
		System.out.println(obj);
	}

}
