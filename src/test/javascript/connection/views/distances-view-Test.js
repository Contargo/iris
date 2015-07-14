describe('Distances View', function () {

    var distances;
    var sut;

    beforeEach(function () {
        distances = new Distances({
            barge: 10,
            raildiesel: 20,
            railelectric: 30
        });

        exportTemplateManagerAsGlobalFunction("src/connections/templates/");
    });

    it('renders', function () {
        sut = DistancesView.prototype.create({
            model: distances
        });
        expect(sut.$('#bargeDieselDistance').val()).toBe('10');
        expect(sut.$('#railDieselDistance').val()).toBe('20');
        expect(sut.$('#railElectricDistance').val()).toBe('30');
    });

    it('updates barge', function () {
        sut = DistancesView.prototype.create({
            model: distances
        });

        spyOn(sut, 'update');

        sut.$('#bargeDieselDistance').val('42');
        sut.$('#bargeDieselDistance').change();

        expect(sut.update).toHaveBeenCalledWith('barge', '42');
    });

    it('updates rail diesel', function () {
        sut = DistancesView.prototype.create({
            model: distances
        });

        spyOn(sut, 'update');

        sut.$('#railDieselDistance').val('42');
        sut.$('#railDieselDistance').change();

        expect(sut.update).toHaveBeenCalledWith('raildiesel', '42');
    });

    it('updates rail electric', function () {
        sut = DistancesView.prototype.create({
            model: distances
        });

        spyOn(sut, 'update');

        sut.$('#railElectricDistance').val('42');
        sut.$('#railElectricDistance').change();

        expect(sut.update).toHaveBeenCalledWith('railelectric', '42');
    });

    it('updates its model attributes', function () {
        sut = DistancesView.prototype.create({
            model: distances
        });
        sut.update('barge', '42');
        expect(distances.get('barge')).toBe(42);
    });
});