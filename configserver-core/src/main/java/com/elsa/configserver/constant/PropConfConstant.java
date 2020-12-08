package com.elsa.configserver.constant;

;

/**
 * 应用配置项 常量
 * 
 * @author haisheng.Long 2015-01-28 18:51:19
 */
public interface PropConfConstant {

	String ENVIRONMENT_TEST = "test";

	String ENVIRONMENT_STAGING = "staging";

	String ENVIRONMENT_PRODUCE = "produce";

	/** 0：申请中;1: 审核通过;2:审核不通过 */
	int STATUS_DEFAULT = 0;

	/** 0：申请中;1: 审核通过;2:审核不通过 */
	int STATUS_PASS = 1;

	/** 0：申请中;1: 审核通过;2:审核不通过 */
	int STATUS_NOT_PASS = 2;

}
