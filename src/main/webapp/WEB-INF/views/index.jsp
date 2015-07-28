<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>

<html lang="en" xmlns="http://www.w3.org/1999/html">
    <head>
        <title>
            <spring:message code="index.page.title" />
        </title>
    </head>
    <body>
        <div class="container-fluid">
            <jsp:include page="iris-explain.jsp" />
            <jsp:include page="iris-actions.jsp" />
        </div>
    </body>
</html>
