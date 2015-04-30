
describe("TriangleRoutePartView", function () {

    beforeEach(function(){
        
        this.from = new Address({displayName: "MyAddy", type : "ADDRESS"});
        this.to = new Terminal( {name :"SuperTerm", type : "TERMINAL"});
        this.model = new TriangleRoutePart({"from" : this.from, "to" : this.to});
        this.view = new TriangleRoutePartView({model : this.model });
        
    });

    it("without model fails", function () {

        expect(
            function() {
                new TriangleRoutePartView({});
            }
        ).toThrow();
    });


    it("works all needed parameters", function () {
        
        expect(this.view).toBeDefined();
        expect(this.view.render).toBeDefined();
    });


    it("renders itself to target on load", function () {
        expect(this.view.$el).not.toBeEmpty();
    });


    it("renders name of location", function () {
        expect(this.view.$el.html()).toContain("SuperTerm");
    });



    it("renders name of address", function () {
        expect(this.view.$el.html()).toContain("MyAddy");
    });





    it("triggers destoy on trashcan-click", function () {

        var spy = jasmine.createSpy("destroyspy");
        this.model.bind("destroy", spy);

        var button = this.view.$(".remove");
        expect(button.length).toBe(1);

        button.click();
        expect(spy).toHaveBeenCalled();

    });


    it("triggers up on upclick", function () {

        var spy = jasmine.createSpy("up");
        this.model.bind("up", spy);

        var button = this.view.$(".up");
        expect(button.length).toBe(1);

        button.click();
        expect(spy).toHaveBeenCalled();

    });

    it("triggers down on downclick", function () {

        var spy = jasmine.createSpy("down");
        this.model.bind("down", spy);

        var button = this.view.$(".down");
        expect(button.length).toBe(1);

        button.click();
        expect(spy).toHaveBeenCalled();

    });
    
});



