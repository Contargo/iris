function ConnectionValidationMessageService(){
    this.validationMessage = {
        "mainrunconnection.duplicate": "Mainrun connection with given seaport, terminal und route type exists"
    };
    this.defaultMessage = "unspecified error";
}

ConnectionValidationMessageService.prototype.getValidationMessage = function (errorCode, fallbackMessage) {
    var validationMessage = this.validationMessage[errorCode];
    if(validationMessage === undefined) {
        if(!!fallbackMessage){
            return fallbackMessage;
        }
        return this.defaultMessage;
    }
    return  validationMessage;
};
