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
<c:url var="BASE_URL" value="/web/routerevisions"/>
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

                <c:url value="${BASE_URL}/new" var="COMPLETE_CREATE_URL"/>
                <div class="management-button-top">
                    <a href="${COMPLETE_CREATE_URL}" class="btn btn-primary btn-create-top" id="top-new">
                        <i class="icon-plus-sign icon-white"></i>
                        <spring:message code="table.newLabel"/>
                    </a>
                </div>
                <div class="margin-bottom-5">
                    <select id="selectedTerminal">
                        <option></option>
                        <c:forEach items="${terminals}" var="terminal">
                            <c:choose>
                                <c:when test="${terminal.id eq selectedTerminal}">
                                    <option value="${terminal.id}" selected="selected">${terminal.niceName}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${terminal.id}">${terminal.niceName}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                    <a id="filter-reset" href="${BASE_URL}" class="btn btn-primary"><spring:message
                            code="routerevision.table.filter.reset"/></a>
                </div>

                <display:table name="routeRevisions" class="table iris-table" uid="routerevision"
                               requestURI="${BASE_URL}">

                    <spring:message code="routerevision.terminal" var="label"/>
                    <display:column title="${label}" property="terminal.name" sortable="true"/>

                    <spring:message code="routerevision.truckdistanceoneway" var="label"/>
                    <display:column title="${label}" property="truckDistanceOneWayInKilometer" sortable="true"/>

                    <spring:message code="routerevision.tolldistanceoneway" var="label"/>
                    <display:column title="${label}" property="tollDistanceOneWayInKilometer" sortable="true"/>

                    <spring:message code="routerevision.airlinedistance" var="label"/>
                    <display:column title="${label}" property="airlineDistanceInKilometer" sortable="true"/>

                    <spring:message code="routerevision.latitude" var="label"/>
                    <display:column title="${label}" property="latitude" sortable="true"/>

                    <spring:message code="routerevision.longitude" var="label"/>
                    <display:column title="${label}" property="longitude" sortable="true"/>

                    <spring:message code="routerevision.radius" var="label"/>
                    <display:column title="${label}" property="radiusInMeter" sortable="true"/>

                    <spring:message code="routerevision.postalcode" var="label"/>
                    <display:column title="${label}" property="postalCode" sortable="true"/>

                    <spring:message code="routerevision.city" var="label"/>
                    <display:column title="${label}" property="city" sortable="true"/>

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
<script type="text/javascript">
    $(document).ready(function () {
        var $selectedTerminal = $('#selectedTerminal');
        $selectedTerminal.select2({
            width: 'resolve',
            placeholder: 'Select a terminal'
        });
        $selectedTerminal.on('change', function () {
            var terminalId = $selectedTerminal.select2('val');

            var params = "?";
            if (terminalId != 0) {
                params += "terminalId=" + terminalId;
            }
            window.location.replace("${BASE_URL}" + params);
        });
    });
</script>
</body>
</html>