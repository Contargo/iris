function numberEnforcer() {

    $('input.number').on('keyup', function () {
        enforceNumbers($(this));
    });

    function enforceNumbers(input) {
        if(/[^0-9\-,\.]/g.test(input.val())){
            input.addClass('error');
            input.next().text('has to be a number');
        } else {
            input.removeClass('error');
            input.next().text('');
        }
    }
}