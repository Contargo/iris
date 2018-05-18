<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="iris" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page session="false" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">

    <title>
        <decorator:title default="IRIS"/>
    </title>

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />

    <!-- Le styles -->
    <link href="<c:url value="/client/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/client/css/custom.css"/>" rel="stylesheet">
    <link href="<c:url value="/client/css/${ciCssFileName}.css"/>" rel="stylesheet">
    <link href="<c:url value="/client/css/bootstrap-notify.css"/>" rel="stylesheet">
    <link href="<c:url value="/client/css/datepicker.css" />" rel="stylesheet"/>
    <link href="<c:url value="/client/js/lib/select2.css" />" rel="stylesheet"/>
    <link href="<c:url value="/client/js/lib/cookieconsent.min.css" />" rel="stylesheet"/>

    <script src="<c:url value="/client/js/lib/jquery-1.10.2.min.js" />"></script>
    <script src="<c:url value="/client/js/lib/jquery-migrate-1.2.1.min.js" />"></script>
    <script src="<c:url value="/client/js/lib/bootstrap.min.js" />"></script>
    <script src="<c:url value="/client/js/lib/bootstrap-notify.js" />"></script>
    <script src="<c:url value="/client/js/lib/bootstrap-datepicker.js" />"></script>
    <script src="<c:url value="/client/js/lib/select2.js" />"></script>
    <script src="<c:url value="/client/js/logoutHandling.js" />"></script>
    <script src="<c:url value="/client/js/lib/cookieconsent.min.js" />"></script>

    <iris:cookieNotification/>

    <decorator:head/>
</head>
<body>

<div class="header">
    <div class="row-fluid">
        <div class="container-fluid">
            <ul class="nav nav-pills iris-nav-links">
                <li>
                    <a id="nav-index" href="<c:url value="/web/"/>"> <spring:message code="page.navbar.index"/></a>
                </li>
                <sec:authorize access="isAuthenticated()">
                    <li>
                        <a id="nav-trianglerouting" href="<c:url value="/web/triangle/"/>"><spring:message
                                code="page.navbar.trianglerouting"/></a>
                    </li>
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <li class="dropdown">
                            <a id="nav-management" data-toggle="dropdown" class="dropdown-toggle" href="#">
                                Management <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu">
                                <li>
                                    <a id="nav-terminal-management" href="<c:url value="/web/terminals/"/>"><spring:message
                                            code="page.navbar.terminalmanagement"/></a>
                                </li>
                                <li>
                                    <a id="nav-seaport-management" href="<c:url value="/web/seaports/"/>"><spring:message
                                            code="page.navbar.seaportmanagement"/></a>
                                </li>
                                <li>
                                    <a id="nav-connection-management"
                                       href="<c:url value="/web/connections/"/>"><spring:message
                                            code="page.navbar.connectionmanagement"/></a>
                                </li>
                                <li>
                                    <a id="nav-staticaddress-management"
                                       href="<c:url value="/web/staticaddresses/"/>"><spring:message
                                            code="page.navbar.staticaddresses"/></a>
                                </li>
                                <li>
                                    <a id="nav-routerevision-management"
                                       href="<c:url value="/web/routerevisions"/>"><spring:message
                                            code="page.navbar.routerevisions"/></a>
                                </li>
                            </ul>
                        </li>
                    </sec:authorize>
                    <ul class="nav pull-right">
                        <li>
                            <a id="logged-in-text" style="color: black;">Logged in as <sec:authentication
                                    property="principal.username"/></a>
                        </li>
                        <li>
                            <a id="logoutlink" href="<c:url value="/logout"/>">Logout</a>
                        </li>
                    </ul>
                </sec:authorize>
            </ul>
        </div>
    </div>
</div>

<div class="container-fluid">
    <div class="row-fluid">
        <div class="header-title pull-right">
            <span class="header-title-first-letters">IRI</span><span class="header-title-last-letters">S</span>
        </div>
        <div class="header-line"></div>
        <div class="header-page-title pull-left">
            <div class="row-fluid">
                <c:choose>
                    <c:when test="${requestScope['page.title']}">
                        <spring:message var="title" code="${requestScope['page.title']}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="title" value="IRIS"/>
                    </c:otherwise>
                </c:choose>
                <h2 id="page-title">
                    <decorator:title default="IRIS"/>
                </h2>
            </div>
        </div>
    </div>
</div>

<div class="container-fluid">
    <div class="row-fluid" id="page-message-container">
        <c:if test="${message != null}">
            <div id="page-message" class="row-fluid message message-${fn:toLowerCase(message.type)} message-width">
                <spring:message code="${message.message}"/>
            </div>
        </c:if>
    </div>
</div>

<!-- CONTENT -->
<decorator:body/>

<!-- FOOTER -->
<footer class="footer">
    <div class="footer-border"></div>
    <p class="pull-left">
        <spring:message code="page.toTopScrollerToolTip" var="toTopScrollerToolTip"/>
        <a class="to-top-scroller" href="" title="${toTopScrollerToolTip}"></a>
    </p>
</footer>

<script type="text/javascript">
    var helplinkanchor = "top";

    function helpPopup(url, title) {

        var complete = url + helplinkanchor;
        var win = window.open(complete, title, "width=800,height=600,top=50,resizable=yes,scrollbars=yes");
        win.focus();
        return false;
    }

    function adjustHelpLink(anchor) {
        helplinkanchor = anchor;
    }

    $(function () {
        $("a.toTopScroller").click(function (event) {
            event.preventDefault();
            window.scrollTo(0, 0);
        });

    })
</script>
</body>
</html>
