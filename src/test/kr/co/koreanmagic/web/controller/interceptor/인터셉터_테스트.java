package kr.co.koreanmagic.web.controller.interceptor;

import javax.servlet.http.HttpServletRequest;

import kr.co.koreanmagic.web.controller.interceptor.WorkResourceInterceptor;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class 인터셉터_테스트 {

	@Test
	public void test() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getQueryString()).thenReturn("fileType=WorkFile");
		
		WorkResourceInterceptor inter = new WorkResourceInterceptor();
		inter.preHandle(request, null, null);
		
	}

}
