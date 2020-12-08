<%@ include file="common.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>统一配置管理系统</title>
<link href="css/top.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
<script>
	/**
	 $(function(){
	 $("#mainFrame").height=$("#mainFrame").scrollHeight;
	 var a=$("#mainFrame").scrollHeight;
	 console.log(a);
	 });
	 */
	parent.leftFrame.location.reload();
</script>
</head>
<body>
	<div class="guide" id="guide"></div>
	<c:if test="${user_role=='tl'}">
		<iframe frameborder="0" width="100%" height="1500" name="mainFrame"
			id="mainFrame" title="mainFrame" scrolling="auto" src="${wwwDomain}config/tl_list"></iframe>
	</c:if>
	<c:if test="${user_role=='opm'}">
		<iframe frameborder="0" width="100%" height="1500" name="mainFrame"
			id="mainFrame" title="mainFrame" scrolling="auto" src="${wwwDomain}opm/opm_list"></iframe>
	</c:if>
	<c:if test="${user_role=='admin'}">
		<iframe frameborder="0" width="100%" height="1500" name="mainFrame"
			id="mainFrame" title="mainFrame" scrolling="auto" src="${wwwDomain}appName/list"></iframe>
	</c:if>
<!-- 		<iframe frameborder="0" width="100%" height="1500" name="mainFrame"
			id="mainFrame" title="mainFrame" scrolling="auto" src="login"></iframe> -->
</body>
</html>