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
		<table>
			<c:forEach items="${cacheList}" var="person">
				<tr>
					<td><input id="pid${person}" type="text"
						value='${person}' size="60" /></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>