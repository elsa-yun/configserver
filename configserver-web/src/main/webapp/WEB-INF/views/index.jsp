<%@ include file="common.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>统一配置管理系统00000</title>
<script type="text/javascript"
	src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
<link href="<c:url value="/resources/css/left.css" />" rel="stylesheet"
	type="text/css" />
</head>
<script type="text/javascript">
	var role = "${user_role}";
	var url = window.location.href;
	if (url.substr(url.length - 1, url.length) == "/") {
		url = url.substr(0, url.length - 1) + "/login";
	}
	if (role == "") {
		location.href = url;
	}
</script>

<c:if
	test="${user_role=='tl' || user_role=='opm' || user_role=='admin'}">
	<frameset rows="72,*" cols="*" frameborder="no" border="0"
		framespacing="0">
		<frame src="top" name="topFrame" scrolling="no" noresize="noresize"
			id="topFrame" title="topFrame" />
		<frameset cols="218,*" frameborder="no" border="0" framespacing="0">
			<frame src="left" name="leftFrame" scrolling="no" noresize="noresize"
				id="leftFrame" title="leftFrame" />
			<frame src="main" name="centerFrame" scrolling="no"
				noresize="noresize" id="centerFrame" title="centerFrame" />
		</frameset>
	</frameset>
</c:if>


</html>
