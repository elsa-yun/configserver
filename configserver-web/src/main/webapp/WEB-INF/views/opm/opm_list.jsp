<%@ include file="../common.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<html>
<head>
<title>opm审核配置项</title>
<link href="<c:url value="/resources/css/common.css" />"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
</head>
<body>
<%@ include file="../top_new.jsp"%>
	<div id="formsContent">

		<form:form id="form" method="get" action="${wwwDomain}opm/opm_list">
			<table border="0" cellpadding="4" cellspacing="1" class="edit_table">
				<tr>
					<td>应用名:</td>
					<td><select id="app_id" name="app_id">
							<option id="" value="">请选择应用名</option>
							<c:forEach items="${appNames}" var="app">
								<option id="${app.id}" value="${app.id}"
									${app.id==app_id?'selected':''}>${app.appName}</option>
							</c:forEach>
					</select></td>
					<td>运行环境:</td>
					<td><select id="environment" name="environment">
							<option id="" value="">请选择运行环境</option>
							<c:forEach items="${environments}" var="env">
								<option id="${env}" value="${env}"
									${env==environment?'selected':''}>${env}</option>
							</c:forEach>
					</select></td>
					<td>是否通过:</td>
					<td><select id="is_pass" name="is_pass">
							<option id="" value="">请选择状态</option>
							<option id="" value="default" ${is_pass=='default'?'selected':''}>未审核</option>
							<option id="" value="yes" ${is_pass=='yes'?'selected':''}>审核通过</option>
							<option id="" value="no" ${is_pass=='no'?'selected':''}>审核不通过</option>
					</select></td>
					<td colspan="2" align="center"><button type="submit" name="查询"
							class="right-button04">查询</button></td>
				</tr>
				<tr>
				</tr>
			</table>
		</form:form>

		<table border="0" cellpadding="4" cellspacing="1" class="edit_table">
			<thead>
				<tr>
					<th>ID</th>
					<th>配置文件名</th>
					<th>应用名</th>
					<th>应用状态</th>
					<th>配置文件状态</th>
					<th>环境</th>
					<th>添加时间</th>
					<th>修改时间</th>
					<th>操作</th>
				</tr>
			</thead>
			<c:forEach items="${propConfList}" var="prop">
				<tr>
					<td>${prop.id}</td>
					<td>${prop.appFileNameDO.propFileName}</td>
					<td>${prop.appNameDO.appName}</td>
					<td><c:choose>
							<c:when test="${prop.appNameDO.isDelete == false}">正常</c:when>
							<c:when test="${prop.appNameDO.isDelete == true}">已删除</c:when>
							<c:otherwise>禁用</c:otherwise>
						</c:choose></td>
					<td><c:choose>
							<c:when test="${prop.status == 0}"><font color="red">未审核</font></c:when>
							<c:when test="${prop.status == 1}">启用</c:when>
							<c:when test="${prop.status == 2}">审核不通过</c:when>
							<c:otherwise>禁用</c:otherwise>
						</c:choose></td>
					<td>${prop.environment}</td>
					<td><fmt:formatDate value='${prop.createTime}'
							pattern='yyyy-MM-dd HH:mm:ss' /></td>
					<td><fmt:formatDate value='${prop.modifyTime}'
							pattern='yyyy-MM-dd HH:mm:ss' /></td>
					<td><a href="${wwwDomain}opm/opm_edit/${prop.id}"><c:if
							test="${prop.status==1 || prop.status==0}">审核</c:if><c:if
							test="${prop.status==2}">查看</c:if></a></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>
