package com.elsa.seller.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class SellerDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return ContextHolder.getCustomerKey();
	}

}
