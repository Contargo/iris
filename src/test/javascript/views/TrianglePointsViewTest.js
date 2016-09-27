
describe("TrianglePointsView instanciation", function () {

    beforeEach(function(){
        this.serverMock = {
            getRouteDetails : jasmine.createSpy("getRouteDetails")
        };
        this.model = new TrianglePoints([],{server: this.serverMock});
        

        this.fakeViewRenderCalled = 0;
        var self = this;
        this.fakeview = Backbone.View.extend({
            initialize : function() {
                this.render();
            },
            render : function() {
                self.fakeViewRenderCalled++;
                var name = this.model.get("to").get("name");
                this.$el.html("<a class='CHILD'>xxx "+name+"</a>");
            }
        });
        
        this.view = new TrianglePointsView({model : this.model, childView : this.fakeview });
       
        
    });

    it("without model fails", function () {

        expect(
            function() {
                new TrianglePointsView({});
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

    it("re-renders itself to target on reset", function () {
        this.view.$el.empty();
        this.model.reset();

        expect(this.view.$el).not.toBeEmpty();
    });

    it("re-renders itself to target on add and remove", function () {

        var address = new Address();
        var address2 = new Address();

        this.view.$el.empty();

        this.model.add(address);
        expect(this.fakeViewRenderCalled).toBe(1);
        this.model.add(address2);
        expect(this.fakeViewRenderCalled).toBe(3);
        expect(this.view.$el.html()).toContain('CHILD');

        this.view.$el.empty();
        this.model.remove(address);
        expect(this.view.$el).not.toBeEmpty();
    });



    it("re-renders itself on repositioning of items", function () {

        var address = new Address({name : "A"});
        var address2 = new Address({name : "B"});

        this.view.$el.empty();
        this.model.add(address);
        this.model.add(address2);

        var children = this.view.$("a.CHILD");
        expect(children.length).toEqual(2);

        expect(children.eq(0).html()).toContain("xxx A");
        expect(children.eq(1).html()).toContain("xxx B");


        this.view.$el.empty();
        var second = this.model.getRoutePairs().at(1);
        second.trigger("up", second);

        var children = this.view.$("a.CHILD");
        expect(children.length).toEqual(2);

        expect(children.eq(0).html()).toContain("xxx B");
        expect(children.eq(1).html()).toContain("xxx A");


    });


    it("clears list on click on trashcan", function () {

        var address = new Address({name : "A"});
        var address2 = new Address({name : "B"});
        this.model.add(address);
        this.model.add(address2);
        expect(this.model.size()).toEqual(2);
        var button = this.view.$(".clear");
        expect(button.length).toEqual(1);

        button.click();
        expect(this.model.size()).toEqual(0);

    });
    
});



