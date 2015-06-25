<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="iris" tagdir="/WEB-INF/tags" %>

<%@ page session="false" %>

<!DOCTYPE html>
<html>

<head>
    <title>
        <spring:message code="management.page.title"/>
    </title>
</head>

<body>
<div id="connection">

    <script type="text/javascript">
        $(function () {
            exportTemplateManagerAsGlobalFunction("<c:url value='/client/js/connections/templates/' />");
            var connectionServer = new ConnectionServer('<c:url value="/" />');
            var app = new ConnectionApp(connectionServer, ${mainRunConnection.id});
            app.start();
        });
    </script>
</div>
<iris:jsConnectionsImport/>
</body>
</html>