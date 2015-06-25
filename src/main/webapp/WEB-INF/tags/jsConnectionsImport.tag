<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!-- ################################# LIBS #################################-->
<script src="<c:url value="/client/js/lib/underscore.js" />"></script>
<script src="<c:url value="/client/js/lib/backbone.js" />"></script>
<script src="<c:url value="/client/js/lib/handlebars-1.3.0.js" />"></script>
<script src="<c:url value="/client/js/lib/modernizr.custom.00395.js" />"></script>

<!-- ################################# GENERAL #################################-->
<script src="<c:url value="/client/js/routing/TemplateManager.js" />"></script>
<script src="<c:url value="/client/js/connections/connection-server.js" />"></script>

<!-- ################################# MODELS #################################-->
<script src="<c:url value="/client/js/connections/models/connection.js" />"></script>

<!-- ################################# VIEWS #################################-->
<script src="<c:url value="/client/js/connections/views/connection-view.js" />"></script>

<!-- ################################# APPS #################################-->
<script src="<c:url value="/client/js/connections/connection-app.js" />"></script>