
describe("A TerminalView", function () {

    beforeEach(function(){
        //
    });

    describe("instantiation", function () {

        it("cannot be instantiated if its template does not exist in the dom", function () {
            expect(function() {
                new TerminalView();
            }).toThrow();
        });
    });

    describe("rendering", function () {

        beforeEach(function(){
            this.terminal = new Terminal({name: "myterminal"});
            this.view = new TerminalView({model: this.terminal});
        });

        it("can be instantiated if its template is found", function () {

            expect(this.view).toBeDefined();

        });


        it("renders its name", function () {

            this.view.render();
            expect(this.view.$el.html()).toContain(this.terminal.get("name"));

        });

        


    });
});