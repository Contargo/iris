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

<c:url var="BASE_URL" value="/web/routerevisions"/>
<c:set var="ACTION_URL" value="${BASE_URL}/cleanup"/>

<div class="container-fluid">
    <div class="row-fluid">

        <a class="back-button-link" href="${BASE_URL}"></a>

        <div class="content-title">
            <h2>
                <spring:message code="routerevisions.page.title"/>
            </h2>
        </div>

    </div>
    <div class="row-fluid">
        <div class="span12 content">
            <form:form method="POST" modelAttribute="cleanupRequest" class="form-horizontal" action="${ACTION_URL}">
                <fieldset>
                    <iris:inputField property="email" messageKey="routerevision.cleanup.mail"/>
                    <div class="form-actions">
                        <input class="btn btn-primary" type="submit"
                               value="<spring:message code="routerevision.cleanup" />"/>
                    </div>
                </fieldset>
            </form:form>
        </div>
    </div>
</div>

</body>
</html>