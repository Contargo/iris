describe('Connection', function () {

    var sut;
    var seaport;
    var terminal;

    beforeEach(function () {
        seaport = new ConnectionSeaport({uniqueId: '4'});
        terminal = new ConnectionTerminal({uniqueId: '5'});

        sut = new Connection({
            id: 3,
            enabled: true,
            seaport: seaport,
            terminal: terminal,
            routeType: new RouteType({value: 'BARGE'}),
            distances: new Distances({
                barge: 42,
                raildiesel: 23,
                railelectric: 65
            })
        });
    });

    it('updates terminal', function () {
        sut.updateTerminal('terminal');
        expect(sut.get('terminal')).toBe('terminal');
    });

    it('updates seaport', function () {
        sut.updateSeaport('seaport');
        expect(sut.get('seaport')).toBe('seaport');
    });

    it('updates route type', function () {

        var routeType = new RouteType({
            value: 'RAIL'
        });
        sut.updateRouteType(routeType);
        expect(sut.get('routeType')).toBe(routeType);
    });

});