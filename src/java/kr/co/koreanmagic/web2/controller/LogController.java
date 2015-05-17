package kr.co.koreanmagic.web2.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.koreanmagic.commons.KoStringUtils;
import kr.co.koreanmagic.service.CustomerService;
import kr.co.koreanmagic.service.WorkService;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
@Controller
public class LogController {
	
	private Logger logger = Logger.getLogger(getClass());
	@Autowired SessionFactory factory;
	@Autowired CustomerService customerService;
	@Autowired WorkService workService;
	
	
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
		
		JSONObject json = new JSONObject();
		json.put("result", "true");
		log(json.toJSONString());
		return json.toJSONString();
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
	
	private List<Map<String, Object>> getDummyData() throws Exception {
		
		Path path = Paths.get("g:/data.txt");
		
		List<Map<String, Object>> result = new ArrayList<>();
		List<String> lines = Files.readAllLines(path, Charset.forName("euc-kr"));
		
		String[] s = null, labels = {"date", "customer", "item", "memo", "size", "count", "cost", "price", "constructor"};
		
		int i=0, len = labels.length;
		Map<String, Object> model = null;
		for(String l : lines) {
			s = l.split(",");
			model = new HashMap<>();
			for(i=0;i<len;i++) {
				if(labels[i].equals("date"))
					model.put(labels[i], new Date());	
				else
					model.put(labels[i], s[i]);
			}
			result.add(model);
		}
		
		return result;
	}
	
	protected void log(Object...t) {
		System.out.println(KoStringUtils.join("", ",", t));
	}
	

}
