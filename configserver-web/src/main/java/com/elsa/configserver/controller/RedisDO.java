package com.elsa.configserver.controller;

public class RedisDO {

	private String host;

	private int port;

	private int timeout = 2000;

	private int weight = 1;

	private int size = 8000;

	private String redisCacheAllHosts;

	private String oldRedisHosts;
	private String newRedisHosts;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getRedisCacheAllHosts() {
		return redisCacheAllHosts;
	}

	public void setRedisCacheAllHosts(String redisCacheAllHosts) {
		this.redisCacheAllHosts = redisCacheAllHosts;
	}

	public String getOldRedisHosts() {
		return oldRedisHosts;
	}

	public void setOldRedisHosts(String oldRedisHosts) {
		this.oldRedisHosts = oldRedisHosts;
	}

	public String getNewRedisHosts() {
		return newRedisHosts;
	}

	public void setNewRedisHosts(String newRedisHosts) {
		this.newRedisHosts = newRedisHosts;
	}

}
