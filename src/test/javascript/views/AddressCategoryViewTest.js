describe("AddressCategoryView instanciation", function () {

    var model = new AddressListList([], {server: {}});
    var searchStatus = new SearchStatus();

    it("without model fails", function () {

        expect(
            function () {
                new AddressCategoryView({});
            }
        ).toThrow();
    });

    it("works all needed parameters", function () {
        var view = new AddressCategoryView({
            model: model,
            searchStatus: searchStatus
        });
        expect(view).toBeDefined();
    });
});


describe("AddressCategoryView rendering", function () {

    var model = new AddressListList([], {server: {}});
    var searchStatus = new SearchStatus();

    beforeEach(function () {

        this.view = new AddressCategoryView({
            model: model,
            searchStatus: searchStatus
        });

    });

    it("shows no message on initial load", function () {
        this.view.render();
        expect(this.view.$el.html()).toBe('');
    });

    it("shows 'no results found' message on search with no results", function () {
        searchStatus.setHaveSearched(true);
        this.view.render();
        expect(this.view.$el.html()).toContain('');
    });

    it("shows '' message on no search with results (maybe not in real life :-))", function () {
        searchStatus.setHaveSearched(false);
        model.add(new AddressListModel({addresses: [new Address()]}));

        this.view.render();
        expect(this.view.$el.html()).toBe('');
    });

    it("renders itself to target on load", function () {
        searchStatus.setHaveSearched(true);
        this.view.render();
        expect(this.view.$el).not.toBeEmpty();
    });

    it("re-renders itself to target on reset", function () {
        this.view.render();
        this.view.$el.empty();
        model.reset();

        expect(this.view.$el).not.toBeEmpty();
    });

    it("re-renders itself to target on add and remove", function () {
        this.view.render();

        var address = new Address();
        var addressesList = new AddressListModel({addresses: [address]});

        this.view.$el.empty();
        searchStatus.setHaveSearched(true);
        model.add(addressesList);
        expect(this.view.$el.html()).toContain('geocoding-results');

        this.view.$el.empty();
        model.remove(addressesList);
        expect(this.view.$el).not.toBeEmpty();
    });

    it("renders child-content", function () {
        this.view.render();

        //this relates on the template having an element with class address_table
        var address = new Address({displayName: "foo"});
        var addressesList = new AddressListModel({addresses: [address]});

        this.view.$el.empty();
        model.add(addressesList);
        expect(this.view.$el.html()).toContain('foo');

    });

});


describe("AddressListView instanciation", function () {

    var model = new AddressListModel({});

    it("without model fails", function () {

        expect(
            function () {
                new AddressListView({});
            }
        ).toThrow();
    });


    it("works all needed parameters", function () {
        var view = new AddressListView({model: model});
        expect(view).toBeDefined();
    });


});

describe("AddressListView rendering", function () {

    var model = new AddressListModel({});

    beforeEach(function () {

        this.view = new AddressListView({model: model});
    });

    it("renders itself to target on load", function () {
        expect(this.view.$el).not.toBeEmpty();
    });

    it("re-renders itself to target on reset", function () {
        this.view.$el.empty();
        model.get("addresses").reset();

        expect(this.view.$el).not.toBeEmpty();
    });

    it("re-renders itself to target on add and remove", function () {

        var address = new Address();

        this.view.$el.empty();
        model.get("addresses").add(address);
        expect(this.view.$el.html()).toContain('iris-table');

        this.view.$el.empty();
        model.get("addresses").remove(address);
        expect(this.view.$el).not.toBeEmpty();
    });

    it("renders child-content", function () {

        var address = new Address({displayName: "foo"});

        this.view.$el.empty();
        model.get("addresses").add(address);
        expect(this.view.$el.html()).toContain('foo');

    });
});


describe("AddressView", function () {


    var model = new Address({displayName: "somewhere"});


    it("cannot be instantiated without options", function () {
        expect(
            function () {
                return new AddressView();
            }
        ).toThrow();
    });

    it("cannot be instantiated without template in options", function () {
        expect(
            function () {
                return new AddressView({});
            }
        ).toThrow();
    });

    it("cannot be instantiated with non-existing template in options", function () {

        expect(
            function () {
                new AddressView({template: "#doesnotexist"});
            }
        ).toThrow();
    });


    it("cannot be instantiated without model in options", function () {
        expect(
            function () {
                new AddressView({template: "#template"});
            }
        ).toThrow();

    });

    it("can be instantiated with template and model in options", function () {


        var view = new AddressView({template: "#template", model: model});
        expect(view).toBeDefined();
    });

    it("renders", function () {
        var view = new AddressView({template: "#itemtemplate", model: model});
        view.render();
        expect(view.$el.html()).toContain("somewhere");
    });


});





