<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ attribute name="property" type="java.lang.String" required="true"%>
<%@ attribute name="messageKey" type="java.lang.String" required="true"%>


<spring:message code="form.textarea.characters.remaining" var="CHARACTERS_REMAINING" />

<div class="control-group">
	<form:label path="${property}" cssClass="control-label" cssErrorClass="control-label error">
		<spring:message code="${messageKey}" var="TRANSLATED_MESSAGE" />
		<c:out value="${TRANSLATED_MESSAGE}" />
	</form:label>
	<div class="controls">
        
        <form:textarea path="${property}" rows="10"  />
        <form:errors path="${property}" cssClass="help-inline error" />
        <div id="commentFeedback"></div>
	</div>
</div>


<script type="text/javascript">
    
    $(document).ready(function() {
        
        var text_max = 5000;
        
        $('#comment').keyup(function() {
            var text_length = $('#comment').val().length;
            var text_remaining = text_max - text_length;
            
            $('#commentFeedback').html(text_remaining + ' ${CHARACTERS_REMAINING}');
            
            if (text_remaining <= 0) {
                $('#commentFeedback').css('color', 'red');
            } else {
                $('#commentFeedback').css('color', 'black');
            }
            
        });
        
    });
    
</script>