<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@environment.getProperty('cookies.link')" var="cookiesLink" />

<script>
    window.addEventListener("load", function () {
        window.cookieconsent.initialise({
            "palette": {
                "popup": {
                    "background": "#00305d"
                },
                "button": {
                    "background": "#B1B2B3"
                }
            },
            "theme": "classic",
            "position": "top",
            "static": true,
            "cookie": {
                "name": "cookieconsent_status_iris"
            },
            "content": {
                "href": "${cookiesLink}"
            }
        })
    });
</script>