package com.elsa.configserver.controller;

public class HessianDO {

	private String interfaceName;

	private String methodName;

	private String methodIp;

	private int methodTime;

	private String methodCount;

	private String remotingHost;

	private String callTimes;

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodIp() {
		return methodIp;
	}

	public void setMethodIp(String methodIp) {
		this.methodIp = methodIp;
	}

	public int getMethodTime() {
		return methodTime;
	}

	public void setMethodTime(int methodTime) {
		this.methodTime = methodTime;
	}

	public String getMethodCount() {
		return methodCount;
	}

	public void setMethodCount(String methodCount) {
		this.methodCount = methodCount;
	}

	public String getRemotingHost() {
		return remotingHost;
	}

	public void setRemotingHost(String remotingHost) {
		this.remotingHost = remotingHost;
	}

	public String getCallTimes() {
		return callTimes;
	}

	public void setCallTimes(String callTimes) {
		this.callTimes = callTimes;
	}
}
