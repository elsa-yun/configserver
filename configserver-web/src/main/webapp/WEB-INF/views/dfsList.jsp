<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>My HTML View</title>
<link href="<c:url value="/resources/form.css" />" rel="stylesheet"
	type="text/css" />
</head>
<body>
	<div class="cacheDB">
		<c:forEach items="${dfsList}" var="s">
			<img alt="" src="${s}">
			<br>
		</c:forEach>
		</table>
	</div>
</body>
</html>