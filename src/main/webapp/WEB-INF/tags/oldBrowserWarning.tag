<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:message code="browser.too.old.warningTitle" var="title" />
<spring:message code="browser.too.old.warning" var="message" />

<span id="browserWarning" class="popup unsupported-browser" title="${title}" data-content="${message}">
    <i class="icon-info-sign icon-red"></i> <spring:message code="browser.too.old.popup.warning"/>
</span>

<script type="text/javascript">
    $(".popup").popover();

	var $element = $("#browserWarning").hide();
	if ($.browser.msie && parseInt($.browser.version, 10) <= 7) {
		$element.show();
	}
</script>
