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
		<c:if test="${success==1}">
			<strong>密码修改成功</strong>
		</c:if>
		<c:if test="${success==2}">
			<strong>原密码输入不正确</strong>
		</c:if>
		<c:if test="${success==0}">
			<form:form id="form" method="post"
				action="${wwwDomain}users/update_pwd">
				<table border="0" cellpadding="4" cellspacing="1" class="edit_table">
					<thead>
						<tr style="background: #eee;">
							<th colspan="2" align="center"><strong>修改</strong></th>
						</tr>
					</thead>
					<tr>
						<td class="addtable_left">用户名:</td>
						<td class="tdright"><input id="username" name="username"
							type="text" value="${username}" readonly="readonly"></td>
					</tr>
					<tr>
						<td class="addtable_left">原密码:</td>
						<td class="tdright"><input id="old_password"
							name="old_password" type="password"></td>
					</tr>
					<tr>
						<td class="addtable_left">新密码:</td>
						<td class="tdright"><input id="password" name="password"
							type="password"></td>
					</tr>
					<tr>
						<td class="addtable_left">确认新密码:</td>
						<td class="tdright"><input id="valid_password"
							name="valid_password" type="password"></td>
					</tr>

					<tr>
						<td colspan="2" align="center"><button type="submit"
								name="修改密码" class="right-button04"
								onclick="return validatorForm();">修改密码</button> &nbsp;&nbsp;</td>
					</tr>
				</table>

			</form:form>
		</c:if>
	</div>
	<script type="text/javascript">
		function validatorForm() {

			var old_password = jQuery("#old_password").val();
			var password = jQuery("#password").val();
			var valid_password = jQuery("#valid_password").val();

			if (old_password.trim().length <= 0) {
				alert("请填写原密码！");
				return false;
			}
			/*if (old_password.trim().length < 6) {
				alert("原密码长度必须大于6！");
				return false;
			}*/
			if (password.trim().length <= 0) {
				alert("请填写新密码！");
				return false;
			}
			if (password.trim().length < 6) {
				alert("新密码长度必须大于6！");
				return false;
			}
			if (valid_password.trim().length <= 0) {
				alert("请再填写一次重复密码！");
				return false;
			}
			if (valid_password != password) {
				alert("两次输入的密码不致,请重新输入！");
				return false;
			}

		}
	</script>
</body>
</html>
