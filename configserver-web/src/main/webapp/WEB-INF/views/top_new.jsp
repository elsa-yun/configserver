<%@ include file="common.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<table border="0" cellpadding="4" cellspacing="0" width="100%"
	style="font-color: white; font-size: 11pt; color: #FFFFFF; background-color: #225678; height: 35px">
	<tr>
		<td width="3%" id="top_tr_td"></td>
		<c:if
			test="${user_role=='tl' || user_role=='opm' || user_role=='admin'}">
			<td width="16%" align="left"><strong>当前用户：<font color="black">${userName}</font>,<a
					href="${wwwDomain}login_out"
					style="color: white; text-decoration: none;">退出系统</a></strong></td>
		</c:if>
		<td width="10%" align="left"><strong><a
				href="${wwwDomain}main"
				style="color: white; text-decoration: none;">首页</a></strong></td>
		<c:if
			test="${user_role=='tl' || user_role=='opm' || user_role=='admin'}">
			<td width="10%" align="left"><strong><a
					href="${wwwDomain}users/edit_pwd"
					style="color: white; text-decoration: none;">修改密码</a></strong></td>
		</c:if>
		<c:if test="${user_role=='admin'}">
			<td width="10%" align="left"><strong><a
					href="${wwwDomain}users/list"
					style="color: white; text-decoration: none;">用户管理</a></strong></td>
			<td width="10%" align="left"><strong><a
					href="${wwwDomain}users/add"
					style="color: white; text-decoration: none;">添加用户</a></strong></td>
		</c:if>
		<c:if test="${user_role=='admin'}">
			<td width="10%" align="left"><strong><a
					href="${wwwDomain}appName/list"
					style="color: white; text-decoration: none;">APP应用管理</a></strong></td>
		</c:if>
		<c:if test="${user_role=='tl'}">
			<td width="10%" align="left"><strong><a
					href="${wwwDomain}config/tl_list"
					style="color: white; text-decoration: none;">APP配置文件名管理</a></strong></td>
		</c:if>
		<c:if test="${user_role=='opm'}">
			<td width="10%" align="left"><strong><a
					href="${wwwDomain}opm/opm_list"
					style="color: white; text-decoration: none;">配置审核管理</a></strong></td>
		</c:if>
		<td></td>
	</tr>

</table>


