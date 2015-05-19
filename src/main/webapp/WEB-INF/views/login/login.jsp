<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="iris" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>
        <spring:message code="index.page.title"/>
    </title>
</head>

<body>

<div class="container-fluid">
    <jsp:include page="../iris-explain.jsp"/>
</div>

<div class="container-fluid">

    <div class="row-fluid">
        <div class="span2">
            <h3>
                <spring:message code="login.use"/>
            </h3>
        </div>

        <div class="span10">
            <h4>
                <spring:message code="login.title"/>
            </h4>

            <p>
                <spring:message code="login.description"/>
            </p>

            <form action="/web/login/process" method="post" class="form-inline">
                <fieldset>
                    <div class="control-group">
                        <div class="controls">
                            <input id="email" type="email" name="email" size="20" title="E-Mail"
                                   placeholder="<spring:message code="login.email.label"/>"/>
                            <input id="password" name="password" type="password" size="20" title="Password"
                                   placeholder="<spring:message code="login.password.label"/>"/>
                            <input id="login" class="btn btn-primary " type="submit"
                                   value="<spring:message code="login.signin"/>"/>
                        </div>
                    </div>
                </fieldset>
            </form>

            <iris:oldBrowserWarning/>

            <c:if test="${not empty param.error}">
                <div class="alert error" id="login-error">
                    <h4>
                        <spring:message code="login.error.title"/>
                    </h4>
                    <spring:message code="login.error.text"/>
                </div>
            </c:if>
        </div>
    </div>
</div>

</body>
</html>