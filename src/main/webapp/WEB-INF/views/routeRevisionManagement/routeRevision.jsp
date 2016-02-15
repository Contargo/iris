<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="iris" tagdir="/WEB-INF/tags" %>

<%@ page session="false" %>

<!DOCTYPE html>
<html>
<head>
    <title>
        <spring:message code="management.page.title"/>
    </title>
</head>

<body>

<c:url var="BASE_URL" value="/web/routerevisions"/>

<c:choose>
    <c:when test="${routeRevision.id == null}">
        <c:set var="SUBMIT_BUTTON_MESSAGE_KEY" value="form.save"/>
        <c:set var="HEADER_MESSAGE_KEY" value="routerevision.create.title"/>
        <c:set var="METHOD" value="POST"/>
        <c:set var="ACTION_URL" value="${BASE_URL}"/>
    </c:when>
    <c:otherwise>
        <c:set var="SUBMIT_BUTTON_MESSAGE_KEY" value="form.update"/>
        <c:set var="HEADER_MESSAGE_KEY" value="routerevision.update.title"/>
        <c:set var="METHOD" value="PUT"/>
        <c:set var="ACTION_URL" value="${BASE_URL}/${routeRevision.id}"/>
    </c:otherwise>
</c:choose>

<div class="container-fluid">
    <div class="row-fluid">

        <a class="back-button-link" href="${BASE_URL}"></a>

        <div class="content-title">
            <h2>
                <spring:message code="${HEADER_MESSAGE_KEY}"/>
            </h2>
        </div>

    </div>
    <div class="row-fluid">
        <div class="span12 content">
            <form:form action="${ACTION_URL}"
                       modelAttribute="routeRevision"
                       method="${METHOD}"
                       cssClass="form-horizontal">
                <fieldset>

                    <div class="control-group">
                        <form:label path="terminal.uniqueId"
                                    cssClass="control-label"
                                    cssErrorClass="control-label error">
                            <spring:message code="routerevision.terminal.name"/>
                        </form:label>

                        <div class="controls">
                            <form:select path="terminal.uniqueId" cssErrorClass="error">
                                <c:forEach var="terminal" items="${terminals}">
                                    <spring:message
                                            code="mainrunconnection.terminal.isenabled.${terminal.enabled}"
                                            var="isenabled"/>
                                    <form:option value="${terminal.uniqueId}" label="${terminal.name}${isenabled}"/>
                                </c:forEach>
                            </form:select>
                            <form:errors path="terminal.uniqueId" cssClass="help-inline error"/>
                        </div>
                    </div>

                    <iris:datePicker property="validFrom" dateValue="${routeRevision.validFrom}"
                                     messageKey="routerevision.validFrom" inputId="validFrom"/>

                    <iris:datePicker property="validTo" dateValue="${routeRevision.validTo}"
                                     messageKey="routerevision.validTo" inputId="validTo"/>

                    <div class="control-group">

                        <spring:message code="routerevision.latitude" var="TRANSLATED_MESSAGE"/>
                        <c:set var="latMsg" value="${TRANSLATED_MESSAGE}"/>

                        <form:label path="latitude" cssClass="control-label" cssErrorClass="control-label error">
                            <spring:message code="routerevision.latitude"/>
                        </form:label>

                        <div class="controls">
                            <form:input path="latitude" size="20" cssErrorClass="error" title="${latMsg}"/>
                            <form:errors path="latitude" cssClass="help-inline error"/>
                                    <span>
                                        <iris:mappicker latitudeId="latitude" longitudeId="longitude"/>
                                    </span>
                        </div>
                    </div>

                    <iris:inputField property="longitude" messageKey="geolocation.longitude"
                                     title="geolocation.longitude.title"/>
                    
                    <iris:inputField property="radiusInMeter"
                                     messageKey="routerevision.radius" unitMessageKey="unit.meter"/>

                    <iris:inputField property="truckDistanceOneWayInKilometer"
                                     messageKey="routerevision.truckdistanceoneway" unitMessageKey="unit.kilometer"/>

                    <iris:inputField property="tollDistanceOneWayInKilometer"
                                     messageKey="routerevision.tolldistanceoneway" unitMessageKey="unit.kilometer"/>

                    <iris:inputField property="airlineDistanceInKilometer"
                                     messageKey="routerevision.airlinedistance" unitMessageKey="unit.kilometer"/>

                    <iris:inputField property="city" messageKey="routerevision.city" readonly="true"/>
                    <iris:inputField property="postalCode" messageKey="routerevision.postalcode" readonly="true"/>
                    <iris:inputField property="country" messageKey="routerevision.country" readonly="true"/>

                    <iris:textArea property="comment" messageKey="routerevision.comment" maxlength="5000"/>

                    <div class="form-actions">
                        <input id="submit-button" class="btn btn-primary" type="submit"
                               value="<spring:message code="${SUBMIT_BUTTON_MESSAGE_KEY}" />"/>
                        <a id="back-button" class="btn" href="${BASE_URL}"><spring:message code="form.backTitle"/></a>
                    </div>

                </fieldset>
            </form:form>
        </div>
    </div>
</div>

</body>
</html>