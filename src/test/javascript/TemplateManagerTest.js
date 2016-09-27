describe("TemplateManager", function() {

    it("can be instatiated", function() {

        this.manager = new TemplateManager();

        expect(this.manager).toBeDefined();
    });

    it("can be instatiated", function() {

        this.manager = new TemplateManager();

        expect(this.manager).toBeDefined();
    });


    it("has method to load a template", function() {

        this.manager = new TemplateManager();

        expect(this.manager.getTemplate).toBeDefined();
    });



    it("throws on error", function() {

        this.manager = new TemplateManager();

        spyOn($, "ajax").and.callFake(function(options) {
            options.error(null, "argh", "argh");
        });
        
        expect(function() {
            var result = this.manager.getTemplate("foo");    
        }).toThrow();

        
        $.ajax.calls.reset();
        

    });


    it("loads a template", function() {

        this.manager = new TemplateManager();
        var data = "<span class='template'><span class='foo'>Template</span> {{name}}</span>";
        var templatename = "foo";

        spyOn($, "ajax").and.callFake(function(options) {
            expect(options.url).toContain(templatename);
            options.success(data);
        });

        var result = this.manager.getTemplate(templatename);
        expect(result).toBeDefined();
        
        var html = result({name : "wurst"});
        expect(html).toContain("wurst");
        expect(html).toContain("foo");

        $.ajax.calls.reset();

    });




    it("caches a template", function() {

        var manager = new TemplateManager();
        
        var data = "<span class='template'>Template</span>";
        var templatename = "foo";

        spyOn($, "ajax").and.callFake(function(options) {
            expect(options.url).toContain(templatename);
            options.success(data);
        });

        var result = manager.getTemplate(templatename);
        expect(result).toBeDefined();

        var result2 = manager.getTemplate(templatename);
        expect(result2).toBeDefined();
        expect(result).toEqual(result2);

        expect($.ajax.calls.count()).toEqual(1);
        
        $.ajax.calls.reset();

    });
});