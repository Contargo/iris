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
                <spring:message code="mainrunconnection.page.title"/>
            </h2>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <div class="content">

                <c:url value="/web/connections/new" var="COMPLETE_CREATE_URL"/>

                <div class="management-button-top">
                    <a href="${COMPLETE_CREATE_URL}" class="btn btn-primary btn-create-top" id="top-new">
                        <i class="icon-plus-sign icon-white"></i>
                        <spring:message code="table.newLabel"/>
                    </a>
                </div>

                <c:url var="BASE_URL" value="/web/connections/"/>
                <display:table name="mainRunConnections"
                               class="table iris-table"
                               uid="routingconnectiondetail"
                               requestURI="${BASE_URL}">

                    <spring:message code="mainrunconnection.seaport.name" var="label"/>
                    <display:column title="${label}" property="seaport.name" sortable="true"/>
                    <spring:message code="mainrunconnection.seaport.enabled" var="label"/>
                    <display:column title="${label}" property="seaport.enabled" sortable="true"/>

                    <spring:message code="mainrunconnection.terminal.name" var="label"/>
                    <display:column title="${label}" property="terminal.name" sortable="true"/>
                    <spring:message code="mainrunconnection.terminal.enabled" var="label"/>
                    <display:column title="${label}" property="terminal.enabled" sortable="true"/>

                    <spring:message code="mainrunconnection.routeType" var="label"/>
                    <display:column title="${label}" property="routeType" sortable="true"/>

                    <spring:message code="mainrunconnection.enabled" var="label"/>
                    <display:column title="${label}" property="enabled" sortable="true"/>

                    <spring:message code="mainrunconnection.everythingenabled" var="label"/>
                    <display:column title="${label}" property="everythingEnabled" sortable="true"/>

                    <display:column class="table-buttons">

                        <c:url var="detailUrl" value="/web/connections/${routingconnectiondetail.id}"/>

                        <a href="${detailUrl}" class="btn btn-primary">
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