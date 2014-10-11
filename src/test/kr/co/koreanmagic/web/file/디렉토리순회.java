package kr.co.koreanmagic.web.file;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import kr.co.koreanmagic.commons.KoCollectionView;
import kr.co.koreanmagic.commons.KoFileUtils;
import kr.co.koreanmagic.commons.KoReflectionView;
import kr.co.koreanmagic.web.db.mybatis.config.ConfigureMyBatis;
import kr.co.koreanmagic.web.db.mybatis.mapper.WorkConditionMapper;
import kr.co.koreanmagic.web.db.mybatis.mapper.WorkFileMapper;
import kr.co.koreanmagic.web.db.mybatis.mapper.WorkMapper;
import kr.co.koreanmagic.web.domain.Work;
import kr.co.koreanmagic.web.domain.WorkFile;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ConfigureMyBatis.class})
public class 디렉토리순회 {
	
	
	static Logger logger = Logger.getLogger("test");
	
	@Autowired private WorkMapper workMapper;
	@Autowired private WorkConditionMapper workConditionMapper;
	@Autowired private WorkFileMapper workFileMapper;
	
	final String ROOT = "g:/작업공유-사본";
	private static Scanner scan = new Scanner(System.in);
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired DataSource dataSource;
	
	
	@Test @Ignore
	public void 이름바꾸기() throws Exception {
		Path root = Paths.get(ROOT, "00. 작업완료");
		Files.walkFileTree(root, new ListTree(
				new FileNameChange("\\b\\d{9}",
								new FileNameChangeInterface() {
									@Override
									public String change(String fileName) {
										StringBuilder sb = new StringBuilder(fileName);
										
										return sb.delete(6, 9).toString();
									}
						}
				)));
	}
	
	
	@Test @Ignore
	public void 타이핑으로이름바꾸기() throws Exception {
		Path root = Paths.get(ROOT, "00. 작업완료");
		Files.walkFileTree(root, new ListTree(
				new RenameWriter("_\\d{2,3}[a-z]*\\.")));
	}
	
	@Test @Ignore
	public void 매칭되는문자지우기() throws Exception {
		StringBuilder test = new StringBuilder("120223001_마평텔레콤(김천)_(2건)_262-187mm(2).pdf");
		Matcher m = Pattern.compile("\\d{9}_([가-힣a-zA-Z0-9]+).*[a-zA-Z]{2}\\.[a-zA-Z]{2,3}").matcher(test);
		
		
		while(m.find()) {
			logger.debug(test.delete(m.start(1), m.end(1)));
		}
	}
	
	
	
	@Test
	public void 파일보기() throws Exception {
		Path root = Paths.get(ROOT, "00. 작업완료");
		Files.walkFileTree(root, new ListTree(
								new DefaultView(
										"\\b\\d{6}_", false, new CustomView())));
	}
	private static String[] patterns = {
		// 씨박 순서가 중요하다.ㅠㅠ
		"\\b\\d{9}_[가-힣a-zA-Z0-9]+_?\\(?(.+?)\\)?_", // 태그
		"\\d{9}_([가-힣a-zA-Z0-9]+)", // 회사명
		"\\b(\\d{9})", // 날짜(아이디)
		"\\d{2,3}-\\d{2,3}([a-zA-Z]{2})", // 단위
		"(\\d{2,3}-\\d{2,3})", // 사이즈
		"\\((\\d)\\)\\.", // 재단선
		"\\[\\d+-(\\d+)\\]", // 수량
		"\\[(\\d+)-\\d+\\]", // 건수
		"(\\d+)건", // 건수
		"\\.([a-zA-Z]{2,3}$)", // 확장자
		"(박스|대봉투|명함|전단|포스터|책|티켓|청첩장|소봉투|자켓봉투|리플렛|카다로그|팜플렛|자격증|신분증|빌지|영수증|NCR|보고서|엽서|스티커|주문서|양식지|마스터|카렌다|쿠폰|초대권|명찰|브로슈어|메달)",
		
	};
	private static Pattern[] regexs;
	static {
		int length = patterns.length;
		regexs = new Pattern[length];
		for(int i = 0; i < length; i++) {
			regexs[i] = Pattern.compile(patterns[i]);
		}
	}
	
	class CustomView implements DefaultViewInterface {
		Set<String> set = new HashSet<>();
		int count = 1;
		@Override
		public String reverse(Path path, String fileNamem, int count) {
			return null;
		}
		private String attachNum(String s) {
			if(s == null || s.length() == 0) return null;
			return this.count++ + ") " + s;
		}
		@Override
		public String view(Path path, String fileName, Matcher m, int count) {
			Work work = WorkFactory.fillWork(fileName);
			WorkFile workFile = new WorkFile();
			String result = null;
			try {
				work = WorkPostProcessor.post(work);
				work.setDeliveryTime(work.getInsertTime());
				Path filePath = path.subpath(path.getNameCount()-3, path.getNameCount()); // 파일패스 입력
				String fileName2 = filePath.getFileName().toString();
				workFile.setFileType(KoFileUtils.getFileType(fileName2));
				workFile.setFileName(fileName2.substring(0, fileName2.lastIndexOf(".")));
				
				workFile.setParentPath(filePath.getParent().toString().replace("\\", "/"));
				work.setId(workMapper.sequence(work.getInsertTime()));
				workFile.setWorkId(work.getId());
				
				if(workMapper.insertSelective(work) == 1) {
					logger.debug(work + " : 상겅");
					workFileMapper.insertSelective(workFile);
				}
				
				
				
			} catch (RuntimeException e) {
				logger.debug("error : " + work + " // " + fileName);
				throw e;
			}
			return attachNum(result);
		}
		
		private void print(Object obj) {
			StringBuilder sb = new StringBuilder();
			List<String> list = KoCollectionView.map(KoReflectionView.printMethodReturnValues(obj, String.class, null));
			for(String s: list) {
				sb.append(s).append(", ");
			}
			logger.debug(sb.toString());
		}
		
		private StringBuilder delete(StringBuilder sb, Pattern pattern) {
			Matcher m = pattern.matcher(sb);
			int groupCount = 0;
			while(m.find()) {
				logger.debug(sb.delete(0, 2)); // 매칭된걸 전부 지운다
				
				/*groupCount = m.groupCount();
				for(int i = 0; i < groupCount; i++) {
					sb.delete(m.start(i+1), m.end(i+1)); // 매칭된걸 전부 지운다
				}*/
			}
			return sb;
		}
	}
	
	
	static class ListTree extends SimpleFileVisitor<Path> {
		private Accept accept;
		DirectoryStream<Path> stream;

		public ListTree(Accept accept)  {
			this.accept = accept;
		}
		
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			
			stream = Files.newDirectoryStream(dir, new DirectoryStream.Filter<Path>() {
				@Override
				public boolean accept(Path entry) throws IOException {
					return !Files.isDirectory(entry, LinkOption.NOFOLLOW_LINKS);
				}
			});
			
			for(Path file : stream) {
				if(!accept.accept(file))
					return FileVisitResult.TERMINATE;
			}
			return FileVisitResult.CONTINUE;
		}
		
		StringBuffer buf = new StringBuffer();
		private StringBuffer getBuf(String sequence) {
			buf.delete(0, buf.length());
			return buf.append(sequence);
		}
		
	}
	
	
	// 인터페이스
	static interface Accept { boolean accept(Path path); }
	//보조 클래스
	static abstract class AcceptImpl implements Accept  {
		private Pattern pattern;
		private Matcher m;
		private int count = 1;
		public AcceptImpl(String pattern) { this.pattern = Pattern.compile(pattern); }
		public Matcher getMatcher(String input) {
			m = this.pattern.matcher(input);
			return m;
		}
		public final boolean accept(Path path) {
			boolean continues = acceptPath(path, path.getFileName().toString());
			count++;
			return continues;
		}
		protected int getCount() { return this.count; }
		abstract boolean acceptPath(Path path, String fileName);
	}
	
	
	
	// 패턴에 맞는 파일 찾아서 파일명 바꾸기
	static interface FileNameChangeInterface { // 인터페이스 구현
		String change(String fileName);
	}
	static class FileNameChange extends AcceptImpl {
		private FileNameChangeInterface change;
		public FileNameChange(String pattern, FileNameChangeInterface change) {
			super(pattern);
			this.change = change;
		}
		@Override
		boolean acceptPath(Path path, String fileName) {
			Matcher m = getMatcher(fileName);
			Path target = null;
			try {
				if(m.find()) {
					target = path.resolveSibling(change.change(fileName));
					Files.move(path, target, StandardCopyOption.REPLACE_EXISTING);
					logger.debug(fileName + "\t\t   ▶▶   " + target.getFileName().toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}
	
	
	// 매칭되는 파일명 출력
	static interface DefaultViewInterface {
		String reverse(Path path, String fileNamem, int count);
		String view(Path path, String fileName, Matcher m,  int count);
	}
	static class DefaultView extends AcceptImpl {
		DefaultViewInterface view;
		boolean reverse;
		int reverseCount = 1;
		
		public DefaultView(String pattern) {
			this(pattern, false, new DefaultViewInterface() {
										@Override
											public String reverse(Path path, String fileName, int count) {
												return count + ">> " + fileName;
											}
										@Override
											public String view(Path path, String fileName, Matcher m, int count) {
												String result = "";
												result += (count + ") " + fileName + "\t\t");
												if(m.groupCount() > 0) {
													for(int i = 0; i < m.groupCount(); i++) {
														result += "【" + (i + 1) + "】 " + m.group(i+1) + ", ";  
													}
												}
												return result;
											}
										});
		}
		
		public DefaultView(String pattern, boolean reverse, DefaultViewInterface view) {
			super(pattern);
			this.reverse = reverse;
			this.view = view;
		}
		
		@Override public boolean acceptPath(Path path, String fileName) {
			Matcher m = getMatcher(fileName);
			String view = null;
			boolean flag = m.find();
			if(reverse && !flag) {
				view = this.view.reverse(path, fileName, reverseCount++);
			} else if(flag) {
				view = this.view.view(path, fileName, m, getCount());
			}
			if(view != null)
				logger.debug(view);
			return true;
		}
	}
	
	
	// 파일명 수동 바꾸기
	static class RenameWriter extends AcceptImpl {
		public RenameWriter(String pattern) {
			super(pattern);
		}

		@Override public boolean acceptPath(Path path, String fileName) {
			String newName = null;
			Path newPath = null;
			try {
				if(getMatcher(fileName).find()) { // 매칭될때만
					logger.debug(path);
					newName = scan.nextLine();
					if(newName.contains("pass")) {
						logger.debug("pass------------> \n");
					} else if(newName.contains("exit")){
						logger.debug("종료합니다.");
						return false;
					} else {
						newPath = path.resolveSibling(newName);
						Files.move(path, newPath, StandardCopyOption.REPLACE_EXISTING);
						logger.debug(fileName + "  ▶▶▶ " + newPath.getFileName() + "\n ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ \n");
					}
				}
			} catch (Exception e) {
				logger.debug("★★★★★★★★★★★ 변경 실패 ★★★★★★★★★★");
				return false;
			}
			
			return true;
		}
	}
	
	
	/*class DBInsert extends AcceptImpl {
		
		private InsertBean<Work> insertBean;
		
		public DBInsert(InsertBean<Work> insertBean) {
			super(insertBean.getPattern());
			this.insertBean = insertBean;
		}
		
		@Override public boolean acceptPath(Path path, String fileName) {
			Path newPath = null;
			Matcher m = getMatcher(fileName);
			Work work = null;
			if(!fileName.startsWith("_") && path.getNameCount() > 3) {
				if(m.find()) {
					newPath = path.resolveSibling("_" + fileName);
					work = insertBean.fillBean(m);
					work.setFilepath(path.subpath(path.getNameCount()-3, path.getNameCount()).toString()); // 파일패스 입력
					workMapper.insertSelective(work);
					
					try {
						Files.move(path, newPath, StandardCopyOption.REPLACE_EXISTING);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					out(getCount() + ">> " + path.getFileName().toString());
					return true;
				}
			}
			return false;
		}
	}*/
}
