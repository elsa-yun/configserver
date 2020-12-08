package com.elsa.configserver.client;

public class TestHessianImpl implements TestHessian {

	@Override
	public String get_test_hessian_str() {
		try {
			Thread.sleep(510);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "good boy";
	}

	@Override
	public String get_test_hessian_str_new() {
		try {
			Thread.sleep(510);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "get_test_hessian_str_new";
	}

}
