describe("A TriangularTrafficDropDownView", function () {

    describe("instantiation", function () {
        it("cannot be instantiated without a model", function () {
            expect(function() {
                new TriangularTrafficDropDownView();
            }).toThrow();
        });
    });

    describe("rendering", function () {

        beforeEach(function(){
            this.seaportAntwerp = new Seaport({name: "Antwerp"});
            this.seaportWoerth = new Seaport({name: "Wörth"});

            this.seaports = new SeaportList([this.seaportAntwerp, this.seaportWoerth]);
            this.view = new TriangularTrafficDropDownView({model: this.seaports, el: $('<select></select>')});
        });

        it("can be instantiated with a model", function () {
            expect(this.view).toBeDefined();
        });
        
        it("renders all options", function(){
            expect(this.view.$el.html()).toContain("Wörth");
            expect(this.view.$el.find("option:contains(Antwerp)").html()).toContain("Antwerp");
            expect(this.view.$el.find("option").length).toBe(2);
        });
        it("sets default value", function(){
            var view = new TriangularTrafficDropDownView({model: this.seaports, el: $('<select/>'), selectedText: "Wörth"});
            expect(this.seaportAntwerp.get("defaultValue")).toBeTruthy();
            expect(view.$el.find("option:contains(Wörth)").attr("selected")).not.toBe("selected");
            expect(view.$el.find("option:contains(Antwerp)").attr("selected")).toBe("selected");
            expect(view.$el.find("option").length).toBe(2);
        });
        it("sets no default value - nothing in the list", function(){
            var view = new TriangularTrafficDropDownView({model: new SeaportList(), el: $('<select/>'), selectedText: ''});
            expect(view.$el.find("option").length).toBe(0);
        });
        it("sets first element to selected without default value", function(){
            var view = new TriangularTrafficDropDownView({model: this.seaports, el: $('<select/>')});
            expect(this.seaportAntwerp.get("defaultValue")).toBeTruthy();
            expect(view.$el.find("option:contains(Antwerp)").attr("selected")).toBe("selected");
            expect(view.$el.find("option:contains(Wörth)").attr("selected")).not.toBe("selected");
            expect(view.$el.find("option").length).toBe(2);
        });
        it("ignores invisible elements", function(){
            this.seaports.add(new Seaport({name: "Karlsruhe", invisible: true}));
            var view = new TriangularTrafficDropDownView({model: this.seaports, el: $('<select/>')});
            expect(view.$el.find("option").length).toBe(2);
        })
    });
});