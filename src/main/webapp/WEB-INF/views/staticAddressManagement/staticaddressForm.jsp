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
<c:url var="BASE_URL" value="/web/staticaddresses/"/>
<c:url var="ACTION_URL" value="/web/staticaddresses/${staticAddress.id}"/>

<c:choose>
    <c:when test="${staticAddress.id == null}">
        <c:set var="SUBMIT_BUTTON_MESSAGE_KEY" value="form.save"/>
        <c:set var="HEADER_MESSAGE_KEY" value="staticaddress.editmode.create"/>
        <c:set var="METHOD" value="POST"/>
    </c:when>
    <c:otherwise>
        <c:set var="SUBMIT_BUTTON_MESSAGE_KEY" value="form.update"/>
        <c:set var="HEADER_MESSAGE_KEY" value="staticaddress.editmode.edit"/>
        <c:set var="METHOD" value="PUT"/>
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
            <form:form action="${ACTION_URL}" modelAttribute="staticAddress" method="${METHOD}"
                       cssClass="form-horizontal"
                       name="staticAddress">
                <fieldset>
                    <iris:inputField property="postalcode" messageKey="staticaddress.label.postalcode"/>
                    <iris:inputField property="city" messageKey="staticaddress.label.city"/>
                    <iris:inputField property="suburb" messageKey="staticaddress.label.suburb"/>
                    <iris:inputField property="country" messageKey="staticaddress.label.country"/>

                    <div class="control-group">
                        <spring:message code="staticaddress.label.lat" var="TRANSLATED_MESSAGE"/>

                        <form:label path="latitude" cssClass="control-label" cssErrorClass="control-label error">
                            <spring:message code="staticaddress.label.lat"/>
                        </form:label>

                        <div class="controls">
                            <form:input path="latitude" size="20" cssErrorClass="error" title="${TRANSLATED_MESSAGE}"/>
                            <form:errors path="latitude" cssClass="help-inline error"/>
                                    
                                    <span class="help-inline">
                                        <iris:mappicker latitudeId="latitude" longitudeId="longitude"/>
                                    </span>
                        </div>
                    </div>

                    <iris:inputField property="longitude" messageKey="staticaddress.label.lon"/>
                    <iris:inputField messageKey="staticaddress.label.hashkey" property="hashKey" readonly="true"/>
                    <form:hidden path="uniqueId"/>

                    <div class="form-actions">
                        <input id="submit" class="btn btn-primary" type="submit" name="submit"
                               value="<spring:message code="${SUBMIT_BUTTON_MESSAGE_KEY}"/>"/>

                        <a id="back" class="btn" href="${BASE_URL}">
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