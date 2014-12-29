package kr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.log4j.Logger;
import org.junit.Test;

public class Twist {

	Path path = Paths.get("G:/app/Projects/web2/webapp/WEB-INF/view/ftl");
	Logger logger = Logger.getLogger(getClass());
	
	@Test
	public void test() throws Exception {
		
		InetAddress ia = InetAddress.getByName("www.naver.com");
		Socket socket = new Socket(ia, 80);
		
		String request = new String(Files.readAllBytes(Paths.get("G:/request.txt")));
		log(request);
		
		OutputStream os = socket.getOutputStream();
		os.write(
				Files.readAllBytes(Paths.get("G:/request.txt")
						));

		os.flush();
		
		int read = -1, len = 0;
		InputStream is = socket.getInputStream();
		
		log(getHeader(is));
		
		Path path = Paths.get("G:/test.txt");
		try(OutputStream oss = Files.newOutputStream(path)) {
			
			while((read = is.read()) != -1) {
				if(read == '\n') oss.write('\r');
				oss.write(read);
			}
		}
	}
	
	
	private String getHeader(InputStream is) throws Exception {
		StringBuilder sb = new StringBuilder();
		int c = '\r', read = -1;
		boolean flag = false;
		while((read = is.read()) != -1) {
			
			if(read == c) {
				is.read();		// 다음에 있는 \n을 그냥 날린다.
				sb.append("\r\n");
				
				if(flag)		// 앞에도 공백이었으면 빈 줄이라는 뜻이다.
					break;
					
				flag = true;	// 플래그를 true
			} else {
				flag = false;
				sb.append((char)read);
			}
		}
		
		return sb.toString();
		
	}
	
	
	
	/*
	 * InputStreamReader로 헤더 추출
	 * --> 내부 버퍼로 인해 컨텐츠 부분까지 이미 버퍼에 담겨버린다.
	 */
	private String header(InputStream is, Object psudo) throws Exception {
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr = new InputStreamReader(is);
		char str = 0;
		int read = -1;
		boolean flag = false;
		while((read = isr.read()) != -1) { 
			str = (char)read;
			if(str == '\r') {
				isr.read();		// 다음에 있는 \n을 그냥 날린다.
				sb.append("\r\n");
				
				if(flag)		// 앞에도 공백이었으면 빈 줄이라는 뜻이다.
					break;
					
				flag = true;	// 플래그를 true
			} else {
				flag = false;
				sb.append(str);
			}
		}
		
		return sb.toString();
		
	}
	
	/*
	 * BufferedReader로 헤더 추출
	 * --> 내부 버퍼로 인해 컨텐츠 부분까지 이미 버퍼에 담겨버린다.
	 */
	private String header(InputStream is) throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while((line = br.readLine()).length() != 0)
			sb.append(line).append("\r\n");
		
		return sb.toString();
	}
	
	private String getContent(InputStream is) throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while((line = br.readLine()) != null)
			sb.append(line).append("\r\n");
		
		return sb.toString();
	}
	
	// 헤더부터 바디까지 그대로 출력
	private void print(InputStream is) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while((line = br.readLine()) != null)
			log(line);
	}
	
	private static void log(Object obj) {
		System.out.println(obj);
	}
	

	private<T extends A> T get(T t) {
		return t;
	}
	

	class A<T> {
		T t;
		public A(T t) {
			this.t = t;
		}
		public T get() {
			return t;
		}
	}
	
	class B extends A<String> {
		String s;
		public B(String s) {
			super(s);
		}
		public String get() {
			return s;
		}
				
	}
	
}
