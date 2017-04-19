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
<c:url var="IMPORT_URL" value="/web/staticaddresses/import"/>

<div class="container-fluid">
    <div class="row-fluid">
        <div class="content-title">
            <h2>
                <spring:message code="staticaddress.page.import.title"/>
            </h2>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <div class="content">
                <form method="POST" enctype="multipart/form-data" class="form-horizontal" action="${IMPORT_URL}">
                    <fieldset>
                        <div class="control-group">
                            <label for="csv-file" class="control-label">
                                <spring:message code="staticaddress.label.import.file"/>
                            </label>
                            <div class="controls">
                                <input id="csv-file" name="file" type="file" accept=".csv">
                            </div>
                        </div>
                        <div class="control-group">
                            <label for="email" class="control-label">
                                <spring:message code="staticaddress.label.import.mail"/>
                            </label>
                            <div class="controls">
                                <input id="email" name="email" type="text">
                            </div>
                        </div>
                        <div class="form-actions">
                            <input id="search" class="btn btn-primary" type="submit"
                                   value="<spring:message code="staticaddress.label.upload" />"/>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>