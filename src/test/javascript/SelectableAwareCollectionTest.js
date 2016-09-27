describe("SelectableAwareCollection", function () {
    
    describe("instantiation", function () {

        it("can be instantiated", function () {
            
            expect(new SelectableAwareCollection()).toBeDefined();
        });
        
        it("can be instantiated with params", function () {
            expect(new SelectableAwareCollection([
                {name: "Waterway", value: "WATERWAY", defaultValue: true},
                {name: "Rail", value: "RAILWAY"},
                {name: "All", value: "ALL"}
            ])).toBeDefined();
        });
        
    });
    
    describe("selectable aware functionality", function () {
        
        beforeEach(function() {
            
            this.collection = new SelectableAwareCollection([
                {name: "Waterway", value: "WATERWAY", defaultValue: true},
                {name: "Rail", value: "RAILWAY"},
                {name: "All", value: "ALL"}
            ]);

	});

        it("deselect all other elements if an element is selected", function () {
            
            var element1 = this.collection.at(1).set("selected", true);
            
            expect(element1.get("selected")).toBe(true);
            
            for(var i = 0; i < this.collection.length; i++) {
                if(this.collection.at(i) != element1) {
                    expect(this.collection.at(i).get("selected")).toBe(false);
                }
            }
            
            // now change the selected element
            this.collection.at(2).set("selected", true);
            
            // show that formerly selected element is not selected anymore
            expect(element1.get("selected")).toBe(false);
            
        });
        
        it("triggers selectedchanged if an element is selected", function () {
            
            var spy = jasmine.createSpy("changelistener");

            this.collection.bind("selectedchanged", spy);

            var element1 = this.collection.at(1).set("selected", true);
            
            expect(element1.get("selected")).toBe(true);

            expect(spy).toHaveBeenCalled();
            expect(spy).toHaveBeenCalledWith(element1);
        });
        
        it("finds selected element", function () {
            
            var selected = this.collection.at(1).set("selected", true);
            
            var searchResult = this.collection.getSelected();
            
            expect(searchResult.get("name")).toEqual(selected.get("name"));
            expect(searchResult).toEqual(selected);
        });
        
        it("sets the element with defaultValue = true as selected", function () {
            
            // element with defaultValue = true is: Waterway = this.collection.at(0)
            
            // show that there is no selected element
            for(var i = 0; i < this.collection.length; i++) {
                expect(this.collection.at(i).get("selected")).toBe(undefined);
            }
            
            this.collection.setDefaultToSelected();
            
            var searchResult = this.collection.getSelected();
            
            expect(searchResult).toBe(this.collection.at(0));
        });
        
    });
    
});


