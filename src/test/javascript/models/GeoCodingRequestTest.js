
describe("GeoCodeRequest", function () {
    var request = new GeoCodeRequest({city: "cityname", countries: new SelectableAwareCollection([{}])});
    
    it("with nothing set is empty", function () {
        var r = new GeoCodeRequest({countries: new SelectableAwareCollection([{}])});
        expect(r.isEmpty()).toBeTruthy();
    });

    it("can be instantiated", function () {
        expect(request).not.toBeUndefined();
    });

    _.each(["street", "postalcode", "city", "country"], function( e) {
        it("has property " + e, function () {
            expect(request.get(e), "foo").toBeDefined();
        });
    });

    it("has getQueryStringMethod", function () {
        expect(request.getQueryString).toBeDefined();
    });

    it("querystring contains city parameter ", function () {
        expect(request.getQueryString()).toContain("city=cityname");
    });

    it("querystring does not contain street parameter", function () {
        expect(request.getQueryString()).not.toContain("street");
    });
    
    it("has the default country set as selected if nothing else selected", function() {

        var countries = new SelectableAwareCollection([{
            name : "Foo",
            value : "FOO"
        }, {
            name : "Bar",
            value : "BAR",
            defaultValue : true
        } ]);

        var req = new GeoCodeRequest({countries: countries});
        
        var selectedValue = req.get('countries').getSelected().get("value");
        
        expect(selectedValue).toBe("BAR");
        
    });

});

describe("GeoCodeRequests with mores than city set", function () {

    var request = new GeoCodeRequest({city: "cityname", countries:new SelectableAwareCollection([{}])});
    request.set("street", "xxx");

    it("querystring contains city parameter ", function () {
        expect(request.getQueryString()).toContain("city=cityname");
    });
    it("querystring contains street parameter ", function () {
        expect(request.getQueryString()).toContain("street=xxx");
    });
    it("querystring contains concatinations", function () {
        expect(request.getQueryString()).toContain("&");
    });
    it("is empty is false", function () {
        expect(request.isEmpty()).toBeFalsy();
    });



});


describe("GeoCodeRequests with special characters", function () {
    var request = new GeoCodeRequest({city: "Foo Bar", countries: new SelectableAwareCollection([{}])});

    it("querystring is encoded", function () {
        expect(request.getQueryString()).toContain("city=Foo%20Bar");
    });




});




