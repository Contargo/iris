<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ attribute name="property" type="java.lang.String" required="true"%>
<%@ attribute name="messageKey" type="java.lang.String" required="true"%>

<%@ attribute name="size" type="java.lang.String" required="false"%>
<%@ attribute name="maxlength" type="java.lang.String" required="false"%>
<%@ attribute name="title" type="java.lang.String" required="false"%>
<%@ attribute name="readonly" type="java.lang.Boolean" required="false"%>
<%@ attribute name="unitMessageKey" type="java.lang.String" required="false"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false"%>
<%@ attribute name="onkeypress" type="java.lang.String" required="false"%>

<div class="control-group">
	<form:label path="${property}" cssClass="control-label" cssErrorClass="control-label error">
		<spring:message code="${messageKey}" var="TRANSLATED_MESSAGE" />
		<c:out value="${TRANSLATED_MESSAGE}" />
	</form:label>
	<div class="controls">
		<c:choose>
			<c:when test="${title}">
                <form:input cssClass="${cssClass}" path="${property}" size="${size}" cssErrorClass="error"
                            title="${title}" readonly="${readonly}" maxlength="${maxlength}"
                            onkeypress="${onkeypress}"/>
                <c:if test="${!empty unitMessageKey}">
                    <span id="inputFieldUnit">${unitMessageKey}</span>
                </c:if>
				<form:errors path="${property}" cssClass="help-inline error" />
			</c:when>
			<c:otherwise>
                <form:input cssClass="${cssClass}" path="${property}" size="${size}" cssErrorClass="error"
                            title="${TRANSLATED_MESSAGE}" readonly="${readonly}" maxlength="${maxlength}"
                            onkeypress="${onkeypress}"/>
                <c:if test="${!empty unitMessageKey}">
                    <span id="inputFieldUnit"><spring:message code="${unitMessageKey}"/></span>
                </c:if>
				<form:errors path="${property}" cssClass="help-inline error" />
			</c:otherwise>
		</c:choose>
	</div>
</div>