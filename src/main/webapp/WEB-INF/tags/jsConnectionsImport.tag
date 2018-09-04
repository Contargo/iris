<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!-- ################################# LIBS #################################-->
<script src="<c:url value="/client/js/lib/underscore.js" />"></script>
<script src="<c:url value="/client/js/lib/backbone.js" />"></script>
<script src="<c:url value="/client/js/lib/handlebars-3.0.3.js" />"></script>
<script src="<c:url value="/client/js/lib/modernizr.custom.00395.js" />"></script>

<!-- ################################# GENERAL #################################-->
<script src="<c:url value="/client/js/routing/TemplateManager.js" />"></script>
<script src="<c:url value="/client/js/connections/connection-server.js" />"></script>
<script src="<c:url value="/client/js/connections/connection-mapper.js" />"></script>
<script src="<c:url value="/client/js/number-enforcer.js" />"></script>
<script src="<c:url value="/client/js/error-object-syntax-checker.js" />"></script>
<script src="<c:url value="/client/js/connections/connection-validation-message-service.js" />"></script>

<!-- ################################# MODELS #################################-->
<script src="<c:url value="/client/js/connections/models/connection-endpoint.js" />"></script>
<script src="<c:url value="/client/js/connections/models/connection-seaport.js" />"></script>
<script src="<c:url value="/client/js/connections/models/connection-terminal.js" />"></script>
<script src="<c:url value="/client/js/connections/models/routetype.js" />"></script>
<script src="<c:url value="/client/js/connections/models/distances.js" />"></script>
<script src="<c:url value="/client/js/connections/models/connection.js" />"></script>

<!-- ################################# VIEWS #################################-->
<script src="<c:url value="/client/js/connections/views/connection-view.js" />"></script>
<script src="<c:url value="/client/js/connections/views/seaports-view.js" />"></script>
<script src="<c:url value="/client/js/connections/views/terminals-view.js" />"></script>
<script src="<c:url value="/client/js/connections/views/routetypes-view.js" />"></script>
<script src="<c:url value="/client/js/connections/views/distances-view.js" />"></script>
<script src="<c:url value="/client/js/connections/views/message-view.js" />"></script>

<!-- ################################# APPS #################################-->
<script src="<c:url value="/client/js/connections/connection-app.js" />"></script>