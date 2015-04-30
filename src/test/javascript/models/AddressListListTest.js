describe("AddressListList", function () {

    var list;

    beforeEach(function() {
        list = new AddressListList();

        var a1 = new Address();
        var a2 = new Address();

        var addressesList = new AddressListModel({addresses : [a1, a2]} );
        
        list.add(addressesList);
    });

    it("can be instantiated", function () {
        expect(list).toBeDefined();
    });

    it("takes elements of type AddressListModel", function () {
        expect(list.length).toBe(1);
    });

    it("has an no selected element until one is actively selected", function () {
        expect(list.selectedAddressOfAddressList).toBeUndefined();
    });

    it("sets its selectedAddressOfAddressList property when one of its elements is selected", function () {
        var a1 = list.at(0).get("addresses").at(0);

        a1.set("selected", true);

        expect(list.selectedAddressOfAddressList).toEqual(a1);
    });

    it("changes its selectedAddressOfAddressList property when another element is selected", function () {
        var a1 = list.at(0).get("addresses").at(0);
        var a2 = list.at(0).get("addresses").at(1);

        a1.set("selected", true);
        a2.set("selected", true);

        expect(list.selectedAddressOfAddressList).toEqual(a2);
    });

    it("resets the selected flag of its members when a different member is selected", function() {
        var a1 = list.at(0).get("addresses").at(0);
        var a2 = list.at(0).get("addresses").at(1);

        a1.set("selected", true);
        a2.set("selected", true);

        expect(a1.get("selected")).toBe(false);
    });
});