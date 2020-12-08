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
		<form:form id="form" method="post"
			action="${wwwDomain}config/tl_apply/add">
			<table border="0" cellpadding="4" cellspacing="1" class="edit_table">
				<thead>
					<tr>
						<th colspan="2" align="center"><strong>添加配置项</strong></th>
					</tr>
				</thead>
				<tr>
					<td class="addtable_left">应用名:</td>
					<td class="tdright">${app.appName}<input id="app_id"
						name="app_id" value="${app.id}" type="hidden"><input
						id="app_file_id" name="app_file_id" value="${appFile.id}"
						type="hidden"></td>
				</tr>
				<tr>
					<td class="addtable_left">文件名:</td>
					<td class="tdright">${appFile.propFileName}</td>
				</tr>
				<c:if test="${environment=='test'}">
					<tr>
						<td class="addtable_left">测试(test)环境配置:</td>
						<td class="tdright"><textarea cols="70" rows="20"
								id="test_content" name="test_content"></textarea></td>
					</tr>
				</c:if>
				<c:if test="${environment=='staging' || environment=='produce'}">
					<tr>
						<td class="addtable_left">预发(staging)环境配置:</td>
						<td class="tdright"><textarea cols="130" rows="20"
								name="staging_content" id="staging_content"></textarea></td>
					</tr>
					<tr>
						<td class="addtable_left">线上(produce)环境配置:</td>
						<td class="tdright"><textarea cols="130" rows="20"
								name="produce_content" id="produce_content"></textarea></td>
					</tr>
				</c:if>
				<tr>
					<td colspan="2" align="center">&nbsp;&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><button type="submit"
							name="添加配置项" class="right-button04"
							onclick="return validatorForm();">添加配置项</button>${errorMsg}</td>
				</tr>
			</table>
		</form:form>
	</div>

	<script type="text/javascript">
		var environment = "${environment}";

		function validatorForm() {

			if (environment == "test") {
				var test_content = jQuery("#test_content").val();
				if (test_content.trim().length <= 0) {
					alert("请填写测试环境配置！");
					return false;
				}
			}
			if (environment == "staging" || environment == "produce") {
				var staging_content = jQuery("#staging_content").val();
				var produce_content = jQuery("#produce_content").val();
				if (staging_content.trim().length <= 0) {
					alert("请填写预发staging环境配置！");
					return false;
				}
				if (produce_content.trim().length <= 0) {
					alert("请填写生产produce环境配置！");
					return false;
				}
			}
		}
	</script>
</body>
</html>
