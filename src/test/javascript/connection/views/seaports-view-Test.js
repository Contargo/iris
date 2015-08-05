describe('Seaports View', function () {

    var ports;
    var port1;
    var port2;
    var sut;

    beforeEach(function () {
        port1 = new ConnectionSeaport({
            uniqueId: '42',
            name: 'Stockach',
            enabled: false,
            selected: false
        });
        port2 = new ConnectionSeaport({
            uniqueId: '23',
            name: 'Luettchendorf',
            enabled: true,
            selected: true
        });
        ports = new ConnectionSeaports([port1, port2]);

        exportTemplateManagerAsGlobalFunction("src/connections/templates/");
    });

    it('renders', function () {
        sut = SeaportsView.prototype.create({
            model: ports
        });
        expect(sut.$('#seaport > option').length).toBe(2);
        expect(sut.$el.html()).toContain('<option value="42">Stockach (not enabled)</option>');
        expect(sut.$el.html()).toContain('<option value="23" selected="selected">Luettchendorf (enabled)</option>');
    });

    it('updates', function () {
        sut = SeaportsView.prototype.create({
            model: ports
        });

        sut.$('#seaport').val('42');
        sut.$('#seaport').change();

        expect(ports.getSelectedName()).toBe('Stockach');
    });
});