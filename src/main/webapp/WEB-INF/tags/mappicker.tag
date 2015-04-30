<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ attribute name="latitudeId" type="java.lang.String" required="true"%>
<%@ attribute name="longitudeId" type="java.lang.String" required="true"%>

<link href="<c:url value="/client/js/lib/leaflet.css"/>" rel="stylesheet">
<script src="<c:url value="/client/js/lib/leaflet.js" />"></script>
<script src="<c:url value="/client/js/lib/mappicker.js" />"></script>



<%--link to display modal dialog--%>
<a href="#mappicker" role="button" id="mapPickerButton" class="btn" onclick="showMap('${latitudeId}','${longitudeId}');return false;"><i class="icon-map-marker"></i></a>

<%-- modal dialog --%>
<div class="modal hide" id="mappicker" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><spring:message code="mappicker.closebutton" /></button>
        <h3 id="myModalLabel"><spring:message code="mappicker.chooselocation" /></h3>
    </div>
    <div class="modal-body">
        <div id="map" style="height: 400px;"></div>
    </div>
    <div class="modal-footer">
        <button id="closedialog" class="btn" data-dismiss="modal" aria-hidden="true"><spring:message code="mappicker.close" /></button>
        <button id="selectcoorinates" onclick="selectCoordinates();return false;" class="btn btn-primary"><spring:message code="mappicker.select" /></button>
    </div>
</div>