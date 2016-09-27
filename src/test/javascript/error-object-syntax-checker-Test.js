describe("ErrorObjectSyntaxChecker test suite", function () {
    'use strict';

    var sut;
    var code;
    var message;
    var validErrorObject;
    var errorObjectWithoutCode;
    var errorObjectWithoutMessage;
    var errorObjectMessageNull;
    var errorObjectCodeNotDefined;
    var errorObjectWithoutMessageAndCodeNull;

    beforeEach(function () {
        sut = new ErrorObjectSyntaxChecker();
        code = "offer.baf";
        message = "offer must not be null";
        validErrorObject = {code: code, message: message};
        errorObjectWithoutCode = {message: message};
        errorObjectWithoutMessage = {code: message};
        errorObjectMessageNull = {code: code, message: null};
        errorObjectCodeNotDefined = {code: undefined, message: message};
        errorObjectWithoutMessageAndCodeNull = {code: null};
    });

    describe("instantiation", function () {
        it("can be instantiated", function () {
            expect(sut).toBeDefined();
        });
    });

    describe("hasAllProperties", function () {
        it("true", function () {
            expect(sut.hasAllProperties(validErrorObject)).toBe(true);
        });

        it("without code", function () {
            expect(sut.hasAllProperties(errorObjectWithoutCode)).toBe(false);
        });

        it("without message", function () {
            expect(sut.hasAllProperties(errorObjectWithoutMessage)).toBe(false);
        });
    });

    describe("allPropertiesDefined", function () {
        it("true", function () {
            expect(sut.allPropertiesDefined(validErrorObject)).toBe(true);
        });

        it("message not defined", function () {
            expect(sut.allPropertiesDefined(errorObjectMessageNull)).toBe(false);
        });

        it("code not defined", function () {
            expect(sut.allPropertiesDefined(errorObjectCodeNotDefined)).toBe(false);
        });
    });

    describe("isValid", function () {
        it("true", function () {
            spyOn(sut, "hasAllProperties").and.returnValue(true);
            spyOn(sut, "allPropertiesDefined").and.returnValue(true);
            expect(sut.isValid(validErrorObject)).toBe(true);
        });

        it("properties not defined", function () {
            spyOn(sut, "hasAllProperties").and.returnValue(true);
            spyOn(sut, "allPropertiesDefined").and.returnValue(false);
            expect(sut.isValid(errorObjectMessageNull)).toBe(false);
        });

        it("not all properties available", function () {
            spyOn(sut, "hasAllProperties").and.returnValue(false);
            spyOn(sut, "allPropertiesDefined").and.returnValue(true);
            expect(sut.isValid(errorObjectWithoutMessage)).toBe(false);
        });

        it("properties not defined and not all properties available", function () {
            spyOn(sut, "hasAllProperties").and.returnValue(false);
            spyOn(sut, "allPropertiesDefined").and.returnValue(false);
            expect(sut.isValid(errorObjectWithoutMessageAndCodeNull)).toBe(false);
        });
    });

    describe("isValidJSONString", function () {
        var jsonErrorObject = '{"code": "offer.baf"}';

        it("true", function () {
            spyOn(sut, "isValid").and.returnValue(true);
            expect(sut.isValidJSONString(jsonErrorObject)).toBe(true);
        });

        it("cannot parse", function () {
            expect(sut.isValidJSONString('"')).toBe(false);
        });

        it("is not valid", function () {
            spyOn(sut, "isValid").and.returnValue(false);
            expect(sut.isValidJSONString(jsonErrorObject)).toBe(false);
        });
    });
});