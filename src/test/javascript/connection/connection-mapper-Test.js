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

    it('creates barge subconnection from json', function () {
        var json = {
            id: 20,
            terminalUid: '1301000000000001',
            bargeDieselDistance: 200,
            railDieselDistance: 0,
            railElectricDistance: 0,
            seaportUid: '1301000000000002',
            routeType: 'BARGE'
        };
        var subconnection = sut.subconnectionFromJson(json, seaports, terminals);

        expect(subconnection.get('id')).toBe(20);
        expect(subconnection.get('routeType').get('value')).toBe('BARGE');
        expect(subconnection.get('routeType').get('name')).toBe('Barge');
        expect(subconnection.get('distances').get('barge')).toBe(200);
        expect(subconnection.get('distances').get('raildiesel')).toBe(0);
        expect(subconnection.get('distances').get('railelectric')).toBe(0);
        expect(subconnection.get('endpoint1').get('name')).toBe('seaport');
        expect(subconnection.get('endpoint2').get('name')).toBe('terminal');
    });

    it('creates rail subconnection from json', function () {
        var json = {
            id: 20,
            terminalUid: '1301000000000001',
            terminal2Uid: '1301000000000001',
            bargeDieselDistance: 0,
            railDieselDistance: 50,
            railElectricDistance: 40,
            routeType: 'RAIL'
        };
        var subconnection = sut.subconnectionFromJson(json, seaports, terminals);

        expect(subconnection.get('id')).toBe(20);
        expect(subconnection.get('routeType').get('value')).toBe('RAIL');
        expect(subconnection.get('routeType').get('name')).toBe('Rail');
        expect(subconnection.get('distances').get('barge')).toBe(0);
        expect(subconnection.get('distances').get('raildiesel')).toBe(50);
        expect(subconnection.get('distances').get('railelectric')).toBe(40);
        expect(subconnection.get('endpoint1').get('name')).toBe('terminal');
        expect(subconnection.get('endpoint2').get('name')).toBe('terminal');
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
            subConnections:[{id: 3}],
            seaportUid: '1301000000000002',
            terminalUid: '1301000000000001'
        };

        spyOn(sut, 'subconnectionFromJson');

        var connection = sut.connectionFromJson(json, seaports, terminals);

        expect(connection.get('id')).toBe(28);
        expect(connection.get('routeType').get('value')).toBe('BARGE_RAIL');
        expect(connection.get('distances').get('barge')).toBe(9);
        expect(connection.get('distances').get('raildiesel')).toBe(0);
        expect(connection.get('distances').get('railelectric')).toBe(0);
        expect(connection.get('distances').get('road')).toBe(3);
        expect(connection.get('seaport').get('uniqueId')).toBe('1301000000000002');
        expect(connection.get('terminal').get('uniqueId')).toBe('1301000000000001');
        expect(sut.subconnectionFromJson).toHaveBeenCalledWith({id: 3}, seaports, terminals);
    });

    it('converts subconnections to json', function() {
        var subconnections = new Subconnections(new Subconnection());

        spyOn(sut, 'subconnectionToJson').and.returnValue({id: 3});

        var json = sut.subconnectionsToJson(subconnections);
        expect(json).toEqual([{ id: 3 }]);
    });

    it('converts barge subconnection to json', function() {
        var subconnection = new Subconnection({
            id: 3,
            routeType: new RouteType({value: 'BARGE'}),
            endpoint1: new ConnectionSeaport({uniqueId: '4'}),
            endpoint2: new ConnectionTerminal({uniqueId: '5'})
        });

        var json = sut.subconnectionToJson(subconnection);
        expect(json).toEqual({
            id: 3,
            routeType: 'BARGE',
            seaportUid: '4',
            terminalUid: '5',
            bargeDieselDistance: 0,
            railDieselDistance: 0,
            railElectricDistance: 0
        });
    });

    it('converts rail subconnection to json', function() {
        var subconnection = new Subconnection({
            id: 3,
            routeType: new RouteType({value: 'RAIL'}),
            endpoint1: new ConnectionSeaport({uniqueId: '4'}),
            endpoint2: new ConnectionTerminal({uniqueId: '5'})
        });

        var json = sut.subconnectionToJson(subconnection);
        expect(json).toEqual({
            id: 3,
            routeType: 'RAIL',
            terminalUid: '4',
            terminal2Uid: '5',
            bargeDieselDistance: 0,
            railDieselDistance: 0,
            railElectricDistance: 0
        });
    });

    it('converts connection to json', function() {
        var connection = new Connection({
            id: 3,
            enabled: true,
            seaport: new ConnectionSeaport({uniqueId: '4'}),
            terminal: new ConnectionTerminal({uniqueId: '5'}),
            routeType: new RouteType({value: 'BARGE'}),
            subconnection: new Subconnections()
        });

        spyOn(sut, 'subconnectionsToJson').and.returnValue('subconnections');

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
            routeType: 'BARGE',
            subConnections: 'subconnections'
        });

        expect(sut.subconnectionsToJson).toHaveBeenCalled();
    });
});