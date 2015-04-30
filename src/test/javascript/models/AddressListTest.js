
describe("AddressList", function () {

    var list;

    beforeEach(function() {
        list = new AddressList();

        var a1 = new Address();
        var a2 = new Address();

        list.add([a1, a2]);
    });

    it("can be instantiated", function () {
        expect(list).toBeDefined();
    });

    it("takes elements of type Address", function () {
        expect(list.length).toBe(2);
    });
});



