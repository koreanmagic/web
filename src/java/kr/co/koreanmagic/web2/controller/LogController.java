package kr.co.koreanmagic.web2.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.koreanmagic.commons.KoStringUtils;
import kr.co.koreanmagic.web.support.nav.Navigator;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
@Controller
public class LogController {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@RequestMapping(value="/")
	public String root(ModelMap model) throws Exception {
		return "test";
	}
	
	@RequestMapping(value="/test")
	public String test(ModelMap model) throws Exception {
		return "test";
	}
	
	@RequestMapping(value="/admin/{1}/{2}")
	public String admin(ModelMap model, @PathVariable("1") String dir, @PathVariable("2") String file,
						HttpServletRequest req, Navigator nav) throws Exception {
		print(req);
		//AdminNavigator nav = getMenu();
		logger.debug("nav : " + nav.hashCode());
		nav.reflesh(req);
		model.put("nav", nav);
		
		return "admin." + dir + "." + file;
	}
	
	@RequestMapping(value="/ajax-native")
	@ResponseBody
	public String ajaxNative(HttpServletRequest req, @RequestParam("img") List<MultipartFile> resources) throws Exception {
		
		printHeader(req);
		printContent(req);
		printParam(req);
		
		multi(resources);
		
		String body = new String(Files.readAllBytes(Paths.get(getClass().getResource("responseBody.txt").toURI())));
		return body;
	}
	
	private void multi(List<MultipartFile> resources) throws Exception {
		
		for(MultipartFile r : resources) {
			log(r.getOriginalFilename());
			log(r.getSize());
		}
		
	}
	
	@RequestMapping(value="/ajax-param")
	@ResponseBody
	public String ajaxWait(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		printHeader(req);
		printParam(req);
		
		String body = new String(Files.readAllBytes(Paths.get(getClass().getResource("responseBody.txt").toURI())));
		return body;
	}
	
	@RequestMapping(value="/ajax")
	@ResponseBody
	public String ajax(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		logger.debug("servletPath : " + req.getServletPath());
		logger.debug("queryString : " + req.getQueryString());
		
		printParam(req);
		printHeader(req);
		
		String body = new String(Files.readAllBytes(Paths.get(getClass().getResource("responseBody.txt").toURI())));
		return body;
	}
	
	
	
	// Request Parameter 출력
	private void printParam(HttpServletRequest req) {
		Enumeration<String> names = req.getParameterNames();
		String name = null;
		Object value = null;
		
		log("\n▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ Params Value ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
		while(names.hasMoreElements()) {
			
			value = req.getParameter(name = names.nextElement());
			if(value.getClass().isArray())
				log("[" + name + "]  ▒▒▒▒▒▒VALUE->▒▒▒▒▒▒" + arrayValue(value) + "▒▒▒▒▒▒<-VALUE▒▒▒▒▒▒");
			else
				log("[" + name + "]  ▒▒▒▒▒▒VALUE->▒▒▒▒▒▒" + value.toString() + "▒▒▒▒▒▒<-VALUE▒▒▒▒▒▒");
			
		}
		
		log("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ Params Value ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n");
		
	}
	
	// Request Body 출력
	private void printContent(HttpServletRequest req) throws Exception {
		String line = null;
		log("\n▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ CONTENT VALUE ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
		BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream(), "utf-8"));
		while(( line = reader.readLine() ) != null) {
			
			log(line);
			
		}
		log("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ CONTENT VALUE ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n");
	}
	
	private String arrayValue(Object array) {
		return KoStringUtils.join("", ", ", array);
	}
	
	// Request Header
	private void printHeader(HttpServletRequest req) {
		log("\n▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ Request Header ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
		Enumeration<String> names = req.getHeaderNames();
		String name = null;
		while(names.hasMoreElements() && (name = names.nextElement()).length() > 0) {
			log(name + " : " + req.getHeader(name));
		}
		log("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ Request Header ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒\n");
	}
	
	
	private void print(HttpServletRequest req) {
		logger.warn("servletPath() : " + req.getServletPath());
		logger.warn("pathInfo() : " + req.getPathInfo());
		logger.warn("contextPath() : " + req.getContextPath());
		Enumeration<String> e = req.getAttributeNames();
		while(e.hasMoreElements())
			logger.debug(e.nextElement());
			
	}
	
	private<T> T log(T t) {
		System.out.println(t);
		return t;
	}
	

}
