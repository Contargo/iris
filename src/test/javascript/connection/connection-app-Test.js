describe('ConnectionApp', function () {

    var sut;
    var serverMock;
    var connection = {
        "id": 5,
        "seaportUid": 1,
        "terminalUid": 93,
        "bargeDieselDistance": 0,
        "railDieselDistance": 0,
        "railElectricDistance": 0,
        "routeType": "BARGE_RAIL",
        "subConnections": []
    };

    beforeEach(function() {

        serverMock = jasmine.createSpyObj('serverMock', ['getConnection', 'getSeaports', 'getTerminals', 'createConnection', 'updateConnection']);
        sut = new ConnectionApp(serverMock, 2);

        serverMock.getConnection.andCallFake(function (id, callback) {
            callback(connection);
        });
        serverMock.getSeaports.andCallFake(function (callback) {
            callback([1, 2]);
        });
        serverMock.getTerminals.andCallFake(function (callback) {
            callback([3, 4]);
        });

        spyOn(ConnectionView.prototype, 'create').andCallFake(function (options) {
            expect(options.seaports.length).toEqual(2);
            expect(options.terminals.length).toEqual(2);
            expect(options.model.get('id')).toEqual(5);
            return {
                el: 'bar',
                render: function() {}
            };
        });
    });

    it('can be instantiated', function () {
        expect(sut).toBeDefined();
        expect(sut.server).toBe(serverMock);
        expect(sut.connectionId).toBe(2);
        expect(sut.mapper).toBeDefined();
    });

    it('start', function () {
        var jQuery = window.$;
        window.$ = function () {
            return {html: function (param) {
                expect(param).toBe('bar');
            }};
        };

        sut.start();
        window.$ = jQuery;
    });

    it('registers update seaport event', function () {
        sut.start();
        spyOn(sut.connection, 'updateSeaport');
        sut.seaports.trigger('selectionChange', 'updatedSeaport');
        
        expect(sut.connection.updateSeaport).toHaveBeenCalledWith('updatedSeaport');
    });

    it('registers update terminal event', function () {
        sut.start();
        spyOn(sut.connection, 'updateTerminal');
        sut.terminals.trigger('selectionChange', 'updatedTerminal');

        expect(sut.connection.updateTerminal).toHaveBeenCalledWith('updatedTerminal');
    });

    it('registers update route type event', function () {
        sut.start();
        spyOn(sut.connection, 'updateRouteType');
        spyOn(sut.connectionView, 'render');
        sut.routeTypes.trigger('selectionChange', 'updatedRouteType');

        expect(sut.connection.updateRouteType).toHaveBeenCalledWith('updatedRouteType');
        expect(sut.connectionView.render).toHaveBeenCalled();
    });

    it('adds subconnections', function () {
        sut.start();
        spyOn(sut.connection, 'createSubconnection');
        spyOn(sut.connectionView, 'render');
        sut.connection.get('subconnections').trigger('addNew');

        expect(sut.connection.createSubconnection).toHaveBeenCalledWith();
        expect(sut.connectionView.render).toHaveBeenCalled();
    });

    it('creates new connection', function () {
        sut.start();
        sut.connection.unset('id');
        spyOn(sut.mapper, 'connectionToJson').andReturn({foo: 'bar'});
        spyOn(sut, 'redirect');
        serverMock.createConnection.andCallFake(function (connection, callback) {
            callback('location');
        });

        sut.connection.trigger('updateConnection');

        expect(serverMock.createConnection).toHaveBeenCalledWith({foo: 'bar'}, jasmine.any(Function));
        expect(sut.redirect).toHaveBeenCalledWith('location');
    });

    it('updates connection', function () {
        sut.start();
        spyOn(sut.mapper, 'connectionToJson').andReturn({foo: 'bar'});
        spyOn(sut, 'loadModels');
        serverMock.updateConnection.andCallFake(function (connection, callback) {
            callback();
        });

        sut.connection.trigger('updateConnection');

        expect(serverMock.updateConnection).toHaveBeenCalledWith({foo: 'bar'}, jasmine.any(Function));
        expect(sut.loadModels).toHaveBeenCalledWith(sut.createView);
    });
});