
describe("Address with nothing set", function () {
    var address = new Address();

    it("can be instantiated", function () {
        expect(address).toBeDefined();
    });

    _.each([ "latitude", "longitude", "displayName"], function( e) {
        it("has property " + e, function () {
            expect(address.get(e), "foo").toBeDefined();
        });
    });

});




