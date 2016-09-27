describe("A BootstrapDropDownView", function () {
   
    describe("instantiation", function () {

        it("cannot be instantiated without a model", function () {
            expect(function() {
                new BootstrapDropDownView();
            }).toThrow();
        });
        
    });
    
    describe("rendering", function () {
       
        beforeEach(function(){
            this.list = new SelectableAwareCollection([
                {name: "20' light", value: "TWENTY_LIGHT", defaultValue: true },
                {name: "20' heavy", value: "TWENTY_HEAVY"},
                {name: "30'", value: "THIRTY"}]);
            this.view = new BootstrapDropDownView({
                model: this.list
                });
        });

        it("can be instantiated with a model", function () {

            expect(this.view).toBeDefined();
        });
        
        it("has Bootstrap button group", function () {

            expect(this.view.$el.html()).toContain("btn-group");
        });
        
        it("renders children of Bootstrap button group", function () {

            expect(this.view.el).toBeDefined();
            expect(this.view.$el.html()).toContain("dropdown-menu");
            expect(this.view.$el.html()).toContain("dropdown-toggle");
            expect(this.view.$("ul.dropdown-menu").html()).toContain("30");
            expect(this.view.$("a.dropdown-toggle").html()).toContain("20"); // default value is set there

        });
        
        it("renders new children if added", function () {

            this.list.add({name : "50'", value :"FIFTY"});
            expect(this.view.$("ul.dropdown-menu").html()).toContain("30'");
            expect(this.view.$("ul.dropdown-menu").html()).toContain("50'");
            expect(this.view.$("a.dropdown-toggle").html()).toContain("20'");
        });
        
        it("calls selectedchanged method if an element is clicked", function () {

            // element is not selected
            expect(this.view.model.at(1).get("selected")).toBe(false);

            // clicked element is set to selected = true
            this.view.$("ul.dropdown-menu>li:eq(1) a").click();
                        
            // element is now selected
            expect(this.view.model.at(1).get("selected")).toBe(true);
                        
        });

        it("triggers change event when element was not selected", function () {
            var triggered = false;

            var model = this.view.model;
            model.bind("change:selected", function () {
                triggered = true;
            });

            var secondElement = model.at(1);
            expect(secondElement.get("selected")).toBe(false);
            this.view.changeSelectStrategy(secondElement);

            expect(secondElement.get("selected")).toBe(true);
            expect(triggered).toBeTruthy();

        });
        it("triggers no change event when element is selected", function () {
            var triggered = false;

            var model = this.view.model;
            var secondElement = model.at(1);
            secondElement.set('selected', true);

            model.bind("change:selected", function () {
                triggered = true;
            });
            this.view.changeSelectStrategy(secondElement);

            expect(secondElement.get("selected")).toBe(true);
            expect(triggered).toBeFalsy();

        });
       
    });
   
});

