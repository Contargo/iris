<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<!DOCTYPE html>
<html>

<head>
    <title>
        <spring:message code="management.page.title"/>
    </title>
</head>

<body>
<div class="container-fluid">

    <div class="row-fluid">
        <div class="content-title">
            <h2>
                <spring:message code="routerevisions.page.title"/>
            </h2>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <div class="content">

                <c:url value="/web/routerevisions/new" var="COMPLETE_CREATE_URL"/>

                <div class="management-button-top">
                    <a href="${COMPLETE_CREATE_URL}" class="btn btn-primary btn-create-top" id="top-new">
                        <i class="icon-plus-sign icon-white"></i>
                        <spring:message code="table.newLabel"/>
                    </a>
                </div>

                <c:url var="completeUrl" value="/web/routerevisions"/>
                <display:table name="routeRevisions" class="table iris-table" uid="routerevision" requestURI="${completeUrl}">

                    <spring:message code="routerevision.terminal" var="label"/>
                    <display:column title="${label}" property="terminal.name" sortable="true"/>

                    <spring:message code="routerevision.truckdistanceoneway" var="label"/>
                    <display:column title="${label}" property="truckDistanceOneWay" sortable="true"/>

                    <spring:message code="routerevision.tolldistanceoneway" var="label"/>
                    <display:column title="${label}" property="tollDistanceOneWay" sortable="true"/>

                    <spring:message code="routerevision.airlinedistance" var="label"/>
                    <display:column title="${label}" property="airlineDistance" sortable="true"/>

                    <spring:message code="routerevision.latitude" var="label"/>
                    <display:column title="${label}" property="latitude" sortable="true"/>

                    <spring:message code="routerevision.longitude" var="label"/>
                    <display:column title="${label}" property="longitude" sortable="true"/>

                    <spring:message code="routerevision.radius" var="label"/>
                    <display:column title="${label}" property="radius" sortable="true"/>

                    <display:column class="table-buttons">

                        <a href="<c:url value="/web/routerevisions/${routerevision.id}" />" class="btn btn-primary">
                            <i class="icon-edit icon-white"></i>
                            <spring:message code="table.editLabel"/>
                        </a>

                    </display:column>
                </display:table>

                <div class="management-button-bottom">
                    <a href="${COMPLETE_CREATE_URL}" class="btn btn-primary btn-create-bottom" id="bottom-new">
                        <i class="icon-plus-sign icon-white"></i>
                        <spring:message code="table.newLabel"/>
                    </a>
                </div>

            </div>
        </div>
    </div>
</div>

</body>
</html>