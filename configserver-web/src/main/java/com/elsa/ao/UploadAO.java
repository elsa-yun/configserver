package com.elsa.ao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.elsa.dfsutils.constants.MetaDataKey;
import com.elsa.dfsutils.file.GeneralFile;
import com.elsa.dfsutils.file.ImgFile;
import com.elsa.dfsutils.file.TxtFile;
import com.elsa.dfsutils.service.DfsService;
import com.elsa.dfsutils.util.DfsDomainUtil;

public class UploadAO {

	public static final String FILE_SIZE = "file size";

	public static final String UPLOADED_FILE_REAL_PATH = "uploaded_file_real_path";

	public static final String UPLOAD_FILE_NAME = "upload_file_name";

	public static final String SUCCESS = "success";

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private DfsService dfsService;

	public DfsDomainUtil dfsDomainUtil;

	private String uploadTempPath;

	public Map<String, Object> uploadFile(HttpServletRequest request, String fileName) throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		File retFile = null;
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile(fileName);
			if (null == multipartFile || multipartFile.isEmpty()) {
				logger.info("empty " + fileName);
				retMap.put(SUCCESS, false);
				retMap.put("msg", "empty " + fileName);
				return retMap;
			}
			long fileSize = multipartFile.getSize();
			String newName = generateFileName();
			String format = getFileFormat(multipartFile.getOriginalFilename());
			File destFile = new File(uploadTempPath);
			if (!destFile.exists()) {
				destFile.mkdirs();
			}
			retFile = new File(destFile, newName + "." + format);
			FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), retFile);
			String upFile = uploadFile(retFile);
			if (null == upFile) {
				retMap.put(SUCCESS, false);
				retMap.put("msg", " upload error ");
				return retMap;
			}
			retMap.put(SUCCESS, true);
			retMap.put(FILE_SIZE, fileSize);
			retMap.put(UPLOAD_FILE_NAME, upFile);
			retMap.put(UPLOADED_FILE_REAL_PATH, dfsDomainUtil.getFileFullUrl(upFile));
		} else {
			logger.info("request");
		}
		return retMap;
	}

	public Map<String, String> upFileNew(MultipartFile multipartFile) throws IOException {
		Map<String, String> retMap = new HashMap<String, String>();
		if (null == multipartFile || multipartFile.isEmpty()) {
			retMap.put(SUCCESS, "false");
			return retMap;
		}
		long fileSize = multipartFile.getSize();
		String newName = generateFileName();
		String format = getFileFormat(multipartFile.getOriginalFilename());
		File destFile = new File(uploadTempPath);
		if (!destFile.exists()) {
			destFile.mkdirs();
		}
		File retFile = new File(destFile, newName + "." + format);
		FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), retFile);
		String upFile = uploadFile(retFile);
		if (null == upFile) {
			retMap.put(SUCCESS, "false");
			retMap.put("msg", " upload error ");
			return retMap;
		}
		retMap.put(SUCCESS, "true");
		retMap.put(FILE_SIZE, fileSize + "");
		retMap.put(UPLOAD_FILE_NAME, upFile);
		retMap.put(UPLOADED_FILE_REAL_PATH, dfsDomainUtil.getFileFullUrl(upFile));
		return retMap;
	}

	public static String getFileFormat(String fileName) {
		if (fileName.indexOf(".") > 0) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		} else {
			return "";
		}
	}

	public void download(HttpServletRequest request, HttpServletResponse response, String fileId) {
		response.setContentType("text/html;charset=utf-8");
		ServletOutputStream out = null;
		try {
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", -1);
			response.addHeader("Content-Disposition", "attachment; filename=" + getFileName(fileId));

			out = response.getOutputStream();
			byte[] buff = dfsService.getFileBytes(fileId);
			if (null == buff) {
				throw new FileNotFoundException("dfs鏈壘鍒帮細" + fileId);
			}
			int len = buff.length;
			out.write(buff, 0, len);
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}
	}

	private String getFileName(String fileId) {
		String format = getFileFormat(fileId);
		return generateFileName() + "." + format;
	}

	public static String generateFileName() {
		return UUID.randomUUID().toString();
	}

	public String uploadFile(File file) {
		GeneralFile info = new GeneralFile();
		String fileId = null;
		info.setFile(file);
		Map<MetaDataKey, String> map = new HashMap<MetaDataKey, String>();
		map.put(MetaDataKey.FILENAME, file.getName());
		map.put(MetaDataKey.CREATOR, " lhs file ");
		info.setMetaData(map);
		try {
			fileId = dfsService.uploadFile(info);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return fileId;
	}

	private String uploadRarOrZipFile(File file) {
		TxtFile info = new TxtFile();
		String fileId = null;
		info.setFile(file);
		Map<MetaDataKey, String> map = new HashMap<MetaDataKey, String>();
		map.put(MetaDataKey.FILENAME, file.getName());
		map.put(MetaDataKey.CREATOR, "longhaisheng");
		info.setMetaData(map);
		try {
			fileId = dfsService.uploadFile(info);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return fileId;
	}

	private String uploadImgFile(File file) {
		ImgFile info = new ImgFile();
		String fileId = null;
		info.setFile(file);
		Map<MetaDataKey, String> map = new HashMap<MetaDataKey, String>();
		map.put(MetaDataKey.FILENAME, file.getName());
		map.put(MetaDataKey.CREATOR, "longhaisehng");
		info.setMetaData(map);
		try {
			fileId = dfsService.uploadFile(info);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return fileId;
	}

	public DfsService getDfsService() {
		return dfsService;
	}

	public void setDfsService(DfsService dfsService) {
		this.dfsService = dfsService;
	}

	public DfsDomainUtil getDfsDomainUtil() {
		return dfsDomainUtil;
	}

	public void setDfsDomainUtil(DfsDomainUtil dfsDomainUtil) {
		this.dfsDomainUtil = dfsDomainUtil;
	}

	public String getUploadTempPath() {
		return uploadTempPath;
	}

	public void setUploadTempPath(String uploadTempPath) {
		this.uploadTempPath = uploadTempPath;
	}

}
