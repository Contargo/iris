describe("GeoCodeRequestView instanciation", function() {

    var searchStatus = new SearchStatus();
    
	beforeEach(function() {
		this.geoCodeRequest = new GeoCodeRequest({countries: new SelectableAwareCollection([{}])});
        
		this.view = new GeoCodeRequestView({
			model : this.geoCodeRequest,
            searchStatus: searchStatus
		});
	});

	it("without model fails", function() {

		expect(function() {
			new GeoCodeRequestView({});
		}).toThrow();
	});

	it("works all needed parameters", function() {
		expect(this.view).toBeDefined();
	});

	it("calls resetForm method if the reset button was clicked", function() {

		spyOn(this.view, "resetForm");

		var postCode = this.view.$("input#postCode");
		var reset = this.view.$("button#resetButton");

		postCode.val("76227");

		reset.click();

		expect(this.view.resetForm).toHaveBeenCalled();
	});
    
    it("has a country select box in Bootstrap Dropdown Style", function() {

        expect(this.view.$el.html()).toContain("country-container");
        expect(this.view.$el.html()).toContain("btn-group");
        
    });

});

describe("GeoCodeRequestView reset", function() {

    var searchStatus = new SearchStatus();

    beforeEach(function() {
        this.geoCodeRequest = new GeoCodeRequest({countries: new SelectableAwareCollection([{}])});
        this.view = new GeoCodeRequestView({
            model : this.geoCodeRequest,
            searchStatus: searchStatus
        });
    });
    it("triggers reset event", function() {
        searchStatus.setHaveSearched(true);
        var triggered = false;
        this.geoCodeRequest.on("reset", function(){
            triggered = true;
        });
        this.view.reset();
        expect(triggered).toBe(true);
    });
});