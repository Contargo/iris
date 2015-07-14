describe('Terminals View', function () {

    var terminals;
    var terminal1;
    var terminal2;
    var sut;

    beforeEach(function () {
        terminal1 = new ConnectionTerminal({
            uniqueId: '42',
            name: 'Hinterweiler',
            enabled: false,
            selected: false
        });
        terminal2 = new ConnectionTerminal({
            uniqueId: '23',
            name: 'Wimmelburg',
            enabled: true,
            selected: true
        });
        terminals = new ConnectionTerminals([terminal1, terminal2]);

        exportTemplateManagerAsGlobalFunction("src/connections/templates/");
    });

    it('renders', function () {
        sut = TerminalsView.prototype.create({
            model: terminals
        });
        expect(sut.$('#terminal > option').length).toBe(2);
        expect(sut.$el.html()).toContain('<option value="42">Hinterweiler (not enabled)</option>');
        expect(sut.$el.html()).toContain('<option value="23" selected="selected">Wimmelburg (enabled)</option>');
    });

    it('updates', function () {
        sut = TerminalsView.prototype.create({
            model: terminals
        });

        sut.$('#terminal').val('42');
        sut.$('#terminal').change();

        expect(terminals.getSelectedName()).toBe('Hinterweiler');
    });
});