<%@ include file="common.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>My HTML View</title>
<link href="<c:url value="/resources/form.css" />" rel="stylesheet"
	type="text/css" />
</head>
<body>
	<div class="cacheDB">
		<table  style="border-color: green;border-width: 1px;border-style: solid;">
				<tr>
					<td style="border-bottom-color: green;border-bottom-width: 1px;border-bottom-style: solid; width: 260px">host:port</td>
					<td style="border-bottom-color: green;border-bottom-width: 1px;border-bottom-style: solid; margin-left: 30px;padding-left: 10px">命中率</td>
				</tr>
			<c:forEach items="${hitMap}" var="person">
				<tr>
					<td style="border-bottom-color: green;border-bottom-width: 1px;border-bottom-style: solid; ">${person.key}</td>
					<td style="border-bottom-color: green;border-bottom-width: 1px;border-bottom-style: solid; margin-left: 30px;padding-left: 10px"><strong>${person.value}%</strong></td>
				</tr>
			</c:forEach>
		</table>
		<br>
		<table style="border-color: green;border-width: 1px;border-style: solid;">
			<c:forEach items="${cacheList}" var="person">
				<tr style="border-bottom-color: green;border-bottom-width: 1px;border-bottom-style: solid; ">
					<td>${person.key}</td>
					<td>${person.value}</td>
				</tr>
			</c:forEach>
		</table>
		<br>
		<table style="border-color: green;border-width: 1px;border-style: solid;">
			<tr
				style="border-bottom-color: green; border-bottom-width: 1px; border-bottom-style: solid;">
				<td width="30%">接口</td>
				<td width="15%">方法</td>
				<td width="5%">参数个数</td>
				<td width="10%">消耗时间</td>
				<td width="30%">URL</td>
				<td width="">调用方IP</td>
			</tr>
			<c:forEach items="${hessian_logs}" var="p">
				<tr style="border-bottom-color: green;border-bottom-width: 1px;border-bottom-style: solid; ">
					<td>${p.interfaceName}</td>
					<td>${p.methodName}</td>
					<td  align="center">${p.methodCount}</td>
					<td>${p.methodTime}(毫秒)</td>
					<td>${p.remotingHost}</td>
					<td>${p.methodIp}</td>
				</tr>
			</c:forEach>
			<c:forEach items="${hessians}" var="p">
				<tr style="border-bottom-color: green;border-bottom-width: 1px;border-bottom-style: solid; ">
					<td>${p}</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>