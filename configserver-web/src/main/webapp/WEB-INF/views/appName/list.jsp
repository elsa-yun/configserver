<%@ include file="../common.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<html>
<head>
<title>修改应用名</title>
<link href="<c:url value="/resources/css/common.css" />"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
</head>
<body>
<%@ include file="../top_new.jsp"%>
	<div id="formsContent">
		<table border="0" cellpadding="4" cellspacing="1" class="edit_table">
			<form:form id="form" method="post" action="add">
				<tr>
					<td>添加应用名：<input id="app_name" name="app_name" type="text">&nbsp;&nbsp;
						<button type="submit" name="添加应用名" class="right-button04"
							onclick="return validatorForm();">添加应用名</button></td>
				</tr>
			</form:form>
		</table>
		<br>
		<table border="0" cellpadding="4" cellspacing="1" class="edit_table">
			<thead>
				<tr style="background: #eee;">
					<th width="5%" align="center">ID</th>
					<th>应用名</th>
					<th>是否有效</th>
					<th>添加时间</th>
					<th>操作</th>
				</tr>
			</thead>
			<c:forEach items="${appNames}" var="app">
				<form:form id="form" method="post" action="update/${app.id}">
					<tr>
						<td>${app.id}<input id="app_id" name="app_id" type="hidden"
							value="${app.id}"></td>
						<td><input id="app_name" name="app_name" type="text" class="app_name${app.id}"
							value="${app.appName}"></td>
						<td>${app.validName}</td>
						<td><fmt:formatDate value='${app.createTime}'
								pattern='yyyy-MM-dd HH:mm:ss' /></td>
						<td><button type="submit" name="修改应用名" class="right-button04" onclick="return validator_update_form(${app.id});">修改应用名</button>&nbsp;&nbsp;<a
							href="${wwwDomain}appFileName/list/${app.id}">添加配置文件名</a>&nbsp;&nbsp;&nbsp;<a href="${wwwDomain}appName/del/${app.id}" onclick="return confirm('删除数据将无法恢复,请慎重操作？');">删除</a></td>
					</tr>
				</form:form>
			</c:forEach>
		</table>
	</div>
	<script type="text/javascript">
		function validatorForm() {
			var app_name = jQuery("#app_name").val();
			if (app_name.trim().length <= 0) {
				alert("请填写应用名！");
				return false;
			}
			if (app_name.trim().length < 4) {
				alert("应用名长度必须大于4！");
				return false;
			}

		}

		function validator_update_form(app_id) {
			var app_name = jQuery(".app_name" + app_id).val();
			if (app_name.trim().length <= 0) {
				alert("请填写应用名！");
				return false;
			}
			if (app_name.trim().length < 4) {
				alert("应用名长度必须大于4！");
				return false;
			}
		}
	</script>
</body>
</html>
