<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="iris" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<!DOCTYPE html>
<html>

<head>
    <title>
        <spring:message code="management.page.title"/>
    </title>
</head>

<body>
<c:set var="METHOD" value="GET"/>
<c:url var="BASE_URL" value="/web/staticaddresses/"/>
<c:url var="CREATE_URL" value="/web/staticaddresses/new"/>
<c:url var="IMPORT_URL" value="/web/staticaddresses/import"/>

<div class="container-fluid">
    <div class="row-fluid">
        <div class="content-title">
            <h2>
                <spring:message code="staticaddress.page.title"/>
            </h2>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <div class="content">
                <form:form action="${BASE_URL}" modelAttribute="request" method="${METHOD}" cssClass="form-horizontal">
                    <fieldset>
                        <iris:inputField property="postalcode" messageKey="staticaddress.label.postalcode"/>
                        <iris:inputField property="city" messageKey="staticaddress.label.city"/>

                        <div class="form-actions">
                            <input id="search" class="btn btn-primary" type="submit"
                                   value="<spring:message code="staticaddress.label.search" />"/>

                            <a href="${CREATE_URL}" id="new" class="btn btn-primary">
                                <i class="icon-plus-sign icon-white"></i>
                                <spring:message code="table.newLabel"/>
                            </a>
                            <a href="${IMPORT_URL}" id="import" class="btn btn-primary">
                                <i class="icon-plus-sign icon-white"></i>
                                <spring:message code="staticaddress.label.import"/>
                            </a>
                        </div>
                    </fieldset>
                </form:form>
            </div>
        </div>
    </div>

    <c:if test="${param.postalcode != null && param.city != null}">
        <div class="row-fluid">
            <div class="span12">
                <div class="content">
                    <display:table name="staticAddresses" class="table iris-table" uid="address"
                                   requestURI="${BASE_URL}">

                        <spring:message code="staticaddress.label.postalcode" var="label"/>
                        <display:column title="${label}" property="postalcode" sortable="true"/>

                        <spring:message code="staticaddress.label.city" var="label"/>
                        <display:column title="${label}" property="city" sortable="true"/>

                        <spring:message code="staticaddress.label.suburb" var="label"/>
                        <display:column title="${label}" property="suburb" sortable="true"/>

                        <spring:message code="staticaddress.label.country" var="label"/>
                        <display:column title="${label}" property="country" sortable="true"/>

                        <display:column class="table-buttons">
                            <a href="<c:url value="/web/staticaddresses/${address.id}" />"
                               class="btn btn-primary">
                                <i class="icon-edit icon-white"></i>
                                <spring:message code="table.editLabel"/>
                            </a>
                        </display:column>
                    </display:table>
                </div>
            </div>
        </div>
    </c:if>
</div>
</body>
</html>