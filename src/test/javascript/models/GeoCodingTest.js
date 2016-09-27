
describe("GeoCoding", function () {

    var errorMock = function () {
        alert("foo");
    };
        
    var server = new Server(undefined, errorMock);

    beforeEach(function(){
        this.geoCoding = new GeoCoding({server: server, countries: new SelectableAwareCollection([{}])} );
    });

    it("instanciation fails without server in options", function () {
        expect(
            function() {
                return new GeoCoding({});
            }
        ).toThrow();
    });

    it("can be instantiated with server", function () {
        expect(this.geoCoding).toBeDefined();
    });

    it("has request", function () {
        expect(this.geoCoding.get('request')).toBeDefined();
    });

    it("has result list", function () {
        expect(this.geoCoding.get('results')).toBeDefined();
    });

    it("has result list", function () {
        expect(this.geoCoding.get('results')).toBeDefined();
    });
});



describe("GeoCoding search interaction", function () {

    var server = { "geoCode" : function(request, callback) {}};

    beforeEach(function(){
        this.geoCoding = new GeoCoding({server: server, countries: new SelectableAwareCollection([{}])} );
    });

    it("returns request", function () {
        expect(this.geoCoding.getRequest()).toBeDefined();
    });

    it("returns results", function () {
        expect(this.geoCoding.getResults()).toBeDefined();
    });

    it("executes server-request on changes to its request", function () {
        spyOn(server, "geoCode");

        var request = this.geoCoding.getRequest().set("city", "foo");
        expect(server.geoCode).toHaveBeenCalledWith(request, jasmine.any(Function));
    });

    it("resets adresses on callback", function () {
        var cityname = "Karlsruhe";
        var result = "Some place in Karlsruhe";

        var server = { "geoCode" : function(request, callback) {
            expect(request.get("city")).toEqual(cityname);
            callback( [{'displayName' :result} ]);
        }};

        var g = new GeoCoding({server: server, countries: new SelectableAwareCollection([{}]), searchStatus: new SearchStatus()});

        g.getRequest().set("city", cityname);

        var results = g.getResults();

        expect(results.length).toEqual(1);
        expect(results.at(0).get("displayName")).toEqual(result);
    });
});

describe("GeoCoding address selection", function () {

    var server = { "geoCode" : function(request, callback) {}};
    var g = new GeoCoding({server: server, countries: new SelectableAwareCollection([{}])});

    var a1;
    var a2;
    var a3;
    
    var a4;
    var a5;

    beforeEach(function() {

        a1 = new Address({displayName: "placeA"});
        a2 = new Address({displayName: "placeB"});
        a3 = new Address({displayName: "placeC"});

        a4 = new Address({displayName: "placeD"});
        a5 = new Address({displayName: "placeE"});

        var adressesA = [a1, a2, a3];

        var adressesB = [a4, a5];

        var adressLists =
            [
                {
                    name : "foo",
                    addresses : adressesA
                },
                {
                    name : "bar",
                    addresses : adressesB
                }
            ];
        
        g.getResults().reset(adressLists);
    });

    it("has addresses", function () {
        // get combined address count
        expect(g.getResults().reduce(function(memo, el) { return memo + el.get("addresses").size() }, 0)).toEqual(5);
    });

    it("has no selected one by default", function () {

        expect(g.getSelected()).toBeUndefined();
    });

    it("has selected when setting one", function () {
        var spy = jasmine.createSpy();
        g.bind("selectedchanged", spy);

        a1.set('selected', true);

        expect(spy).toHaveBeenCalledWith(a1);
    });

    it("has new when setting anotherone", function () {
        var spy = jasmine.createSpy();
        g.bind("selectedchanged", spy);

        a1.set('selected', true);
        expect(spy).toHaveBeenCalledWith(a1);
        a3.set('selected', true);
        expect(spy).toHaveBeenCalledWith(a3);
    });

    it("works on double-select", function () {
        var spy = jasmine.createSpy();
        g.bind("selectedchanged", spy);

        a1.set('selected', true);
        a1.set('selected', true);

        expect(spy.calls.count()).toEqual(1);
    });
});

describe("GeoCoding", function () {

    var errorMock = function () {
        alert("foo");
    };

    var serverMock = new Server(undefined, errorMock);

    beforeEach(function(){
        serverMock.addressByGeolocation = function (latitude, longitude, callback) {
            callback([
                {addresses: [{}]}
            ]);
        };
        serverMock.addressByOsmId = function (osmId, callback) {
            callback([
                {addresses: [{}]}
            ]);
        };
        this.geoCoding = new GeoCoding({server: serverMock, countries: new SelectableAwareCollection([{}])} );
        this.geoCoding.set('searchStatus',new SearchStatus());
    });    
});



