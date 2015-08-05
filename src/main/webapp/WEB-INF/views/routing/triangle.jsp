<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ page session="false" %>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="triangle.page.title"/></title>
</head>

<body>

<div class="container-fluid">
    <div id="userdata" class="btn-group"></div>
</div>

<div class="container-fluid">
    <div class="row-fluid">
        <div class="span12">
            <div id="appcontent">Application is loading...</div>
            <div class='notifications bottom-left'></div>
            <div id="logindialog"></div>
        </div>
    </div>
</div>

<script type="text/javascript">
    /** install dummy console for headless mode. So IE8 works without JS-console open */
    if (window.console == undefined) {
        window.console = {
            log: function () {
            },
            error: function () {
            }
        };
    }
</script>


<!-- libs -->
<script src="<c:url value="/client/js/lib/underscore.js" />"></script>
<script src="<c:url value="/client/js/lib/backbone.js" />"></script>
<script src="<c:url value="/client/js/lib/handlebars-3.0.3.js" />"></script>
<script src="<c:url value="/client/js/lib/modernizr.custom.00395.js" />"></script>

<!-- model -->
<script src="<c:url value="/client/js/routing/SelectableAwareCollection.js" />"></script>
<script src="<c:url value="/client/js/routing/models/Seaport.js" />"></script>
<script src="<c:url value="/client/js/routing/models/Terminal.js" />"></script>
<script src="<c:url value="/client/js/routing/models/Address.js" />"></script>
<!-- Views -->
<script src="<c:url value="/client/js/routing/views/GeoCodingView.js" />"></script>
<script src="<c:url value="/client/js/routing/views/GeoCodeRequestView.js" />"></script>
<script src="<c:url value="/client/js/routing/views/BootstrapDropDownView.js" />"></script>
<script src="<c:url value="/client/js/routing/views/AddressListView.js" />"></script>
<script src="<c:url value="/client/js/routing/views/TriangleDropDownWithAddButtonView.js" />"></script>
<script src="<c:url value="/client/js/routing/views/TerminalView.js" />"></script>
<script src="<c:url value="/client/js/routing/views/SeaportView.js" />"></script>

<script src="<c:url value="/client/js/routing/Server.js" />"></script>
<script src="<c:url value="/client/js/routing/Helper.js" />"></script>
<script src="<c:url value="/client/js/routing/TemplateManager.js" />"></script>
<script src="<c:url value="/client/js/routing/models/GeoCoding.js" />"></script>
<script src="<c:url value="/client/js/routing/models/GeoCodeRequest.js" />"></script>
<script src="<c:url value="/client/js/routing/models/SearchStatus.js" />"></script>


<script src="<c:url value="/client/js/routing/TriangleRoutingRouter.js" />"></script>
<script src="<c:url value="/client/js/routing/TriangleRoutingApp.js" />"></script>

<script src="<c:url value="/client/js/routing/models/TriangleRoutePart.js" />"></script>
<script src="<c:url value="/client/js/routing/models/TrianglePoints.js" />"></script>
<script src="<c:url value="/client/js/routing/models/TriangleModel.js" />"></script>

<script src="<c:url value="/client/js/routing/views/TriangleRouteTotalView.js" />"></script>
<script src="<c:url value="/client/js/routing/views/TriangleRoutePartView.js" />"></script>
<script src="<c:url value="/client/js/routing/views/TrianglePointsView.js" />"></script>

<script src="<c:url value="/client/js/routing/views/TriangleView.js" />"></script>

<script type="text/javascript">
    $(function () {
        exportTemplateManagerAsGlobalFunction("<c:url value='/client/js/routing/templates/' />");

        var app = new TriangleRoutingApp({contextpath: "<c:url value="/" />"});
        app.start();
    });
</script>

</body>
</html>
