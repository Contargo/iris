describe('Connection View', function () {

    var sut;
    var connection;
    var seaport;
    var seaports;
    var terminal;
    var terminals;
    var barge, rail, barge_rail, dtruck;
    var routeTypes;

    beforeEach(function () {
        barge = new RouteType({value: 'BARGE'});
        rail = new RouteType({value: 'RAIL'});
        barge_rail = new RouteType({value: 'BARGE_RAIL'});
        dtruck = new RouteType({value: 'DTRUCK'});
        routeTypes = new RouteTypes([barge, rail, barge_rail, dtruck]);
        seaport = new ConnectionSeaport({
            uniqueId: '42',
            name: 'seaport'
        });
        seaports = new ConnectionSeaports(seaport);
        terminal = new ConnectionTerminal({
            uniqueId: '23',
            name: 'terminal'
        });
        terminals = new ConnectionTerminals(terminal);
        connection = new Connection({
            id: 5,
            seaport: seaport,
            terminal: terminal,
            routeType: barge
        });

        spyOn(SeaportsView.prototype, 'create').and.callFake(function() {return 0;});
        spyOn(TerminalsView.prototype, 'create').and.callFake(function() {return 0;});
        spyOn(RouteTypesView.prototype, 'create').and.callFake(function() {return 0;});
        spyOn(DistancesView.prototype, 'create').and.callFake(function() {return 0;});

        exportTemplateManagerAsGlobalFunction("src/connections/templates/");
    });

    it('renders barge connection', function () {
        sut = ConnectionView.prototype.create({
            model: connection,
            seaports: seaports,
            terminals: terminals,
            routeTypes: routeTypes
        });

        expect(SeaportsView.prototype.create).toHaveBeenCalled();
        expect(TerminalsView.prototype.create).toHaveBeenCalled();
        expect(RouteTypesView.prototype.create).toHaveBeenCalled();
        expect(DistancesView.prototype.create).toHaveBeenCalled();
        expect(sut.$el.html()).toContain('<h2>Edit main run connection - Seaport seaport to Terminal terminal</h2>');
        expect(sut.$el.html()).toContain('<input id="enabled" name="enabled" type="checkbox" checked="checked">');
        expect(sut.$el.html()).toContain('<button id="submit-button" class="btn btn-primary">Update</button>');
    });

    it('updates enabled', function () {
        sut = ConnectionView.prototype.create({
            model: connection,
            seaports: seaports,
            terminals: terminals,
            routeTypes: routeTypes
        });
        sut.$('#enabled').attr('checked', false);
        sut.$('#enabled').change();
        expect(connection.get('enabled')).toBe(false);
    });

    it('updates connection', function () {
        var updateConnection = false;
        connection.trigger = function (event) {
            if(event === 'updateConnection'){
                updateConnection = true;
            }
        };
        sut = ConnectionView.prototype.create({
            model: connection,
            seaports: seaports,
            terminals: terminals,
            routeTypes: routeTypes
        });
        sut.$('#submit-button').click();
        expect(updateConnection).toBe(true);
    });

    it('displays error if an input has a form error', function() {
        spyOn(MessageView.prototype, 'create');
        sut = ConnectionView.prototype.create({
            model: connection,
            seaports: seaports,
            terminals: terminals,
            routeTypes: routeTypes
        });
        spyOn(sut, 'hasFormError').and.returnValue(true);
        sut.$('#submit-button').click();
        expect(MessageView.prototype.create).toHaveBeenCalledWith({ message : 'Cannot create or update connection:' +
            ' validation errors.', className : 'message message-error message-width' });
    });

});