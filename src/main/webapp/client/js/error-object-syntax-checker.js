function ErrorObjectSyntaxChecker() {
}

ErrorObjectSyntaxChecker.prototype.isValidJSONString = function (errorJSON) {
    var errorObject;
    try {
        errorObject = JSON.parse(errorJSON);
        return this.isValid(errorObject);
    } catch (e) {
        return false;
    }
};

ErrorObjectSyntaxChecker.prototype.isValid = function (errorObject) {
    return (this.hasAllProperties(errorObject) && this.allPropertiesDefined(errorObject));
};

ErrorObjectSyntaxChecker.prototype.hasAllProperties = function (errorObject) {
    return (errorObject.hasOwnProperty('code') && errorObject.hasOwnProperty("message"));
};

ErrorObjectSyntaxChecker.prototype.allPropertiesDefined = function (errorObject) {
    return ((!!errorObject.code || errorObject.code === '') && (!!errorObject.message || errorObject.message === ''));
};
