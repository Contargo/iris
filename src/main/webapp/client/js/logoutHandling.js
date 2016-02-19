/**
 * This snippets listens for unauthenticated ajax requests and forces the browser to reload the current site to start
 * the normal logout handling with redirecting to the current page.
 */
$(document).ajaxError(function (event, jqxhr) {
    if (jqxhr.status === 401) {
        location.reload();
    }
});
