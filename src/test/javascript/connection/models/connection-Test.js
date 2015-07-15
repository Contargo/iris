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
            subconnection: new Subconnections()
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

        spyOn(sut, 'createSubconnection');

        var routeType = new RouteType({
            value: 'RAIL'
        });
        sut.updateRouteType(routeType);
        expect(sut.get('routeType')).toBe(routeType);
        expect(sut.createSubconnection).not.toHaveBeenCalled();
    });

    it('updates route type to barge-rail with empty subconnections', function () {

        spyOn(sut, 'createSubconnection');

        var routeType = new RouteType({
            value: 'BARGE_RAIL'
        });
        sut.updateRouteType(routeType);
        expect(sut.get('routeType')).toBe(routeType);
        expect(sut.createSubconnection).toHaveBeenCalled();
    });

    it('updates route type to barge-rail with non-empty subconnections', function () {

        sut.get('subconnections').add(new Subconnection());

        spyOn(sut, 'createSubconnection');
        var routeType = new RouteType({
            value: 'BARGE_RAIL'
        });
        sut.updateRouteType(routeType);
        expect(sut.get('routeType')).toBe(routeType);
        expect(sut.createSubconnection).not.toHaveBeenCalled();
    });

    it('creates Subconnection empty list', function () {
        sut.createSubconnection();

        expect(sut.get('subconnections').length).toBe(1);
        expect(sut.get('subconnections').first().get('endpoint1')).toBe(seaport);
        expect(sut.get('subconnections').first().get('endpoint2')).toBe(terminal);
        expect(sut.get('subconnections').first().get('routeType').get('value')).toBe('BARGE');
    });

    it('creates Subconnection non-empty list', function () {
        var t = new ConnectionTerminal();
        sut.createSubconnection();
        sut.get('subconnections').first().set('endpoint2', t);
        sut.createSubconnection();

        expect(sut.get('subconnections').length).toBe(2);
        expect(sut.get('subconnections').last().get('endpoint1')).toBe(t);
        expect(sut.get('subconnections').last().get('endpoint2')).toBe(t);
        expect(sut.get('subconnections').last().get('routeType').get('value')).toBe('RAIL');
    });

    it('creates Subconnection non-empty list but matching endpoints', function () {

        spyOn(window, 'alert');

        sut.createSubconnection();
        sut.createSubconnection();

        expect(sut.get('subconnections').length).toBe(1);

        expect(window.alert).toHaveBeenCalledWith('Connection endpoint and latest subconnection endpoint are the ' +
            'same. Adding more subconnections is not necessary.');
    });
});