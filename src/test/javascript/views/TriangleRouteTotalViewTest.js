
describe("TriangleRouteTotalView", function () {

    beforeEach(function(){
        
        this.model = new Backbone.Model({
            distance : 777,
            toll : 888  ,
            size: 999,
            duration: 666
        });
        
        this.view = new TriangleRouteTotalView({model : this.model });
        
    });

    it("without model fails", function () {

        expect(
            function() {
                new TriangleRouteTotalView();
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


    it("renders total stops", function () {
        expect(this.view.$el.html()).toContain(this.model.get("size"));
    });

    it("renders distance", function () {
        expect(this.view.$el.html()).toContain(this.model.get("distance"));
    });

    it("renders toll distance", function () {
        expect(this.view.$el.html()).toContain(this.model.get("toll"));
    });

    it("renders duration", function () {
        expect(this.view.$el.html()).toContain(this.model.get("duration"));
    });

    it("renders clear button", function () {
        expect(this.view.$el.html()).toContain("clear");
    });

    it("re-renders itself to target on change", function () {

        this.view.$el.empty();

        this.model.set("duration", "miminutes");
        
    
        expect(this.view.$el.html()).toContain('miminutes');

    });

    
});



