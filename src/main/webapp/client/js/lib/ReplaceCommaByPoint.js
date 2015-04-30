$(document).ready(function () {

    $('form').submit(function () {
        $('input.comma-replacement-aware').each(function () {
            replaceCommaByPoint($(this));
        });
    });

    $('input.comma-replacement-aware').on('blur', function () {
        replaceCommaByPoint($(this));
    });

    function replaceCommaByPoint(input) {
        input.val(input.val().replace(/,/g, '.'));
    }
});