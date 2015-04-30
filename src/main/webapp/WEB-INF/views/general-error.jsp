<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.io.PrintWriter" %>

<div class="container-fluid">
    <div id="internal_server_error">
        <spring:message code="iris.internal.error"/>
        <c:if test="${not empty exception_id}">
            <br/><spring:message code="iris.error.report" arguments="${exception_id}"/>
        </c:if>
    </div>

    <div id="internal_server_error_details">
        <h3>${exception}</h3>

        <p>
            <pre style="font-size:9px;">
            <%
                Exception e = (Exception) pageContext.findAttribute("exception");
                if (e != null) {
                    PrintWriter pw = new PrintWriter(out);
                    e.printStackTrace(pw);
                    pw.flush();
                } else {
                    pageContext.getOut().write("No Exception");
                }
            %>
            </pre>
        </p>
    </div>
</div>
<br/>