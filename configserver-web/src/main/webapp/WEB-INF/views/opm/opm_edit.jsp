<%@ include file="../common.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<html>
<head>
<title>审核配置项</title>
<link href="<c:url value="/resources/css/common.css" />"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
</head>
<body>
<%@ include file="../top_new.jsp"%>
	<div id="formsContent">

		<form:form id="form" method="post" action="${wwwDomain}opm/opm_update">
			<table border="0" cellpadding="4" cellspacing="1" class="edit_table">
				<thead>
					<tr>
						<th colspan="2" align="center"><strong><c:if
							test="${propConfDO.status==1 || propConfDO.status==0}">审核配置项</c:if><c:if
							test="${propConfDO.status==2}">查看配置项</c:if></strong></th>
					</tr>
				</thead>
				<tr>
					<td class="addtable_left">应用名:<input id="prop_id"
						name="prop_id" value="${propConfDO.id}" type="hidden"></td>
					<td class="tdright">${appNameDO.appName}<input id="app_id"
						name="app_id" value="${appNameDO.id}" type="hidden"><input
						id="app_file_id" name="app_file_id" value="${appFileNameDO.id}"
						type="hidden"></td>
				</tr>
				<tr>
					<td class="addtable_left">文件名:</td>
					<td class="tdright">${appFileNameDO.propFileName}</td>
				</tr>
				<tr>
					<td class="addtable_left">环境:</td>
					<td class="tdright">${propConfDO.environment}</td>
				</tr>
				<tr>
					<td class="addtable_left">是否通过(审核不通过不能再修改内容):</td>
					<td class="tdright"><select id="is_pass" name="is_pass">
							<option id="" value="">请选择审核状态</option>
							<option id="" value="yes" ${propConfDO.status==1?'selected':''}>审核通过</option>
							<option id="" value="no" ${propConfDO.status==2?'selected':''}>审核不通过</option>
					</select></td>
				</tr>
				<c:if test="${propConfDO.environment=='test'}">
					<tr>
						<td class="addtable_left">测试(${propConfDO.environment})环境配置：<input
							id="test_config_id" name="test_config_id"
							value="${propConfDO.id}" type="hidden"></td>
						<td class="tdright"><textarea cols="70" rows="20"
								name="content" id="content">
						${testProp.content}
					</textarea></td>
					</tr>
				</c:if>
				<c:if test="${propConfDO.environment=='staging'}">
					<tr>
						<td class="addtable_left"><font color="red"
							style="font-size: medium;">预发(${propConfDO.environment})环境配置：</font><input
							id="staging_config_id" name="staging_config_id"
							value="${propConfDO.id}" type="hidden"></td>
						<td class="tdright"><textarea cols="150" rows="20"
								name="content" id="content" ${propConfDO.status==2?'readonly':''}>${propConfDO.content}</textarea></td>
					</tr>
					<tr>
						<td class="addtable_left"><font color="red"
							style="font-size: medium;">预发(${propConfDO.environment})环境last配置(只读)：</font></td>
						<td class="tdright"><textarea cols="150" rows="20"
								name="content" id="content" readonly="readonly">${opraterLog.beforeContent}</textarea></td>
					</tr>
				</c:if>
				<c:if test="${propConfDO.environment=='produce'}">
					<tr>
						<td class="addtable_left"><font color="red"
							style="font-size: medium;">线上(${propConfDO.environment})环境配置：</font><input
							id="produce_config_id" name="produce_config_id"
							value="${propConfDO.id}" type="hidden"></td>
						<td class="tdright"><c:if
								test="${propConfDO.status==1 || propConfDO.status==0 }">
								<textarea cols="150" rows="20" name="content" id="content">${propConfDO.content}</textarea>
							</c:if> <c:if test="${propConfDO.status==2}">
								<textarea cols="150" rows="20" name="content" id="content"
									readonly="readonly">${propConfDO.content}</textarea>
							</c:if> &nbsp;&nbsp;&nbsp;&nbsp;<br> <br> <font color="red"
							style="font-size: medium;"></font></td>
					</tr>
					<tr>
						<td class="addtable_left"><font color="red"
							style="font-size: medium;">(${propConfDO.environment})环境last配置(只读)：</font></td>
						<td class="tdright"><textarea cols="150" rows="20" name="beforeContent"
								id="beforeContent" readonly="readonly">${opraterLog.beforeContent}</textarea></td>
					</tr>
				</c:if>
				<tr>
					<td colspan="2" align="center">&nbsp;&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><c:if
							test="${propConfDO.status==1 || propConfDO.status==0}">
							<button type="submit" class="right-button04"
								name="right-button04" id="right-button04"
								onclick="return validatorForm();" value="submit">确认修改</button>
						</c:if></td>
				</tr>
			</table>
		</form:form>
	</div>

	<script type="text/javascript">
		var status = "${propConfDO.status}";
		function validatorForm() {

			var is_pass = jQuery("#is_pass").val();
			var content = jQuery("#content").val();

			if (is_pass == "") {
				alert("请选择审核状态！");
				return false;
			}

			if (content.trim().length <= 0) {
				alert("请填写配置项！");
				return false;
			}

		}
	</script>
</body>
</html>
