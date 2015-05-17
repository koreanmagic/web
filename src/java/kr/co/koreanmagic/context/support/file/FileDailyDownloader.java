package kr.co.koreanmagic.context.support.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

// 저장하는 날짜에 따라 폴더를 만들어 저장한다.
public class FileDailyDownloader implements FileDownloader {
	
	private Path root;
	private String sub;
	private Map<LocalDate, Path> cache = new HashMap<>();
	
	public FileDailyDownloader(Path root) {
		this.root = root;
	}
	public FileDailyDownloader(String root) {
		this.root = Paths.get(root);
	}
	
	
	// root / 2013-12 / 03 / {subDir} / file.type
	public FileDailyDownloader setSubdir(String subName) {
		this.sub = subName;
		return this;
	}
	private String getSubDir() {
		if(this.sub == null) this.sub = "";
		return this.sub;
	}
	
	
	@Override
	public Path saveFile(InputStream is, String fileName) throws IOException {

		fileName = uuid( fileName );
		Path savePath = getRoot().resolve(fileName);
		Files.copy(is, savePath );
		
		//  returnValue :: 2015-03/05/file.type
		return savePath.subpath( root.getNameCount(), savePath.getNameCount());
	}
	
	// 파일명-a400ca.type
	private String uuid(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(0, pos) + uuid() + fileName.substring(pos, fileName.length()); 
	}
	// 유일키 생성
	// 아주 만약에 중복된 값이 나올 수 있다. 중복된 값이 나오면 로또 긁으러 가자
	private String uuid() {
		String uuid = UUID.randomUUID().toString();
		return "-" + uuid.substring(0, uuid.indexOf("-"));
	}
	
	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "] " + root.resolve( getSubDir() );
	}
	
	
	// LocalDate에 의해 만들어진 폴더는 캐시에 저장된다.
	@Override
	public Path getRoot() {
		Path dir = null;
		LocalDate date = LocalDate.now();
		if( (dir = cache.get(date)) == null ) {
			try {
				dir = createDir( date.toString() );
			} catch (IOException e) {
				throw new RuntimeException("폴더 생성하다 에러");
			}
			cache.put(date, dir);
		}
		return dir;
	}
	
	
	/*
	 * LocalDate로 폴더 생성
	 * ex) 2015-03-21이면  root/2015-03/21 생성
	 */
	private Path createDir( String date ) throws IOException {
		Path result = root;
		for( String p : date.split("-(?=\\d{2}$)") ) {
			result = result.resolve(p);
		}
		// sub디렉토리명을 결정하는 람다식을 호출한다.
		result = result.resolve( getSubDir() );
		return Files.createDirectories(result);
	}
	
}
