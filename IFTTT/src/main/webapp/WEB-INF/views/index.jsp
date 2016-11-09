<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<h2>Hello World!</h2>
<a href="http://localhost:8080/IFTTT/prova">prova</a>
<c:if test="${request.getParameter(\"ris\")=='true'}">
	<p>Success!</p>
</c:if>
<c:if test="${request.getParameter(\"ris\")=='false'}">
	<p>Request failed...</p>
</c:if>
</body>
</html>