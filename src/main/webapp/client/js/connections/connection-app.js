function ConnectionApp(connectionServer, connectionId, newlyCreated) {
    'use strict';
    this.connectionId = connectionId;
    this.server = connectionServer;
    this.server.errorHandler = this.error;
    this.connection = undefined;
    this.mapper = new ConnectionMapper();
    this.newlyCreated = newlyCreated;
    this.errorSyntaxChecker = new ErrorObjectSyntaxChecker();
    this.validationMessageService = new ConnectionValidationMessageService();
}

ConnectionApp.prototype.start = function () {
    'use strict';

    _.bindAll(this, 'update', 'registerEvents', 'updateTerminal', 'updateSeaport', 'updateRouteType', 'loadModels',
        'createView', 'handleSaveError');

    if (this.newlyCreated) {
        MessageView.prototype.create({message: 'Created connection.'});
        this.newlyCreated = false;
    }

    this.loadModels(this.createView);
};

ConnectionApp.prototype.createView = function () {
    'use strict';
    this.connectionView = ConnectionView.prototype.create({
        model: this.connection,
        seaports: this.seaports,
        terminals: this.terminals,
        routeTypes: this.routeTypes
    });
    this.registerEvents();
    $('#connection').html(this.connectionView.el);
    numberEnforcer();
};

ConnectionApp.prototype.loadModels = function (callback) {
    'use strict';

    var that = this;
    this.server.getSeaports(function (seaports) {
        if (seaports.length === 0) {
            that.handleCriticalError('No seaports available');
            return;
        }
        that.server.getTerminals(function (terminals) {
            if (terminals.length === 0) {
                that.handleCriticalError('No terminals available');
                return;
            }
            that.server.getTypes(function (types) {
                that.seaports = new ConnectionSeaports(_.map(seaports, function (seaport) {
                    return new ConnectionSeaport(seaport);
                }));

                that.terminals = new ConnectionTerminals(_.map(terminals, function (terminal) {
                    return new ConnectionTerminal(terminal);
                }));
                that.routeTypes = new RouteTypes(_.map(types, function (name, value) {
                    return new RouteType({value: value, name: name});
                }));

                if (that.connectionId) {
                    that.server.getConnection(that.connectionId, function (connection) {
                        that.connection = that.mapper.connectionFromJson(connection);
                        callback();
                    }, that.handleCriticalError);
                } else {
                    that.connection = new Connection();
                    callback();
                }
            }, that.handleCriticalError);
        }, that.handleCriticalError);
    }, this.handleCriticalError);
};

ConnectionApp.prototype.registerEvents = function () {
    'use strict';
    this.connection.bind('updateConnection', this.update);
    this.seaports.bind('selectionChange', this.updateSeaport);
    this.terminals.bind('selectionChange', this.updateTerminal);
    this.routeTypes.bind('selectionChange', this.updateRouteType);
};

ConnectionApp.prototype.updateTerminal = function (updatedValue) {
    'use strict';
    this.connection.updateTerminal(updatedValue);
};

ConnectionApp.prototype.updateSeaport = function (updatedValue) {
    'use strict';
    this.connection.updateSeaport(updatedValue);
    this.connectionView.render();
};

ConnectionApp.prototype.updateRouteType = function (updatedValue) {
    'use strict';
    this.connection.updateRouteType(updatedValue);
    this.connectionView.render();
};

ConnectionApp.prototype.update = function () {
    'use strict';
    var that = this;
    if (this.connection.get('id')) {
        this.server.updateConnection(that.mapper.connectionToJson(this.connection), function () {
            MessageView.prototype.create({message: 'Updated connection.'});
            that.loadModels(that.createView);
        }, this.handleSaveError);
    } else {
        this.server.createConnection(that.mapper.connectionToJson(this.connection), function (location) {
            that.redirect(location + '?success=true');
        }, this.handleSaveError);
    }
};

ConnectionApp.prototype.redirect = function (location) {
    'use strict';
    window.location.href = location;
};

ConnectionApp.prototype.handleSaveError = function (data) {
    'use strict';
    var message;
    if (this.errorSyntaxChecker.isValidJSONString(data.responseText)) {
        message = this.validationMessageService.getValidationMessage(data.responseJSON.code, data.responseJSON.message);
    } else {
        message = this.validationMessageService.defaultMessage;
    }
    MessageView.prototype.create({
        message: 'Failed to update connection: ' + message,
        className: 'message message-error message-width'
    });
};

ConnectionApp.prototype.handleCriticalError = function (msg) {
    'use strict';
    $('.notifications').notify({
        type: 'error',
        message: {
            text: msg
        },
        fadeOut: {
            enabled: false
        },
        closable: true
    }).show();
};