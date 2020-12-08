package com.elsa.seller.datasource;

public class ContextHolder {

	public static final String MASTER_ORDER_DATE_SOURCE = "master_supplier_data_source";

	public static final String SLAVE_ORDER_DATE_SOURCE = "slave_supplier_data_source";

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setCustomerKey(String data_source_key) {
		contextHolder.set(data_source_key);
	}

	public final static String getCustomerKey() {
		String string = contextHolder.get();
		clearCustomerKey();
		return string;
	}

	public final static void clearCustomerKey() {
		contextHolder.remove();
	}

}