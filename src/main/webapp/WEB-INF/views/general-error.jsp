<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid">
    <div id="internal_server_error">
        <spring:message code="iris.internal.error"/>
        <c:if test="${not empty exception_id}">
            <br/><spring:message code="iris.error.report" arguments="${exception_id}"/>
        </c:if>
    </div>

    <div id="internal_server_error_details">
        ${exception}
    </div>
</div>
<br/>