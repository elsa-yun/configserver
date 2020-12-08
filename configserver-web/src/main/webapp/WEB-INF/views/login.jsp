<%@ include file="common.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<html>
<head>
<title>添加配置项</title>
<link href="<c:url value="/resources/css/left.css" />" rel="stylesheet"
	type="text/css" />
<link href="<c:url value="/resources/css/common.css" />"
	rel="stylesheet" type="text/css" />

</head>
<script type="text/javascript">
</script>
<body>
	<div id="formsContent" style="">
		<form:form id="form" method="post" action="${wwwDomain}login_in">
			<table border="0" cellpadding="4" cellspacing="1"
				class="edit_table_login" algin="center">
				<thead>
					<tr>
						<th colspan="2" align="center"><strong>用户登录</strong></th>
					</tr>
				</thead>
				<tr>
					<td class="addtable_left">用户名:</td>
					<td class="tdright"><input id="username" name="username"
						value="" type="text"></td>
				</tr>
				<tr>
					<td class="addtable_left">密码:</td>
					<td class="tdright"><input id="password" name="password"
						value="" type="password"></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><button type="submit" name="登录"
							class="right-button04">登录</button></td>
				</tr>
			</table>
		</form:form>
	</div>
</body>
</html>
