
describe("TriangleRoutePart", function() {

    beforeEach(function() {
        this.from = new Terminal({name : "from"});
        this.to = new Terminal({name : "to"});

        this.part = new TriangleRoutePart({from :this.from, to: this.to});
    });

    it("can be instatiated", function() {

        expect(this.part).toBeDefined();
    });


    it("has from and to", function() {

        var from = this.part.get("from");
        var to = this.part.get("to");

        expect(from).toBe(this.from);
        expect(to).toBe(this.to);

    });

});
