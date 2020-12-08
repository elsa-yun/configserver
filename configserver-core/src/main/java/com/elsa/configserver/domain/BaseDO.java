package com.elsa.configserver.domain;

import java.io.Serializable;

public class BaseDO implements Serializable {

	private static final long serialVersionUID = 6581029953195073637L;

	/** 起始页 */
	private int startPage = 1;

	/** 每页记录数 */
	private int pageSize = 20;

	/** 是否读从库,默认读从库 */
	private boolean readSlave = true;

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getStart() {
		if (startPage < 0 || pageSize < 0) {
			return 0;
		} else {
			return ((startPage - 1) * pageSize);
		}
	}

	public boolean isReadSlave() {
		return readSlave;
	}

	public void setReadSlave(boolean readSlave) {
		this.readSlave = readSlave;
	}

}
