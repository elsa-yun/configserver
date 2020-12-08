package com.elsa.configserver.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class IpController {

	@RequestMapping(value = "/ip_test", method = RequestMethod.GET)
	public byte[] Test(HttpServletRequest request, HttpServletResponse response) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(getLocalIPList().toString());
		sb.append("localIP=>" + getLocalIP());
		String str = sb.toString();

		response.setContentType("application/octet-stream");
		OutputStream out = response.getOutputStream();
		out.write(str.getBytes("utf-8"));
		out.flush();
		out.close();
		return null;
	}

	// private static String getLocalIP() {
	// try {
	// return InetAddress.getLocalHost().getHostAddress();
	// } catch (Exception e) {
	// return "";
	// }
	// }

	public static String getLocalIP() {
		List<String> ipList = getLocalIPList();
		for (Iterator<String> iterator = ipList.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			if ("127.0.0.1".equals(string)) {
				iterator.remove();
			}
		}
		if (ipList.size() > 0) {
			return ipList.get(0);
		}
		return "127.0.0.1";
	}

	public static List<String> getLocalIPList() {
		List<String> ipList = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			NetworkInterface networkInterface;
			Enumeration<InetAddress> inetAddresses;
			InetAddress inetAddress;
			String ip;
			while (networkInterfaces.hasMoreElements()) {
				networkInterface = networkInterfaces.nextElement();
				inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					inetAddress = inetAddresses.nextElement();
					if (inetAddress != null && inetAddress instanceof Inet4Address) {
						ip = inetAddress.getHostAddress();
						ipList.add(ip);
					}
				}
			}
		} catch (SocketException e) {

		}
		Collections.sort(ipList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		return ipList;
	}

}
