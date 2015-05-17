package kr.co.koreanmagic.hibernate3.legacy.support;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/*
 * 
 * 클래스패스 폴더에 파일을 만들고, 파일명에 'mastername'이 들어가 있으면 대상파일이 됨.
 * 인코딩은 UTF-8
 * 
 * ---------------------- 파일양식 --------------------------
 * ##
 * full className
 * 마스터네임=유형1,유형2,유형3 ...
 * --------------------------------------------------------
 * 
 * 위와같은 양식으로 파일을 작성해놓고
 * 각 유형에 따라 마스터네임을 반환해준다.
 * 
 * 클라이언트에 제공되는 인터페이스는 NameConvertor이고, 핵심 메서드는 convert(String name) 이다
 * 해당 유형이 등록되어 있지 않을때는 체크예외인 NoSuchMasterNameException이 던져진다.
 * 
 */
public class NameConvertManager {
	
	private static Map<Class<?>, NameConvertor> cache;
	/*
	 * 파일명에 mastername이 포함된 모든 이름리스트를 끌어올려 NameConvertor 객체로 만들고 이를 Cache에 저장한다.
	 * Cache의 키값은 해당 객체의 Class<T>가 된다.
	 * 
	 */
	static {
		try {
			
			cache = new HashMap<>();
			
			/*
			 * 클래스패스에 있는 mastername 파일을 모두 로딩한다.
			 */
			Path root = Paths.get(NameConvertManager.class.getResource(".").toURI());
			try(DirectoryStream<Path> stream = Files.newDirectoryStream(root);) {
				for(Path masterFile : stream) {
					if(Files.isRegularFile(masterFile) && masterFile.getFileName().toString().contains("mastername")) {
						staticLoad(masterFile);
					}
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException("마스터네임 컨버터를 로딩하는 중 에러가 생겼습니다.", e);
		}
	}
	
	
	/*
	 * 로딩용 메서드
	 */
	private static void staticLoad(Path filePath) throws Exception {
		BufferedReader br = Files.newBufferedReader(filePath, Charset.forName("utf-8"));

		List<String> list = new ArrayList<>();
		
		String line = null;
		Class<?> key = null;
		
		int i = 0;
		br.readLine(); // 첫줄은 날린다.
		while((line = br.readLine()) != null) {
			// 두번째 줄에는 cache의 키값으로 쓰이는 className이 있다.
			if(i++ == 0) key = Class.forName(line);
			list.add(line);
		}
		cache.put(key, getConvertor(list));
	}
	
	
	
	public static NameConvertor get(String key) {
		return cache.get(key);
	}
	

	/*
	 * NameConvertor에 이름을 추가한다.
	 */
	public static NameConvertor addNames(NameConvertor convertor, String masterName, String...names) {
		ConvertorImpl impl = (ConvertorImpl)convertor;
		impl.add(masterName, names);
		return convertor;
	}
	
	/*
	 * NameConvertor를 파일로 저장한다.
	 */
	public static int outputFile(NameConvertor convertor, Path file) {
		
		int result = 0;
		
		try(BufferedWriter bw = Files.newBufferedWriter(file, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);) {
			
			ConvertorImpl impl = (ConvertorImpl)convertor;
			for(Convert convert : impl.list) {
				bw.append(convert.toString()).append("\r\n");
				result++;
			}
			bw.flush();
			
		} catch (Exception e) { 
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	
	/*
	 * 파일을 읽어들여 Convertor 객체를 만들어준다. 
	 */
	public static NameConvertor getConvertorByPath(Path path, String charsetName) {
		try {
			return getConvertor(Files.readAllLines(path, Charset.forName(charsetName)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static NameConvertor getConvertor(List<String> list) {
		
		Set<Convert> result = new TreeSet<>(); 

		/*
		 * ##파일 양식##
		 * 마스터네임=유형1,유형2
		 */
		String line = null;
		Convert c = null;

		for (int i = 1, l = list.size(); i < l; i++) {
			line = list.get(i);
			String[] values = line.split("=");
			c = new NameConvertManager.Convert(values[0],
										values.length == 2 ? values[1].split(",") : null
										);
			if(!result.add(c))
				throw new RuntimeException("중복된 마스터네임 ( " + values[0] + " ) 이 있습니다.");
		}

		return new ConvertorImpl(result);
	}
	
	
	
	/*
	 * 클라이언트에게 반환되는 NameConvertor 구현체
	 * convert()를 통해 컨버터를 이용할 수 있다.
	 */
	public static class ConvertorImpl implements NameConvertor {
		private TreeSet<Convert> list = new TreeSet<>();
		private ConvertorImpl() {}
		private ConvertorImpl(Collection<Convert> list) {
			this.list.addAll(list);
		}
		
		private void add(String masterName, String...names) {
			Convert convert = null;
			for(Convert convertor : this.list) {
				if(convertor.masterName.equals(masterName)) {
					convert = convertor;
					convert.add(names);
					break;
				}
			}
			if(convert == null) {
				convert = new NameConvertManager.Convert(masterName, names);
				this.list.add(convert);
			}
		}
		
		@Override
		public int size() {
			return this.list.size();
		}
		
		@Override
		public String convert(String name) {
			for(Convert c : list) {
				if(c.check(name)) {
					return c.get();
				}
			}
			NoSuchMasterNameException exception = new NoSuchMasterNameException("이름리스트에 ( " + name + " )의 처리유형이 등록되어 있지 않습니다.,");
			exception.setName(name);
			throw exception;
		}
		
		@Override
		public String toString() {
			return String.valueOf(size());
		}
	}
	
	
	/*
	 * 마스터네임과 이름목록이 들어있는 객체
	 */
	private static class Convert implements Comparable<Convert> {
		private String masterName;
		private Set<String> nameList = new TreeSet<>();
		
		private Convert(String masterName, String[] nameList) {
			this.masterName = masterName;
			
			// 마스터네임 하나만 적어도 Convert가 될 수 있도록 null검사를 한다. 
			if(nameList != null) {
				for(String name : nameList) {
					this.nameList.add(name);
				}
			}
			this.nameList.add(masterName);
		}
		
		private int add(String...names) {
			int insert = 0;
			for(String name : names) {
				if(this.nameList.add(name))
					insert++;
			}
			return insert; 
		}
		
		private boolean check(String name) {
			for(String n : this.nameList) {
				if(n.equals(name))
					return true;
			}
			return false;
		}
		private String get() {
			return this.masterName;
		}
		
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((masterName == null) ? 0 : masterName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Convert other = (Convert) obj;
			if (masterName == null) {
				if (other.masterName != null)
					return false;
			} else if (!masterName.equals(other.masterName))
				return false;
			return true;
		}

		@Override
		public String toString() {
			
			return this.masterName + "=" + String.join(",", nameList.toArray(new String[nameList.size()]));
		}

		@Override
		public int compareTo(Convert other) {
			return this.masterName.compareTo(other.masterName);
		}
	}

}
