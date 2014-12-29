package kr;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;

public class SCSS파일찾기 {

	private static final Path ROOT = Paths.get("G:/app/Projects/web2/webapp/");
	
	@Test
	public void test() throws Exception {
		find("$base-tag-default-border-color");
		//replace("$base-tag-default-border-color", "");
	}
	
	
	private void replace(Path path, String oldStr, String newStr) throws Exception {
		try(BufferedReader reader = createReader(path);) {
			
			
		}
	}
	
	
	/* 단어 찾기 */
	private void find(String line) throws Exception {
		
		List<Log> result = find(ROOT, line);
		
		Log log = null;
		for(Log l : result) {
			log = find( createReader(l.getPath()), l );
			if(!log.isEmpty())
				System.out.println(log);
		}
	}
	
	private List<Log> find(Path dir, String line) throws Exception {
		final List<Log> logs = new ArrayList<>();
		
		selectScssFiles(dir, path -> {
			logs.add(new Log(path, line));
		});
		
		return logs;
	}
	
	/*
	 * 모든 파일 순회하면서 scss 파일만 골라낸다. 
	 */
	private void selectScssFiles(Path dir, Consumer<Path> consumer) throws Exception {
		Path logPath = null;
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir);) {
			
			for(Path path : stream) {
				logPath = path;
				if(path.getFileName().toString().equals(".sass-cache"))
					continue;
				else if(Files.isDirectory(path))
					selectScssFiles(path, consumer);
				else if(path.getFileName().toString().endsWith("scss")) {
					consumer.accept(path);
				}
			}
		} catch (Exception e) {
			System.out.println(">>>" + logPath);
			throw e;
		}
	}
	
		
	// 인코딩에 맞게 Reader를 만들어서 내보낸다.
	private BufferedReader createReader(Path path) throws Exception {
		try {
			Files.newBufferedReader(path, Charset.forName("euc-kr"))
					.readLine(); // 한번 읽어보고 문제가 없으면 다시 만들어서 보낸다.
			return Files.newBufferedReader(path, Charset.forName("euc-kr"));
		} catch (Exception e) {
			return Files.newBufferedReader(path, Charset.forName("utf-8"));
		}
	}
	
	private Log find(BufferedReader reader, Log log) throws Exception {
		String v = null;
		int lineNum = 1;
		while((v = reader.readLine()) != null) {
			if(v.contains(log.getSearchStr())) {
				log.addNum(lineNum);
			}
			lineNum++;
		}
		return log;
	}
	
	

	static class Log {
		
		private Path path;
		private String searchStr;
		private List<Integer> list;
		private boolean isEmpty = true;
		
		
		Log(Path path, String searchStr) {
			this.path = path;
			this.searchStr = searchStr;
		}
		
		public Path getPath() {
			return path;
		}
		public void setPath(Path path) {
			this.path = path;
		}
		
		public String getSearchStr() {
			return searchStr;
		}
		public void setSearchStr(String searchStr) {
			this.searchStr = searchStr;
		}
		
		public List<Integer> getList() {
			if(list == null)
				list = new ArrayList<>();
			return list;
		}
		public void setList(List<Integer> list) {
			this.list = list;
		}
		public void addNum(int line) {
			getList().add(line);
			setEmpty(false);
		}
		
		public boolean isEmpty() {
			return isEmpty;
		}
		public void setEmpty(boolean isEmpty) {
			this.isEmpty = isEmpty;
		}
		
		@Override
		public String toString() {
			return getPath().toString() + "\n" + getSearchStr() + " ==> " + getList() + "\n-------------------\n";
		}
	}
	
}
