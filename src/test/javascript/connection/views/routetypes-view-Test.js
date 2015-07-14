describe('Route Types View', function () {

    var routeTypes;
    var rail;
    var barge;
    var sut;

    beforeEach(function () {
        rail = new RouteType({value: 'RAIL'});
        barge = new RouteType({value: 'BARGE', selected: true});
        routeTypes = new RouteTypes([rail, barge]);

        exportTemplateManagerAsGlobalFunction("src/connections/templates/");
    });

    it('renders', function () {
        sut = RouteTypesView.prototype.create({
            model: routeTypes
        });
        expect(sut.$('#routeType > option').length).toBe(2);
        expect(sut.$el.html()).toContain('<option value="RAIL">RAIL</option>');
        expect(sut.$el.html()).toContain('<option value="BARGE" selected="selected">BARGE</option>');
    });

    it('updates', function () {
        sut = RouteTypesView.prototype.create({
            model: routeTypes
        });

        sut.$('#routeType').val('RAIL');
        sut.$('#routeType').change();

        expect(routeTypes.getSelected()).toBe(rail);
    });
});