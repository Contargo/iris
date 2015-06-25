describe('ConnectionApp', function () {

    var sut;
    var serverMock;
    var connection = {
        "id": 5,
        "seaportId": 1,
        "terminalId": 93,
        "bargeDieselDistance": 0,
        "railDieselDistance": 0,
        "railElectricDistance": 0,
        "routeType": "BARGE_RAIL",
        "subConnections": []
    };

    beforeEach(function() {

        var emptyFunction = function () {
            
        };
        serverMock = {getConnection: emptyFunction, getSeaports: emptyFunction, getTerminals: emptyFunction};
        sut = new ConnectionApp(serverMock, 2);
    });

    it('can be instantiated', function () {
        expect(sut).toBeDefined();
        expect(sut.server).toBe(serverMock);
        expect(sut.connectionId).toBe(2);
    });

    it('start', function () {
        
        window.$ = function () {
            return {html: function (param) {
                expect(param).toBe('bar');
            }};
        };
        
        spyOn(serverMock, 'getConnection').andCallFake(function (id, callback) {
            callback(connection);
        });
        spyOn(serverMock, 'getSeaports').andCallFake(function (callback) {
            callback([1, 2]);
        });
        spyOn(serverMock, 'getTerminals').andCallFake(function (callback) {
            callback([3, 4]);
        });

        spyOn(ConnectionView.prototype, 'create').andCallFake(function (options) {
            expect(options.seaports).toEqual([1, 2]);
            expect(options.terminals).toEqual([3, 4]);
            expect(options.model.get('id')).toEqual(5);
            return {
                el: 'bar'
            };
        });

        sut.start();
    });
});