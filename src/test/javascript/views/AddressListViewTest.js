describe("A AddressListView", function () {

	var view;
	
    describe("instantiation", function () {
    	
        it("cannot be instantiated without a model", function () {
            expect(function() {
                new AddressListView();
            }).toThrow();
        });
        
    });
    
    describe("rendering", function () {
       
        beforeEach(function(){
        	
            view = new AddressListView({
                model: new AddressListModel({addresses: [new Address(), new Address()]})
            });
            
        });

        it("can be instantiated with a model", function () {

            expect(view).toBeDefined();
        });
        
        it("html contains all important classes", function () {

            expect(view.el).toBeDefined();
            expect(view.$el.html()).toContain("accordion-group");
            expect(view.$el.html()).toContain("accordion-heading");
            expect(view.$el.html()).toContain("selectAddressRow");

        });
        
    });
   
});

