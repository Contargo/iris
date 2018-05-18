<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="iris" tagdir="/WEB-INF/tags" %>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="en">
<head>

    <title>
        <decorator:title default="IRIS"/>
    </title>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />

    <!-- Le styles -->
    <link href="<c:url value="/client/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/client/css/custom.css"/>" rel="stylesheet">
    <link href="<c:url value="/client/css/${ciCssFileName}.css"/>" rel="stylesheet">
    <link href="<c:url value="/client/css/bootstrap-notify.css"/>" rel="stylesheet">
    <link href="<c:url value="/client/js/lib/select2.css" />" rel="stylesheet"/>
    <link href="<c:url value="/client/js/lib/cookieconsent.min.css" />" rel="stylesheet"/>

    <script src="<c:url value="/client/js/lib/jquery-1.10.2.min.js" />"></script>
    <script src="<c:url value="/client/js/lib/jquery-migrate-1.2.1.min.js" />"></script>
    <script src="<c:url value="/client/js/lib/bootstrap.min.js" />"></script>
    <script src="<c:url value="/client/js/lib/bootstrap-notify.js" />"></script>
    <script src="<c:url value="/client/js/lib/select2.js" />"></script>
    <script src="<c:url value="/client/js/lib/cookieconsent.min.js" />"></script>

    <iris:cookieNotification/>

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <decorator:head/>

</head>
<body>
<div class="container-fluid">
    <div class="row-fluid" style="text-align: center; margin-top: 10px; margin-bottom: 10px;">
        <img src="<c:url value="/template/style/pictures/logo/start.png" />" alt="logo"/>
    </div>
</div>

<!-- CONTENT -->
<decorator:body/>

</body>
</html>
