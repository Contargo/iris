<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="row-fluid">
    <div class="span2">
        <h3>
            <spring:message code="index.page.clients"/>
        </h3>
    </div>
    <div class="span10">
        <div class="row-fluid">
            <div class="span10">
                <p>
                    <spring:message code="index.page.clients.description"/>
                </p>
            </div>
        </div>

        <div class="row-fluid">
            <div class="span2">
                <a href="<c:url value="/web/triangle/" />" class="btn btn-large">
                    <spring:message code="index.page.triangle"/>
                </a>
            </div>
            <div class="span8">
                <h4>
                    <spring:message code="index.page.triangle"/>
                </h4>

                <p>
                    <spring:message code="index.page.triangle.description"/>
                </p>
            </div>
        </div>

        <div class="row-fluid">
            <div class="span2">
                <a href="<c:url value="/api/" />" class="btn btn-large">
                    <spring:message code="index.page.search.api"/>
                </a>
            </div>

            <div class="span8">
                <h4>
                    <spring:message code="index.page.restful"/>
                </h4>

                <p>
                    <spring:message code="index.page.restful.description"/>
                </p>
            </div>
        </div>

        <div class="row-fluid">
            <div class="span2">
                <a href="<c:url value="/api/docs.html" />" class="btn btn-large">
                    <spring:message code="index.page.docs.api"/>
                </a>
            </div>
        </div>
    </div>
</div>