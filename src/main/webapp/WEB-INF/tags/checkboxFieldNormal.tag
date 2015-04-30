<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ attribute name="property" type="java.lang.String" required="true"%>
<%@ attribute name="messageKey" type="java.lang.String" required="true"%>
<%@ attribute name="size" type="java.lang.String" required="false"%>

<div class="control-group">
	<form:label path="${property}" cssClass="control-label" cssErrorClass="control-label error">
		<spring:message code="${messageKey}" />
	</form:label>
	<div class="controls">
		<div class="checkbox inline">
			<form:checkbox path="${property}" />
		</div>
	</div>
</div>