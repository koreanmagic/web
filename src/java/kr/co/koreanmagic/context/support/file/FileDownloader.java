package kr.co.koreanmagic.context.support.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface FileDownloader {
	
	// 실제 저장될 계산된 Path를 보내준다.
	Path getRoot();
	
	Path saveFile(InputStream is, String file) throws IOException;

}
