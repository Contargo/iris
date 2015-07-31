describe('Subconnections View', function () {

    var sut;
    var subconnection;
    var terminals;
    var terminal1, terminal2;

    beforeEach(function () {
        terminal1 = new ConnectionTerminal({
            uniqueId: '42',
            name: 'Hinterweiler'
        });
        terminal2 = new ConnectionTerminal({
            uniqueId: '23',
            name: 'Wimmelburg'
        });
        terminals = new ConnectionTerminals([terminal1, terminal2]);
        subconnection = new Subconnection({
            id: 5,
            endpoint1: terminal1,
            endpoint2: terminal2,
            distances: new Distances({
                barge: 42,
                raildiesel: 23,
                railelectric: 65
            }),
            routeType: new RouteType({value: 'RAIL', name: 'Rail'})
        });

        exportTemplateManagerAsGlobalFunction("src/connections/templates/");
    });

    it('renders', function () {
        sut = SubconnectionView.prototype.create({
            model: subconnection,
            terminals: terminals,
            latest: false,
            first: false
        });
        expect(sut.$el.html()).toContain('<input class="start" title="start" type="text" readonly="" value="Hinterweiler">');
        expect(sut.$el.html()).toContain('<input class="type input-small" title="type" type="text" readonly="" value="Rail">');
        expect(sut.$('.end > option').length).toBe(2);
        expect(sut.$('.end').attr('disabled')).toBe('disabled');
        expect(sut.$el.html()).toContain('<option value="42">Hinterweiler</option>');
        expect(sut.$el.html()).toContain('<option value="23" selected="selected">Wimmelburg</option>');
        expect(sut.$el.html()).not.toContain('<button class="new-subconnection btn btn-primary"><i class="icon-plus-sign icon-white"></i></button>');
        expect(sut.$el.html()).not.toContain('<button class="remove-subconnection btn btn-primary btn-danger"><i class="icon-trash icon-white"></i></button>');
    });

    it('renders barge subconnection', function () {
        subconnection.set('routeType',new RouteType({value: 'BARGE', name: 'Barge'}));
        sut = SubconnectionView.prototype.create({
            model: subconnection,
            terminals: terminals,
            latest: false,
            first: false
        });
        expect(sut.$el.html()).toContain('<input class="barge comma-replacement-aware number input-small" title="barge" type="text" value="42">');
        expect(sut.$el.html()).toContain('<input class="raildiesel comma-replacement-aware number input-small" title="raildiesel" type="text" value="23" readonly="readonly">');
        expect(sut.$el.html()).toContain('<input class="railelectric comma-replacement-aware number input-small" title="railelectric" type="text" value="65" readonly="readonly">');
    });

    it('renders rail subconnection', function () {
        sut = SubconnectionView.prototype.create({
            model: subconnection,
            terminals: terminals,
            latest: false,
            first: false
        });
        expect(sut.$el.html()).toContain('<input class="barge comma-replacement-aware number input-small" title="barge" type="text" value="42" readonly="readonly">');
        expect(sut.$el.html()).toContain('<input class="raildiesel comma-replacement-aware number input-small" title="raildiesel" type="text" value="23">');
        expect(sut.$el.html()).toContain('<input class="railelectric comma-replacement-aware number input-small" title="railelectric" type="text" value="65">');
    });

    it('renders for latest subconnection', function () {
        sut = SubconnectionView.prototype.create({
            model: subconnection,
            terminals: terminals,
            latest: true,
            first: false
        });
        expect(sut.$el.html()).toContain('<input class="start" title="start" type="text" readonly="" value="Hinterweiler">');
        expect(sut.$el.html()).toContain('<input class="type input-small" title="type" type="text" readonly="" value="Rail">');
        expect(sut.$el.html()).toContain('<input class="barge comma-replacement-aware number input-small" title="barge" type="text" value="42" readonly="readonly">');
        expect(sut.$el.html()).toContain('<input class="raildiesel comma-replacement-aware number input-small" title="raildiesel" type="text" value="23">');
        expect(sut.$el.html()).toContain('<input class="railelectric comma-replacement-aware number input-small" title="railelectric" type="text" value="65">');
        expect(sut.$('.end > option').length).toBe(2);
        expect(sut.$('.end').attr('disabled')).not.toBeDefined();
        expect(sut.$el.html()).toContain('<option value="42">Hinterweiler</option>');
        expect(sut.$el.html()).toContain('<option value="23" selected="selected">Wimmelburg</option>');
        expect(sut.$el.html()).toContain('<button class="new-subconnection btn btn-primary"><i class="icon-plus-sign icon-white"></i></button>');
        expect(sut.$el.html()).toContain('<button class="remove-subconnection btn btn-primary btn-danger"><i class="icon-trash icon-white"></i></button>');
    });

    it('renders for latest and only subconnection', function () {
        sut = SubconnectionView.prototype.create({
            model: subconnection,
            terminals: terminals,
            latest: true,
            first: true
        });
        expect(sut.$el.html()).not.toContain('<button class="remove-subconnection btn btn-primary btn-danger"><i class="icon-trash icon-white"></i></button>');
    });

    it('updates barge', function () {
        sut = SubconnectionView.prototype.create({
            model: subconnection,
            terminals: terminals,
            latest: false
        });
        sut.$('.barge').val('111');
        sut.$('.barge').change();

        expect(subconnection.get('distances').get('barge')).toBe(111);
    });

    it('updates rail diesel', function () {
        sut = SubconnectionView.prototype.create({
            model: subconnection,
            terminals: terminals,
            latest: false
        });
        sut.$('.raildiesel').val('111');
        sut.$('.raildiesel').change();

        expect(subconnection.get('distances').get('raildiesel')).toBe(111);
    });

    it('updates rail electric', function () {
        sut = SubconnectionView.prototype.create({
            model: subconnection,
            terminals: terminals,
            latest: false
        });
        sut.$('.railelectric').val('111');
        sut.$('.railelectric').change();

        expect(subconnection.get('distances').get('railelectric')).toBe(111);
    });

    it('updates endpoint2', function () {
        sut = SubconnectionView.prototype.create({
            model: subconnection,
            terminals: terminals,
            latest: false
        });
        sut.$('.end').val('42');
        sut.$('.end').change();

        expect(subconnection.get('endpoint2')).toBe(terminal1);
    });

    it('adds a new subconnection', function () {
        var subconnections = new Subconnections(subconnection);
        var addNew = false;
        subconnections.trigger = function (event) {
            if(event === 'addNew'){
                addNew = true;
            }
        };
        sut = SubconnectionView.prototype.create({
            model: subconnection,
            terminals: terminals,
            latest: true
        });
        sut.$('.new-subconnection').click();

        expect(addNew).toBe(true);
    });

    it('removes a new subconnection', function () {
        var subconnections = new Subconnections(subconnection);
        var removeLast = false;
        subconnections.trigger = function (event) {
            if(event === 'removeLast'){
                removeLast = true;
            }
        };
        sut = SubconnectionView.prototype.create({
            model: subconnection,
            terminals: terminals,
            latest: true
        });
        sut.$('.remove-subconnection').click();

        expect(removeLast).toBe(true);
    });
});