package com.elsa.seller.datasource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

public class MeitunDataSourceTransactionManager extends DataSourceTransactionManager {

	private Log logger = LogFactory.getLog(this.getClass());

	private static final long serialVersionUID = -7775738079919982096L;

	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		super.doCleanupAfterCompletion(transaction);
		ContextHolder.clearCustomerKey();
		if (logger.isDebugEnabled()) {
			logger.debug("==================================================================================================");
		}
	}

}
