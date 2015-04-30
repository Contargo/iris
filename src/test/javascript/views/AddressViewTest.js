describe("A AddressView", function() {

	var view;

	describe("instantiation", function() {

		it("cannot be instantiated without a model", function() {
			expect(function() {
				new AddressView();
			}).toThrow();
		});

	});

	describe("rendering", function() {

		beforeEach(function() {

			view = new AddressView({
				model : new Address({
					latitude : 0,
					longitude : 0,
					displayName : "fooAdress",
					selected : false
				})
			});
			
			view.render();

		});

		it("can be instantiated with a model", function() {

			expect(view).toBeDefined();
		});

		it("displayes displayName correctly", function() {

			expect(view.el).toBeDefined();
			
			expect(view.$el.html()).toContain("fooAdress");

		});
		
		it("swiches selected when calling method selectAddress", function() {

			var selectBefore = view.model.get("selected");
			
			view.selectAddress();
			
			var selectAfter = view.model.get("selected");
			
			expect(selectBefore).toBeFalsy();
			
			expect(selectAfter).toBeTruthy();

		});

	});

});