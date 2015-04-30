describe("Seaport", function() {
	var port = new Seaport();

	it("can be instantiated", function() {
		expect(port).toBeDefined();
	});

	it("has a name", function() {
		port.set("name", "hans");
		expect(port.get("name")).toEqual("hans");
	});

	it("has possibleTypes", function() {
		expect(port.get("possibleTypes")).toBeDefined();
	});

	it("has canAcceptTransport Method", function() {
		expect(port.canAcceptTransport).toBeDefined();
	});

	// it("canAcceptTransport returns false by default", function () {
	// expect(port.canAcceptTransport()).toBeFalsy();
	// expect(port.canAcceptTransport("FOOT")).toBeFalsy();
	//
	// });
	//
	// it("canAcceptTransport returns true if transport is possible", function
	// () {
	// port.set("possibleTypes", ["ROCKET", "FOOT"]);
	// expect(port.canAcceptTransport("FOOT")).toBeTruthy();
	//
	// });

});

describe("SeaportList", function() {
	var list = new SeaportList();

	it("can be instantiated", function() {
		expect(list).toBeDefined();
	});

	it("has no elements by default", function() {
		expect(list.size()).toEqual(0);
	});

	it("has filterByPossibleType function", function() {
		expect(list.filterByPossibleType).toBeDefined();
	});

	it("filterByPossibleType returns empty array by default", function() {
		var foos = list.filterByPossibleType("FOO");
		expect(foos).toBeDefined();
		expect(foos.size()).toEqual(0);
	});

	// it("filterByPossibleType returns correct ports", function() {
	//
	// list.reset([ {
	// name : "A",
	// possibleTypes : [ "A" ]
	// }, {
	// name : "B",
	// possibleTypes : [ "B" ]
	// }, {
	// name : "AB",
	// possibleTypes : [ "A", "B", "C" ]
	// } ]);
	//
	// var as = list.filterByPossibleType("A");
	// expect(as).toBeDefined();
	// expect(as.size()).toEqual(2);
	//
	// var bs = list.filterByPossibleType("B");
	// expect(bs).toBeDefined();
	// expect(bs.size()).toEqual(2);
	//
	// var cs = list.filterByPossibleType("C");
	// expect(cs).toBeDefined();
	// expect(cs.size()).toEqual(1);
	//
	// });

	// it("setvisibility sets correct visibilities", function () {
	//
	// var a = new Seaport({ name : "A", possibleTypes : [ "A"] });
	// var b = new Seaport({ name : "B", possibleTypes : [ "B"] });
	// var c = new Seaport({ name : "C", possibleTypes : [ "A", "B", "C"] });
	//      
	// list.reset([a,b,c]);
	//
	// list.setVisibility("A");
	//
	// expect(a.get("invisible")).toBeFalsy();
	// expect(b.get("invisible")).toBeTruthy();
	// expect(c.get("invisible")).toBeFalsy();
	//
	// list.setVisibility("C");
	// expect(a.get("invisible")).toBeTruthy();
	// expect(b.get("invisible")).toBeTruthy();
	// expect(c.get("invisible")).toBeFalsy();
	//
	//
	//
	// });

});
