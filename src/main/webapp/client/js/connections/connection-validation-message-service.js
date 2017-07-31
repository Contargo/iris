function ConnectionValidationMessageService() {
    'use strict';
    this.validationMessage = {
        'mainrunconnection.duplicate': 'Mainrun connection with given seaport, terminal and route type exists'
    };
    this.defaultMessage = 'unspecified error';
}

ConnectionValidationMessageService.prototype.getValidationMessage = function (errorCode, fallbackMessage) {
    'use strict';
    var validationMessage = this.validationMessage[errorCode];
    if (validationMessage === undefined) {
        if (!!fallbackMessage) {
            return fallbackMessage;
        }
        return this.defaultMessage;
    }
    return validationMessage;
};
