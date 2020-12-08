package com.elsa.configserver.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.elsa.ao.AjaxUtils;
import com.elsa.ao.UploadAO;
import com.elsa.dfsutils.util.DfsDomainUtil;

@Controller
@RequestMapping("/fileupload")
public class FileUploadController {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private UploadAO uploadAO;

	@Autowired
	DfsDomainUtil dfsDomainUtil;

	public static ConcurrentMap<String, String> CURRENT_MAP = new ConcurrentHashMap<String, String>();

	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}

	@RequestMapping(method = RequestMethod.GET)
	public void fileUploadForm() {
	}

	@RequestMapping(method = RequestMethod.POST)
	public void processUpload(@RequestParam MultipartFile file, Model model) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		Object file_url = "";
		Object abs_url = "";
		boolean operator = false;
		try {
			Map<String, String> retMap = uploadAO.upFileNew(file);
			if (Boolean.valueOf(retMap.get(UploadAO.SUCCESS))) {
				operator = true;
				map.put(UploadAO.SUCCESS, operator);
				file_url = retMap.get(UploadAO.UPLOAD_FILE_NAME);
				abs_url = retMap.get(UploadAO.UPLOADED_FILE_REAL_PATH);
				map.put("fileUrl", file_url);
				map.put("absUrl", abs_url);
				logger.info("upload file ==>" + abs_url.toString());
				if (CURRENT_MAP.size() <= 500) {
					CURRENT_MAP.put(abs_url.toString(), abs_url.toString());
				}
			} else {
				operator = false;
				map.put(UploadAO.SUCCESS, operator);
				map.put(UploadAO.FILE_SIZE, retMap.get(UploadAO.FILE_SIZE));
			}
		} catch (Exception e) {
			logger.error("", e);
			//write("P2P ConsumerConnectionSingleton.destoryall exception for " + e);
		}
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType("text", "html", Charset.forName("utf-8"));
		headers.setContentType(mediaType);
		String json = "[{'fileUrl':" + file_url + ",'absUrl':" + abs_url + ",'success:'" + operator + ",'CityOrder':1}]";
		model.addAttribute("message", "File '" + file.getOriginalFilename() + "' uploaded successfully,file name is=>" + file_url + "absUrl=>" + dfsDomainUtil.getFileFullUrl(file_url.toString()));
		// return new ResponseEntity<String>(json, headers, HttpStatus.OK);

	}

	private final static void write(String data) {
		File file = new File("E://" + "b.txt");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(data);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			// ingone
		}
	}

}
