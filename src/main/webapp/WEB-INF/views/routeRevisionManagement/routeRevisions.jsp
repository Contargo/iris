<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="iris" tagdir="/WEB-INF/tags" %>

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
                <form:form action="${BASE_URL}" modelAttribute="request" method="GET" cssClass="form-horizontal">
                    <fieldset>
                        <div class="control-group">
                            <form:label path="${terminalId}" cssClass="control-label"
                                        cssErrorClass="control-label error">
                                <spring:message code="routerevision.terminal"/>
                            </form:label>
                            <div class="controls">
                                <form:select path="terminalId" cssErrorClass="error">
                                    <option></option>
                                    <c:forEach var="terminal" items="${terminals}">
                                        <form:option value="${terminal.id}" label="${terminal.niceName}"/>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>
                        <iris:inputField property="postalcode" messageKey="routerevision.postalcode"/>
                        <iris:inputField property="city" messageKey="routerevision.city"/>

                        <div class="form-actions">
                            <input id="search" class="btn btn-primary" type="submit"
                                   value="<spring:message code="staticaddress.label.search" />"/>

                            <a id="reset" class="btn btn-primary" onclick="resetForm()">
                                <spring:message code="routerevision.reset"/>
                            </a>

                            <a href="${COMPLETE_CREATE_URL}" id="new" class="btn btn-primary">
                                <i class="icon-plus-sign icon-white"></i>
                                <spring:message code="table.newLabel"/>
                            </a>

                            <a href="${BASE_URL}/cleanup" class="btn btn-primary">
                                <i class="icon-trash icon-white"></i>
                                <spring:message code="routerevision.cleanup"/>
                            </a>
                        </div>
                    </fieldset>
                </form:form>

                <c:if test="${routeRevisions != null}">
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
                </c:if>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        var $selectedTerminal = $('#terminalId');
        $selectedTerminal.select2({
            width: 'resolve',
            placeholder: 'Select a terminal'
        });
    });

    function resetForm() {
        $('#terminalId').select2('val', '');
        $('#postalcode').val('');
        $('#city').val('');
    }
</script>
</body>
</html>