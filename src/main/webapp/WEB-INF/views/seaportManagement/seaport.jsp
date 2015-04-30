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

<c:url var="BASE_URL" value="/web/seaports/"/>

<c:choose>
    <c:when test="${seaport.id == null}">
        <c:set var="SUBMIT_BUTTON_MESSAGE_KEY" value="form.save"/>
        <c:set var="HEADER_MESSAGE_KEY" value="seaport.create.title"/>
        <c:set var="METHOD" value="POST"/>
    </c:when>
    <c:otherwise>
        <c:set var="SUBMIT_BUTTON_MESSAGE_KEY" value="form.update"/>
        <c:set var="HEADER_MESSAGE_KEY" value="seaport.update.title"/>
        <c:set var="METHOD" value="PUT"/>
    </c:otherwise>
</c:choose>

<div class="container-fluid">
    <div class="row-fluid">

        <a class="back-button-link" href="${BASE_URL}"></a>

        <div class="content-title">
            <h2>
                <spring:message code="${HEADER_MESSAGE_KEY}"/> <c:out value="${seaport.name}"/>
            </h2>
        </div>

    </div>
    <div class="row-fluid">
        <div class="span12 content">
            <form:form action="${BASE_URL}${seaport.id}"
                       modelAttribute="seaport"
                       method="${METHOD}"
                       cssClass="form-horizontal">
                <fieldset>

                        <%-- Seaport Information --%>
                    <h2>
                        <spring:message code="seaport.metaInformation"/>
                    </h2>

                    <iris:inputField property="name" messageKey="seaport.name"/>

                    <div class="control-group">

                        <spring:message code="geolocation.latitude" var="TRANSLATED_MESSAGE"/>
                        <c:set var="latMsg" value="${TRANSLATED_MESSAGE}"/>

                        <form:label path="latitude" cssClass="control-label" cssErrorClass="control-label error">
                            <spring:message code="geolocation.latitude"/>
                        </form:label>

                        <div class="controls">
                            <form:input path="latitude" size="20" cssErrorClass="error" title="${latMsg}"/>
                            <form:errors path="latitude" cssClass="help-inline error"/>
                                    
                                    <span class="help-inline"> 
                                        <iris:mappicker latitudeId="latitude" longitudeId="longitude"/>
                                    </span>

                        </div>
                        <form:hidden path="uniqueId"/>
                    </div>
                    <iris:inputField property="longitude" messageKey="geolocation.longitude"
                                     title="geolocation.longitude.title"/>

                    <iris:checkboxFieldNormal property="enabled" messageKey="seaport.enabled"/>

                    <div class="form-actions">
                        <input id="submit-seaport" class="btn btn-primary " type="submit"
                               value="<spring:message code="${SUBMIT_BUTTON_MESSAGE_KEY}" />"/>
                        <a class="btn" href="${BASE_URL}" id="back-button">
                            <spring:message code="form.backTitle"/>
                        </a>
                    </div>

                </fieldset>
            </form:form>
        </div>
    </div>
</div>

</body>
</html>