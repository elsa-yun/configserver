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
		<form:form id="form" method="post" action="${wwwDomain}users/save">
			<table border="0" cellpadding="4" cellspacing="1" class="edit_table">
				<thead>
					<tr style="background: #eee;">
						<th colspan="2" align="center"><strong>添加用户</strong></th>
					</tr>
				</thead>
				<tr>
					<td class="addtable_left">用户名:</td>
					<td class="tdright"><input id="username" name="username"
						type="text"></td>
				</tr>
				<tr>
					<td class="addtable_left">密码:</td>
					<td class="tdright"><input id="password" name="password"
						type="password"></td>
				</tr>
				<tr>
					<td class="addtable_left">确认密码:</td>
					<td class="tdright"><input id="valid_password"
						name="valid_password" type="password"></td>
				</tr>
				<tr>
					<td class="addtable_left">角色:</td>
					<td class="tdright"><input id="role1" name="role" type="radio"
						class="role_tl" value="tl" checked>业务线TL &nbsp;&nbsp; <input
						id="role2" name="role" class="role_tl" type="radio" value="opm">运维</td>
				</tr>
				<tr>
					<td class="addtable_left">可操作应用名称：</td>
					<td class="tdright"><c:forEach items="${appNames}" var="app">
							<label><input name="app_ids_${app.id}" type="checkbox"
								value="${app.id}" class="app_ids" />${app.appName} </label>
						</c:forEach></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><button type="submit"
							name="添加用户" class="right-button04"
							onclick="return validatorForm();">添加用户</button> &nbsp;&nbsp; <a
						href="${wwwDomain}users/list">返回用户列表</a></td>
				</tr>
			</table>

			<c:if test="not empty error">${error}</c:if>
		</form:form>
	</div>
	<script type="text/javascript">
		function validatorForm() {

			var username = jQuery("#username").val();
			var password = jQuery("#password").val();
			var valid_password = jQuery("#valid_password").val();

			var role = jQuery(".role_tl:checked").val();
			var app_ids = jQuery(".app_ids:checked").val();
			
			if (username.trim().length <= 0) {
				alert("请填写用户名！");
				return false;
			}
			if (username.trim().length < 6) {
				alert("用户名长度必须大于6！");
				return false;
			}
			if (password.trim().length <= 0) {
				alert("请填写密码！");
				return false;
			}
			if (password.trim().length < 6) {
				alert("密码长度必须大于6！");
				return false;
			}
			if (valid_password.trim().length <= 0) {
				alert("请再填写一次密码！");
				return false;
			}
			if (valid_password != password) {
				alert("两次输入的密码不致,请重新输入！");
				return false;
			}
			if (role == 'tl') {
				if (typeof (app_ids) == "undefined") {
					alert("请选择可操作的应用！");
					return false;
				}
			}

			//return false;
		}
	</script>
</body>
</html>
