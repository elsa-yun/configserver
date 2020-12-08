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
		<form:form id="form" method="post" action="${wwwDomain}users/update">
			<table border="0" cellpadding="4" cellspacing="1" class="edit_table">
				<thead>
					<tr style="background: #eee;">
						<th colspan="2" align="center"><strong>修改用户</strong></th>
					</tr>
				</thead>
<!-- 				<tr>
					<td class="addtable_left">角色:</td>
					<td class="tdright"><input id="role1" name="role" type="radio"
						class="role_tl" value="tl" checked>业务线TL &nbsp;&nbsp; <input
						id="role2" name="role" class="role_tl" type="radio" value="opm">运维</td>
				</tr> -->
				<tr>
					<td class="addtable_left">可操作应用名称：</td>
					<td class="tdright"><c:forEach items="${appNames}" var="app">
							<label><input name="app_ids_${app.id}" type="checkbox"
								<c:if test="${app.checked eq '1'}">checked='checked'</c:if>
								value="${app.id}" class="app_ids" />${app.appName} </label>
						</c:forEach><input type="hidden" name="m_user_id" value="${m_user_id}" id="id"></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><button type="submit"
							name="添加用户" class="right-button04"
							onclick="return validatorForm();">修改用户</button> &nbsp;&nbsp; <a
						href="${wwwDomain}users/list">返回用户列表</a></td>
				</tr>
			</table>

			<c:if test="not empty error">${error}</c:if>
		</form:form>
	</div>
	<script type="text/javascript">
		function validatorForm() {

			var role = jQuery(".role_tl:checked").val();
			var app_ids = jQuery(".app_ids:checked").val();

			if (role == 'tl') {
				if (typeof (app_ids) == "undefined") {
					alert("请选择可操作的应用！");
					return false;
				}
			}

			return true;
		}
	</script>
</body>
</html>
