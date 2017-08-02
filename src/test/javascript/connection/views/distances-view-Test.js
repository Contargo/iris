describe('Distances View', function () {

    var distances;
    var sut;

    beforeEach(function () {
        distances = new Distances({
            barge: 10,
            raildiesel: 20,
            railelectric: 30,
            road: 40
        });

        exportTemplateManagerAsGlobalFunction("src/connections/templates/");
    });

    it('renders for barge connection', function () {
        sut = DistancesView.prototype.create({
            model: distances,
            isBarge: true,
            isRail: false,
            isDtruck: false,
            isDtruckAvailable: true
        });
        expect(sut.$el.html()).toContain('<input id="bargeDieselDistance" name="bargeDieselDistance" class="comma-replacement-aware number" title="Barge km" type="text" value="10">');
        expect(sut.$el.html()).toContain('<input id="railDieselDistance" name="railDieselDistance" class="comma-replacement-aware number" title="Rail Diesel km" type="text" value="20" readonly="readonly">');
        expect(sut.$el.html()).toContain('<input id="railElectricDistance" name="railElectricDistance" class="comma-replacement-aware number" title="Rail Electrical km" type="text" value="30" readonly="readonly">');
        expect(sut.$el.html()).toContain('<input id="roadDistance" name="roadDistance" class="comma-replacement-aware number" title="Road km" type="text" value="40" readonly="readonly">');
    });

    it('renders for barge connection without direct truck', function () {
        sut = DistancesView.prototype.create({
            model: distances,
            isBarge: true,
            isRail: false,
            isDtruck: false,
            isDtruckAvailable: false
        });
        expect(sut.$el.html()).toContain('<input id="bargeDieselDistance" name="bargeDieselDistance" class="comma-replacement-aware number" title="Barge km" type="text" value="10">');
        expect(sut.$el.html()).toContain('<input id="railDieselDistance" name="railDieselDistance" class="comma-replacement-aware number" title="Rail Diesel km" type="text" value="20" readonly="readonly">');
        expect(sut.$el.html()).toContain('<input id="railElectricDistance" name="railElectricDistance" class="comma-replacement-aware number" title="Rail Electrical km" type="text" value="30" readonly="readonly">');
        expect(sut.$el.html()).not.toContain('roadDistance');
    });

    it('renders for rail connection', function () {
        sut = DistancesView.prototype.create({
            model: distances,
            isBarge: false,
            isRail: true,
            isDtruck: false,
            isDtruckAvailable: true
        });
        expect(sut.$el.html()).toContain('<input id="bargeDieselDistance" name="bargeDieselDistance" class="comma-replacement-aware number" title="Barge km" type="text" value="10" readonly="readonly">');
        expect(sut.$el.html()).toContain('<input id="railDieselDistance" name="railDieselDistance" class="comma-replacement-aware number" title="Rail Diesel km" type="text" value="20">');
        expect(sut.$el.html()).toContain('<input id="railElectricDistance" name="railElectricDistance" class="comma-replacement-aware number" title="Rail Electrical km" type="text" value="30">');
        expect(sut.$el.html()).toContain('<input id="roadDistance" name="roadDistance" class="comma-replacement-aware number" title="Road km" type="text" value="40" readonly="readonly">');
    });

    it('renders for dtruck connection', function () {
        sut = DistancesView.prototype.create({
            model: distances,
            isBarge: false,
            isRail: false,
            isDtruck: true,
            isDtruckAvailable: true
        });
        expect(sut.$el.html()).toContain('<input id="bargeDieselDistance" name="bargeDieselDistance" class="comma-replacement-aware number" title="Barge km" type="text" value="10" readonly="readonly">');
        expect(sut.$el.html()).toContain('<input id="railDieselDistance" name="railDieselDistance" class="comma-replacement-aware number" title="Rail Diesel km" type="text" value="20" readonly="readonly">');
        expect(sut.$el.html()).toContain('<input id="railElectricDistance" name="railElectricDistance" class="comma-replacement-aware number" title="Rail Electrical km" type="text" value="30" readonly="readonly">');
        expect(sut.$el.html()).toContain('<input id="roadDistance" name="roadDistance" class="comma-replacement-aware number" title="Road km" type="text" value="40">');
    });

    it('updates barge', function () {
        sut = DistancesView.prototype.create({
            model: distances,
            isBarge: true,
            isRail: false,
            isDtruckAvailable: true
        });

        spyOn(sut, 'update');

        sut.$('#bargeDieselDistance').val('42');
        sut.$('#bargeDieselDistance').change();

        expect(sut.update).toHaveBeenCalledWith('barge', '42');
    });

    it('updates rail diesel', function () {
        sut = DistancesView.prototype.create({
            model: distances,
            isBarge: false,
            isRail: true,
            isDtruck: false,
            isDtruckAvailable: true
        });

        spyOn(sut, 'update');

        sut.$('#railDieselDistance').val('42');
        sut.$('#railDieselDistance').change();

        expect(sut.update).toHaveBeenCalledWith('raildiesel', '42');
    });

    it('updates rail electric', function () {
        sut = DistancesView.prototype.create({
            model: distances,
            isBarge: false,
            isRail: true,
            isDtruck: false,
            isDtruckAvailable: true
        });

        spyOn(sut, 'update');

        sut.$('#railElectricDistance').val('42');
        sut.$('#railElectricDistance').change();

        expect(sut.update).toHaveBeenCalledWith('railelectric', '42');
    });

    it('updates road', function () {
        sut = DistancesView.prototype.create({
            model: distances,
            isBarge: false,
            isRail: false,
            isDtruck: true,
            isDtruckAvailable: true
        });

        spyOn(sut, 'update');

        sut.$('#roadDistance').val('42');
        sut.$('#roadDistance').change();

        expect(sut.update).toHaveBeenCalledWith('road', '42');
    });

    it('updates its model attributes', function () {
        sut = DistancesView.prototype.create({
            model: distances,
            isBarge: true,
            isRail: false,
            isDtruck: false,
            isDtruckAvailable: true
        });
        sut.update('barge', '42');
        expect(distances.get('barge')).toBe(42);
    });
});