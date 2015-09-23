<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="property" type="java.lang.String" required="true" %>
<%@ attribute name="dateValue" type="java.util.Date" required="true" %>
<%@ attribute name="messageKey" type="java.lang.String" required="true" %>
<%@ attribute name="inputId" type="java.lang.String" required="true" %>

<div class="control-group" >
    <div>
        <form:label path="${property}" cssClass="control-label" cssErrorClass="control-label error">
            <spring:message code="${messageKey}" var="translated_message_key"/>
            <c:out value="${translated_message_key}"/>
        </form:label>
    </div>

    <fmt:formatDate value="${dateValue}" type="date" pattern="dd.MM.yyyy" var="formattedDateValue"/>

    <div class="controls">

    <div class="input-append date" id="datePickerFor${inputId}" data-date="" data-date-format="dd.mm.yyyy"
         data-date-viewmode="days" data-date-minviewmode="days">

        <div>
        
            <form:input path="${property}" value="${formattedDateValue}" class="input${inputId} form-control"
                        cssErrorClass="error form-control"/>
            <form:errors path="${property}" cssClass="help-inline error"/>
    
            <span class="add-on btn btn-default">
                <span class="icon-calendar" />
            </span>
            
        </div>
        
        
        <script type="text/javascript">
            var element = $('#datePickerFor${inputId}');
            var datepicker = element.datepicker();
            datepicker.on('changeDate', function (ev) {
                $('div.datepicker').hide();
            });
        </script>
    </div>
    </div>

</div>