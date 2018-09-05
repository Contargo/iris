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
        "routeType": "BARGE_RAIL"
    };
    var callback;

    beforeEach(function() {

        serverMock = jasmine.createSpyObj('serverMock', ['getConnection', 'getSeaports', 'getTerminals', 'createConnection', 'updateConnection', 'getTypes']);
        sut = new ConnectionApp(serverMock, 2);

        serverMock.getConnection.and.callFake(function (id, callback) {
            callback(connection);
        });
        serverMock.getSeaports.and.callFake(function (callback) {
            callback([1, 2]);
        });
        serverMock.getTerminals.and.callFake(function (callback) {
            callback([3, 4]);
        });
        serverMock.getTypes.and.callFake(function (callback) {
            callback({'BARGE': 'Barge', 'RAIL': 'Rail', 'BARGE_RAIL': 'Barge-Rail'});
        });

        spyOn(MessageView.prototype, 'create');

        spyOn(ConnectionView.prototype, 'create').and.callFake(function (options) {
            expect(options.seaports.length).toEqual(2);
            expect(options.terminals.length).toEqual(2);
            expect(options.model.get('id')).toEqual(5);
            return {
                el: 'bar',
                render: function() {}
            };
        });
        callback = jasmine.createSpy();
        spyOn(sut, 'handleCriticalError');
    });

    it('can be instantiated', function () {
        expect(sut).toBeDefined();
        expect(sut.server).toBe(serverMock);
        expect(sut.connectionId).toBe(2);
        expect(sut.mapper).toBeDefined();
    });

    it('start create', function () {
        sut = new ConnectionApp(serverMock, 2, true);

        spyOn(sut.__proto__, 'loadModels');

        sut.start();

        expect(MessageView.prototype.create).toHaveBeenCalledWith({message: "Created connection."});
        expect(sut.__proto__.loadModels).toHaveBeenCalledWith(sut.createView);
    });

    it('start update', function () {
        sut = new ConnectionApp(serverMock, 2);

        spyOn(sut.__proto__, 'loadModels');

        sut.start();

        expect(MessageView.prototype.create).not.toHaveBeenCalledWith();
        expect(sut.__proto__.loadModels).toHaveBeenCalledWith(sut.createView);
    });

    it('loadModels', function () {
        sut.loadModels(callback);

        expect(callback).toHaveBeenCalled();
        expect(sut.seaports.length).toBe(2);
        expect(sut.terminals.length).toBe(2);
        expect(sut.routeTypes.length).toBe(3);
        expect(sut.connection.get('id')).toBe(5);
    });

    it('loadModels empty seaports', function () {

        serverMock.getSeaports.and.callFake(function (callback) {
            callback([]);
        });

        sut.loadModels(callback);

        expect(sut.handleCriticalError).toHaveBeenCalledWith('No seaports available');
        expect(callback).not.toHaveBeenCalled();

    });

    it('loadModels with seaport loading error', function () {

        serverMock.getSeaports.and.callFake(function (callback, errorCallback) {
            errorCallback('errorCallback');
        });

        sut.loadModels();

        expect(sut.handleCriticalError).toHaveBeenCalledWith('errorCallback');
        expect(callback).not.toHaveBeenCalled();
    });

    it('loadModels empty terminals', function () {

        serverMock.getTerminals.and.callFake(function (callback) {
            callback([]);
        });

        sut.loadModels();

        expect(sut.handleCriticalError).toHaveBeenCalledWith('No terminals available');
        expect(callback).not.toHaveBeenCalled();
    });

    it('loadModels with terminal loading error', function () {

        serverMock.getTerminals.and.callFake(function (callback, errorCallback) {
            errorCallback('errorCallback');
        });

        sut.loadModels();

        expect(sut.handleCriticalError).toHaveBeenCalledWith('errorCallback');
        expect(callback).not.toHaveBeenCalled();
    });

    it('loadModels with types loading error', function () {

        serverMock.getTypes.and.callFake(function (callback, errorCallback) {
            errorCallback('errorCallback');
        });

        sut.loadModels();

        expect(sut.handleCriticalError).toHaveBeenCalledWith('errorCallback');
        expect(callback).not.toHaveBeenCalled();
    });


    it('loadModels without connectionId', function () {
        sut.connectionId = undefined;
        sut.loadModels(callback);

        expect(callback).toHaveBeenCalled();
        expect(sut.seaports.length).toBe(2);
        expect(sut.terminals.length).toBe(2);
        expect(sut.routeTypes.length).toBe(3);
        expect(sut.connection.get('id')).toBe(undefined);
    });

    it('loadModels with connection loading error', function () {

        serverMock.getConnection.and.callFake(function (id, callback, errorCallback) {
            errorCallback('errorCallback');
        });

        sut.loadModels(callback);

        expect(callback).not.toHaveBeenCalled();
        expect(sut.handleCriticalError).toHaveBeenCalledWith('errorCallback');
    });

    it('registers update seaport event', function () {
        sut.start();
        spyOn(sut.connection, 'updateSeaport');
        spyOn(sut.connectionView, 'render');

        sut.seaports.trigger('selectionChange', 'updatedSeaport');

        expect(sut.connection.updateSeaport).toHaveBeenCalledWith('updatedSeaport');
        expect(sut.connectionView.render).toHaveBeenCalled();
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

    it('creates new connection', function () {
        sut.start();
        sut.connection.unset('id');
        spyOn(sut.mapper, 'connectionToJson').and.returnValue({foo: 'bar'});
        spyOn(sut, 'redirect');
        serverMock.createConnection.and.callFake(function (connection, callback) {
            callback('location');
        });

        sut.connection.trigger('updateConnection');

        expect(serverMock.createConnection).toHaveBeenCalledWith({foo: 'bar'}, jasmine.any(Function), jasmine.any(Function));
        expect(sut.redirect).toHaveBeenCalledWith('location?success=true');
        expect(MessageView.prototype.create).not.toHaveBeenCalled();
    });

    it('creates new connection with error', function () {
        sut.start();
        sut.connection.unset('id');
        spyOn(sut.mapper, 'connectionToJson').and.returnValue({foo: 'bar'});
        spyOn(sut, 'redirect');
        spyOn(sut, 'handleSaveError');
        serverMock.createConnection.and.callFake(function (connection, callback, errorCallback) {
            errorCallback('errorResponse');
        });

        sut.connection.trigger('updateConnection');

        expect(serverMock.createConnection).toHaveBeenCalledWith({foo: 'bar'}, jasmine.any(Function), jasmine.any(Function));
        expect(sut.redirect).not.toHaveBeenCalled();
        expect(sut.handleSaveError).toHaveBeenCalledWith('errorResponse');
    });

    it('updates connection', function () {
        sut.start();
        spyOn(sut.mapper, 'connectionToJson').and.returnValue({foo: 'bar'});
        spyOn(sut, 'loadModels');
        serverMock.updateConnection.and.callFake(function (connection, callback, errorcallback) {
            callback();
        });

        sut.connection.trigger('updateConnection');

        expect(serverMock.updateConnection).toHaveBeenCalledWith({foo: 'bar'}, jasmine.any(Function), jasmine.any(Function));
        expect(sut.loadModels).toHaveBeenCalledWith(sut.createView);
        expect(MessageView.prototype.create).toHaveBeenCalledWith({message: "Updated connection."});
    });

    it('updates connection with error', function () {
        sut.start();
        spyOn(sut.mapper, 'connectionToJson').and.returnValue({foo: 'bar'});
        spyOn(sut, 'loadModels');
        spyOn(sut, 'handleSaveError');
        serverMock.updateConnection.and.callFake(function (connection, callback, errorcallback) {
            errorcallback('errorResponse');
        });

        sut.connection.trigger('updateConnection');

        expect(serverMock.updateConnection).toHaveBeenCalledWith({foo: 'bar'}, jasmine.any(Function), jasmine.any(Function));
        expect(sut.loadModels).not.toHaveBeenCalled();
        expect(sut.handleSaveError).toHaveBeenCalledWith('errorResponse');
    });

    it('handles save error', function () {
        spyOn(sut.errorSyntaxChecker, 'isValidJSONString').and.returnValue(true);
        spyOn(sut.validationMessageService, 'getValidationMessage').and.returnValue('wasd');

        sut.handleSaveError({
            responseText: 'abc',
            responseJSON: {code: 'def', message: 'foobar'}
        });

        expect(sut.errorSyntaxChecker.isValidJSONString).toHaveBeenCalledWith('abc');
        expect(sut.validationMessageService.getValidationMessage).toHaveBeenCalledWith('def', 'foobar');
        expect(MessageView.prototype.create).toHaveBeenCalledWith({message: "Failed to update connection: wasd", className: "message message-error message-width"});
    });

    it('handles save error with invalid error object', function () {
        spyOn(sut.errorSyntaxChecker, 'isValidJSONString').and.returnValue(false);
        spyOn(sut.validationMessageService, 'getValidationMessage');

        sut.handleSaveError({
            responseText: 'abc',
            responseJSON: {code: 'def', message: 'foobar'}
        });

        expect(sut.errorSyntaxChecker.isValidJSONString).toHaveBeenCalledWith('abc');
        expect(sut.validationMessageService.getValidationMessage).not.toHaveBeenCalled();
        expect(MessageView.prototype.create).toHaveBeenCalledWith({message: "Failed to update connection: unspecified error", className: "message message-error message-width"});
    });
});