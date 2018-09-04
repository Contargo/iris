describe('ConnectionMapper', function () {

    var sut;
    var seaports;
    var terminals;

    beforeEach(function() {
        sut = new ConnectionMapper();
        seaports = new ConnectionSeaports(new ConnectionSeaport({
            uniqueId: '1301000000000002',
            name: 'seaport'
        }));
        terminals = new ConnectionTerminals(new ConnectionTerminal({
            uniqueId: '1301000000000001',
            name: 'terminal'
        }));
    });

    it('creates connection from json', function () {
        var json = {
            id: 28,
            bargeDieselDistance: 9,
            railDieselDistance: 0,
            railElectricDistance: 0,
            roadDistance: 3,
            routeType: 'BARGE_RAIL',
            enabled: true,
            seaportUid: '1301000000000002',
            terminalUid: '1301000000000001'
        };


        var connection = sut.connectionFromJson(json, seaports, terminals);

        expect(connection.get('id')).toBe(28);
        expect(connection.get('routeType').get('value')).toBe('BARGE_RAIL');
        expect(connection.get('distances').get('barge')).toBe(9);
        expect(connection.get('distances').get('raildiesel')).toBe(0);
        expect(connection.get('distances').get('railelectric')).toBe(0);
        expect(connection.get('distances').get('road')).toBe(3);
        expect(connection.get('seaport').get('uniqueId')).toBe('1301000000000002');
        expect(connection.get('terminal').get('uniqueId')).toBe('1301000000000001');
    });

    it('converts connection to json', function() {
        var connection = new Connection({
            id: 3,
            enabled: true,
            seaport: new ConnectionSeaport({uniqueId: '4'}),
            terminal: new ConnectionTerminal({uniqueId: '5'}),
            routeType: new RouteType({value: 'BARGE'})
        });

        var json = sut.connectionToJson(connection);
        expect(json).toEqual({
            id: 3,
            enabled: true,
            seaportUid: '4',
            terminalUid: '5',
            bargeDieselDistance: 0,
            railDieselDistance: 0,
            railElectricDistance: 0,
            roadDistance: 0,
            routeType: 'BARGE'
        });
    });
});