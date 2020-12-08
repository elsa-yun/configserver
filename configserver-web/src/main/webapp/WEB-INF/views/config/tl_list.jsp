<%@ include file="../common.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<html>
<head>
<title>添加配置项</title>
<link href="<c:url value="/resources/css/common.css" />"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
</head>
<body>
<%@ include file="../top_new.jsp"%>
	<div id="formsContent">
		<table border="0" cellpadding="4" cellspacing="1" class="edit_table">
			<thead>
				<tr>
					<th>ID</th>
					<th>应用名</th>
					<th>应用状态</th>
					<th>添加时间</th>
					<th>配置文件名</th>
				</tr>
			</thead>
			<c:forEach items="${userAppList}" var="app">
				<tr>
					<td>${app.id}</td>
					<td>${app.appName}</td>
					<td><c:choose>
							<c:when test="${app.isDelete eq false}">启用</c:when>
							<c:otherwise>禁用</c:otherwise>
						</c:choose></td>
					<td><fmt:formatDate value='${app.createTime}'
							pattern='yyyy-MM-dd HH:mm:ss' /></td>
					<td><c:forEach items="${app.fileNameList}" var="configFile">
						${configFile.propFileName} <c:if
								test="${configFile.hasApplyConf =='no'}">
								<a href="${wwwDomain}config/tl_apply/${app.id}/${configFile.id}">添加修改配置项</a>&nbsp;&nbsp;&nbsp;&nbsp;
							</c:if>
							<c:if test="${configFile.hasApplyConf =='yes'}">&nbsp;&nbsp;&nbsp;&nbsp;<Strong
									style="color: red">存在未审核配置项</Strong>&nbsp;&nbsp;&nbsp;&nbsp;<a
									href="${wwwDomain}config/tl_edit/${app.id}/${configFile.id}">查看并修改</a>
							</c:if>
							<br>
							<br>
						</c:forEach></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>
