package com.elsa.configserver.controller;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ScanDfsFile {

	private static final String _750_JPEG = "_750.jpeg";

	private static final String _750_JPG = "_750.jpg";

	private static final Log LOGGER = LogFactory.getLog(ScanDfsFile.class);

	private static final int FILE_SIZE = 10 * 10000;

	private static List<String> suffixs = new ArrayList<String>();

	private List<String> folders = new ArrayList<String>();

	private List<String> files = new ArrayList<String>();

	private Map<String, String> filesMap = new HashMap<String, String>();

	private int count = 0;

	private int totolCount = 0;

	private String rootPath;

	static {
		suffixs.add(_750_JPG);
		suffixs.add(_750_JPEG);
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public ScanDfsFile() {

	}

	public static void main(String[] args) {
		ScanDfsFile scanFile = new ScanDfsFile();
		scanFile.setRootPath("d:/");
		scanFile.init();
	}

	public void init() {
		File file = new File(this.getRootPath());
		this.treeFolder(file);
		this.treeFile();
		this.scan();
	}

	public void scan() {
		for (String snapshotPath : this.files) {
			String sourceFilePath = "";
			if (snapshotPath.endsWith(_750_JPG)) {
				sourceFilePath = snapshotPath.substring(0, snapshotPath.indexOf(_750_JPG)) + ".jpg";
			}
			if (snapshotPath.endsWith(_750_JPEG)) {
				sourceFilePath = snapshotPath.substring(0, snapshotPath.indexOf(_750_JPEG)) + ".jpeg";
			}
			File snapshot_file = new File(snapshotPath);
			long snapshot_file_length = snapshot_file.length();

			long source_file_length = 0;
			File source_file = new File(sourceFilePath);
			if (source_file.exists()) {
				source_file_length = source_file.length();
			}

			this.filesMap.put(sourceFilePath + ":" + (source_file_length / 1024) + "", snapshotPath + ":" + (snapshot_file_length / 1024) + "");
			if (snapshot_file_length >= source_file_length) {
				LOGGER.info("snapshotPath=>" + snapshotPath + " snapshot_file_length=>" + snapshot_file_length / 1024 + " source_file_path=>" + sourceFilePath + " source_file_length=>"
						+ source_file_length / 1024);
				count++;
			}
		}
		this.totolCount = this.filesMap.size();
		LOGGER.info("dfs file count=>" + this.count);
		LOGGER.info("dfs file totolCount=>" + this.totolCount);

		float num = (float) this.count / (this.count + this.totolCount) * 100;
		DecimalFormat df = new DecimalFormat("0.00");
		String percent = df.format(num);
		LOGGER.info("dfs file percent=>" + percent);
	}

	public void treeFolder(File file) {
		if (file.isDirectory()) {
			if (!this.folders.contains(file.getAbsolutePath())) {
				this.folders.add(file.getAbsolutePath());
			}
			File[] t = file.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if (pathname.isDirectory()) {
						return true;
					} else {
						return false;
					}
				}
			});
			if (null != t) {
				int length = t.length;
				for (int i = 0; i < length; i++) {
					if (t[i].isDirectory()) {
						if (!this.folders.contains(t[i].getAbsolutePath())) {
							this.folders.add(t[i].getAbsolutePath());
							treeFolder(t[i]);
						}
					}
				}
			}
		}
	}

	public void treeFile() {
		for (String folder : this.folders) {
			File file = new File(folder);
			File[] files = file.listFiles(new MyFileFilter(suffixs));
			if (null != files) {
				int length = files.length;
				for (int i = 0; i < length; i++) {
					if (files[i].isDirectory()) {
						treeFile();
					} else {
						if (this.files.size() < FILE_SIZE) {
							this.files.add(files[i].getAbsolutePath());
						}
					}
				}
			}
		}
	}

}

class MyFileFilter implements FileFilter {

	List<String> suffixs = new ArrayList<String>();

	MyFileFilter(List<String> suffixs) {
		this.suffixs = suffixs;
	}

	public boolean accept(File file) {
		for (String suffix : suffixs) {
			if (file.getName().endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}

}
