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
<c:url var="BASE_URL" value="/web/connections/"/>

<c:choose>
    <c:when test="${mainRunConnection.id == null}">
        <c:set var="SUBMIT_BUTTON_MESSAGE_KEY" value="form.save"/>
        <c:set var="HEADER_MESSAGE_KEY" value="mainrunconnection.create.title"/>
        <c:set var="METHOD" value="POST"/>
    </c:when>
    <c:otherwise>
        <c:set var="SUBMIT_BUTTON_MESSAGE_KEY" value="form.update"/>
        <c:set var="HEADER_MESSAGE_KEY" value="mainrunconnection.update.title"/>
        <c:set var="METHOD" value="PUT"/>
    </c:otherwise>
</c:choose>


<div class="container-fluid">

    <div class="row-fluid">
        <a class="back-button-link" href="${BASE_URL}"></a>

        <div class="content-title">
            <h2>
                <spring:message code="${HEADER_MESSAGE_KEY}"/>
                <c:if test="${mainRunConnection.id != null}">
                    <c:out value="${mainRunConnection.seaport.name}"/>
                    to
                    <c:out value="${mainRunConnection.terminal.name}"/>
                </c:if>
            </h2>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12 content">
            <form:form action="${BASE_URL}${mainRunConnection.id}"
                       modelAttribute="mainRunConnection"
                       method="${METHOD}" cssClass="form-horizontal">

                <fieldset>

                    <div class="control-group">
                        <form:label path="seaport.id" cssClass="control-label" cssErrorClass="control-label error">
                            <spring:message code="mainrunconnection.seaport.name"/>
                        </form:label>
                        <div class="controls">

                            <form:select path="seaport.id" cssErrorClass="error">
                                <c:forEach var="seaport" items="${seaports}">
                                    <spring:message code="mainrunconnection.seaport.isenabled.${seaport.enabled}"
                                                    var="isenabled"/>
                                    <form:option value="${seaport.id}" label="${seaport.name} ${isenabled}"/>
                                </c:forEach>
                            </form:select>

                            <form:errors path="seaport.id" cssClass="help-inline error"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <form:label path="terminal.id"
                                    cssClass="control-label"
                                    cssErrorClass="control-label error">
                            <spring:message code="mainrunconnection.terminal.name"/>
                        </form:label>
                        <div class="controls">

                            <form:select path="terminal.id" cssErrorClass="error">
                                <c:forEach var="terminal" items="${terminals}">
                                    <spring:message
                                            code="mainrunconnection.terminal.isenabled.${terminal.enabled}"
                                            var="isenabled"/>
                                    <form:option value="${terminal.id}" label="${terminal.name} ${isenabled}"/>
                                </c:forEach>
                            </form:select>

                            <form:errors path="terminal.id" cssClass="help-inline error"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <form:label path="routeType" cssClass="control-label" cssErrorClass="control-label error">
                            <spring:message code="mainrunconnection.routeType"/>
                        </form:label>
                        <div class="controls">

                            <form:select path="routeType" cssErrorClass="error">
                                <c:forEach var="routeType" items="${routetypes}">
                                    <form:option value="${routeType}">
                                        <spring:message code="${routeType.messageKey}"/>
                                    </form:option>
                                </c:forEach>
                            </form:select>

                            <form:errors path="routeType" cssClass="help-inline error"/>
                        </div>
                    </div>

                    <iris:inputField property="dieselDistance"
                                     cssClass="comma-replacement-aware"
                                     messageKey="mainrunconnection.dieselDistance"/>

                    <iris:inputField property="electricDistance"
                                     cssClass="comma-replacement-aware"
                                     messageKey="mainrunconnection.electricDistance"/>

                    <iris:checkboxFieldNormal property="enabled" messageKey="mainrunconnection.enabled"/>

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
<script type="text/javascript" src="<c:url value='/client/js/lib/ReplaceCommaByPoint.js' />"></script>
</body>
</html>