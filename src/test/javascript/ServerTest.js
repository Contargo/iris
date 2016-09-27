describe("Server", function () {
    var errorMock = function () {
        alert("foo");
    };
        
    var server = new Server(undefined, errorMock);

    it("can be instantiated", function () {
        expect(server).not.toBeUndefined();
    });

    _.each([ "generateDetailsUrl", "getRouteDetails", "geoCode", "countries",
        "getActiveSeaports", "terminals"], function(e) {

        it("has function " + e, function () {
            expect(server[e]).toBeDefined();
        });
    });
});

describe("Servers geoCode", function () {
    var errorMock = function () {
        alert("foo");
    };
        
    var server = new Server(undefined, errorMock);

    it("fails if no request comes in", function () {
        expect(
            function() {
                server.geoCode();
            }
        ).toThrow();
    });

    it("fails if strange request comes in", function () {
        expect(
            function() {
                server.geoCode({});
            }
        ).toThrow();
    });

    it("does the request", function () {
        spyOn($, 'ajax');
        var r = new GeoCodeRequest({countries: this.countries = new SelectableAwareCollection([{}]), city : "Karlsruhe"});
        server.geoCode(r);
        expect($.ajax).toHaveBeenCalled();
    });

    it("alerts if invalid structure is coming back from server", function () {
        var result = {"foo" : {bar : "hhh"}};

        var callback = jasmine.createSpy('callbackspy');
        var ajaxMock = function (x) {
            var callback = x.success;
            callback(result);
        };

        spyOn($, 'ajax').and.callFake(ajaxMock);
        spyOn(window, 'alert');

        var r = new GeoCodeRequest({countries: new SelectableAwareCollection([{}]), city : "Karlsruhe"});
        server.geoCode(r, callback);
        expect(alert).toHaveBeenCalled();
    });

    it("executes the callback", function () {
        var addresses = [{displayName: "foo"}];

        var result = {"geoCodeResponse" : {addresses : addresses}};
        var callback = jasmine.createSpy('callbackspy');
        var ajaxMock = function (x) {
            var callback = x.success;
            callback(result);
        };

        spyOn($, 'ajax').and.callFake(ajaxMock);
        var r = new GeoCodeRequest({countries: new SelectableAwareCollection([{}]), city : "Karlsruhe"});
        server.geoCode(r, callback);
        expect(callback).toHaveBeenCalledWith(addresses);
    });

});

describe("Servers get active seaports", function () {
    var errorMock = function () {
        alert("foo");
    };

    var server = new Server(undefined, errorMock);

    it("does the request", function () {
        spyOn($, 'ajax');
        server.getActiveSeaports();
        expect($.ajax).toHaveBeenCalled();
    });

    it("alerts if invalid structure is coming back from server", function () {
        var result = {"foo" : {bar : "hhh"}};

        var callback = jasmine.createSpy('callbackspy');
        var ajaxMock = function (x) {
            var callback = x.success;
            callback(result);
        };

        spyOn($, 'ajax').and.callFake(ajaxMock);
        spyOn(window, 'alert');

        server.getActiveSeaports(callback);

        expect(alert).toHaveBeenCalled();
    });

    it("executes the callback", function () {

        var ports = [{name : "Rot"}];

        var result = {seaports : ports};
        var callback = jasmine.createSpy('callbackspy');
        var ajaxMock = function (x) {
            var callback = x.success;
            callback(result);
        };
        spyOn($, 'ajax').and.callFake(ajaxMock);

        server.getActiveSeaports(callback);
        expect(callback).toHaveBeenCalledWith(ports);
    });
});

describe("Servers terminals", function () {
    var errorMock = function () {
        alert("foo");
    };

    var server = new Server(undefined, errorMock);

    it("does the request", function () {
        spyOn($, 'ajax');
        server.terminals();
        expect($.ajax).toHaveBeenCalled();
    });

    it("alerts if invalid structure is coming back from server", function () {
        var result = {"foo" : {bar : "hhh"}};

        var callback = jasmine.createSpy('callbackspy');
        var ajaxMock = function (x) {
            var callback = x.success;
            callback(result);
        };

        spyOn($, 'ajax').and.callFake(ajaxMock);
        spyOn(window, 'alert');

        server.terminals(callback);

        expect(alert).toHaveBeenCalled();
    });

    it("executes the callback", function () {
        var terminals = [{name : "DIT"}];

        var result = {"response" : {terminals : terminals}};
        var callback = jasmine.createSpy('callbackspy');
        var ajaxMock = function (x) {
            var callback = x.success;
            callback(result);
        };
        spyOn($, 'ajax').and.callFake(ajaxMock);

        server.terminals(callback);
        expect(callback).toHaveBeenCalledWith(terminals);
    });
});



describe("Servers seaportConnections", function () {
    var errorMock = function () {
        alert("foo");
    };
        
    var server = new Server(undefined, errorMock);

    it("fails if no request comes in", function () {
        expect(
            function() {
                server.seaportConnections();
            }
        ).toThrow();
    });

    it("alerts if invalid structure is coming back from server", function () {
        var result = {"foo" : {bar : "hhh"}};

        var callback = jasmine.createSpy('callbackspy');
        var ajaxMock = function (x) {
            var callback = x.success;
            callback(result);
        };

        spyOn($, 'ajax').and.callFake(ajaxMock);
        spyOn(window, 'alert');

        var r = new GeoCodeRequest({countries: new SelectableAwareCollection([{}]), city : "Karlsruhe"});

        server.geoCode(r, callback);
        expect(alert).toHaveBeenCalled();
    });

    it("executes the callback", function () {
        var addresses = [{displayName: "foo"}];

        var result = {"geoCodeResponse" : {addresses : addresses}};
        var callback = jasmine.createSpy('callbackspy');
        var ajaxMock = function (x) {
            var callback = x.success;
            callback(result);
        };

        spyOn($, 'ajax').and.callFake(ajaxMock);
        var r = new GeoCodeRequest({countries: new SelectableAwareCollection([{}]), city : "Karlsruhe"});
        server.geoCode(r, callback);
        expect(callback).toHaveBeenCalledWith(addresses);
    });
});