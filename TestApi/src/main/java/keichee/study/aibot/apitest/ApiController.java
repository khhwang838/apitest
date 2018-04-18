package keichee.study.aibot.apitest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import keichee.study.aibot.apitest.domain.ReturnDto;
import keichee.study.aibot.apitest.domain.ReturnDto.Doc;

@Controller
@RequestMapping("/test")
public class ApiController {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@ResponseBody
	public Object noResult(HttpServletRequest request, HttpSession session) {
		Gson gson = new Gson();
		ReturnDto rd = new ReturnDto();
		String paramK = (String)request.getParameter("k");
		if ( paramK != null && "점포".equals(paramK)) {
			rd.setNumFound(0);
		} else {
			
			List<Doc> docs = new ArrayList<>();
			rd.setDocs(docs);
			Doc doc = rd.new Doc("title-1", "http://www.naver.com/svc-url-1", "1,000", "원");
			docs.add(doc);
			doc = rd.new Doc("title-2", "http://www.naver.com/svc-url-2", "20", "만원");
			docs.add(doc);
			doc = rd.new Doc("title-3", "http://www.naver.com/svc-url-3", "30", "만원");
			docs.add(doc);
			doc = rd.new Doc("title-4", "http://www.naver.com/svc-url-4", "40", "만원");
			docs.add(doc);
			doc = rd.new Doc("title-5", "http://www.naver.com/svc-url-5", "50", "만원");
			docs.add(doc);
			doc = rd.new Doc("title-6", "http://www.naver.com/svc-url-6", "60", "만원");
			docs.add(doc);
			
			rd.setNumFound(docs.size());
		}
		System.out.println(gson.toJson(rd));
		String result = "{ \"response\" : " + gson.toJson(rd) + "}";
		System.out.println(result);
		System.out.println();
		return result;
	}
}
