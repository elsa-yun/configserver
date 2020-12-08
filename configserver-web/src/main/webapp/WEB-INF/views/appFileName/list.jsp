<%@ include file="../common.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<html>
<head>
<title>修改配置文件名</title>
<link href="<c:url value="/resources/css/common.css" />"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
</head>
<body>
<%@ include file="../top_new.jsp"%>
	<div id="formsContent">
		<table border="0" cellpadding="4" cellspacing="1" class="edit_table">
			<form:form id="form" method="post"
				action="${wwwDomain}appFileName/add">
				<tr>
					<td>添加配置文件名：<input id="app_file_name" name="app_file_name"
						type="text"> <c:if test="not empty error">${error}</c:if>
						<input id="app_id" name="app_id" type="hidden" value="${appId}">
						<button type="submit" name="添加文件名" class="right-button04"
							onclick="return validatorForm();">添加文件名</button>&nbsp;&nbsp; <a
						href="${wwwDomain}appName/list">返回应用列表</a>
					</td>
				</tr>
			</form:form>
		</table>
		<table border="0" cellpadding="4" cellspacing="1" class="edit_table">
			<thead>
				<tr>
					<th>ID</th>
					<th>配置文件文件名</th>
					<th>所属应用</th>
					<th>是否有效</th>
					<th>添加时间</th>
					<th>操作</th>
				</tr>
			</thead>
			<c:forEach items="${appFileNames}" var="appFile">
				<form:form id="form" method="post"
					action="${wwwDomain}appFileName/update/${appFile.id}">
					<tr>
						<td>${appFile.id}<input id="app_file_id" name="app_file_id"
							type="hidden" value="${appFile.id}"><input id="app_id"
							name="app_id" type="hidden" value="${appId}"></td>
						<td><input id="propFileName" name="propFileName" type="text"
							value="${appFile.propFileName}"
							class="propFileName_${appFile.id}"></td>
						<td>${appNameDO.appName}</td>
						<td><c:choose>
								<c:when test="${appFile.isDelete eq false}">启用</c:when>
								<c:otherwise>禁用</c:otherwise>
							</c:choose></td>
						<td><fmt:formatDate value='${appFile.createTime}'
								pattern='yyyy-MM-dd HH:mm:ss' /></td>
						<td><button type="submit" name="修改文件名" class="right-button04"
								onclick="return validator_update_form(${appFile.id});">修改文件名</button></td>
					</tr>
				</form:form>
			</c:forEach>
		</table>
	</div>

	<script type="text/javascript">
		function validatorForm() {
			var app_file_name = jQuery("#app_file_name").val();
			if (app_file_name.trim().length <= 0) {
				alert("请填写配置文件名！");
				return false;
			}
			if (app_file_name.trim().length < 4) {
				alert("配置文件名长度必须大于4！");
				return false;
			}
			if(app_file_name.substr(0,1)=="/" || app_file_name.substr(0,1)=="\\" ){
				alert("配置文件名不能以/t和\开头！");
				return false;
			}
		}

		function validator_update_form(app_id) {
			var app_name = jQuery(".propFileName_" + app_id).val();
			if (app_name.trim().length <= 0) {
				alert("请填写配置文件名！");
				return false;
			}
			if (app_name.trim().length < 4) {
				alert("配置文件名长度必须大于4！");
				return false;
			}
		}
	</script>
</body>
</html>
