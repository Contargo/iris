<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="iris" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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

</div>
<div class='notifications bottom-left'></div>
<iris:jsConnectionsImport/>
<script type="text/javascript">
    $(function () {
        exportTemplateManagerAsGlobalFunction("<c:url value='/client/js/connections/templates/' />");
        var connectionServer = new ConnectionServer('<c:url value="/" />');
        var app;
        var id = window.location.pathname.split('/').pop();
        if (id === 'new') {
            app = new ConnectionApp(connectionServer);
        } else {
            app = new ConnectionApp(connectionServer, parseInt(id), !!"${param['success']}");
        }
        app.start();
    });
</script>

</body>
</html>