function ConnectionApp(connectionServer, connectionId, newlyCreated) {
    'use strict';
    this.connectionId = connectionId;
    this.server = connectionServer;
    this.server.errorHandler = this.error;
    this.connection = undefined;
    this.mapper = new ConnectionMapper();
    this.newlyCreated = newlyCreated;
}

ConnectionApp.prototype.start = function () {
    'use strict';

    _.bindAll(this, 'update', 'registerEvents', 'updateTerminal', 'updateSeaport', 'updateRouteType', 'loadModels', 'createView', 'addNewSubconnection');

    if (this.newlyCreated) {
        MessageView.prototype.create({message: "Created connection."});
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
        that.server.getTerminals(function (terminals) {
            that.seaports = new ConnectionSeaports(_.map(seaports, function(seaport) {
                return new ConnectionSeaport(seaport);
            }));
            that.terminals = new ConnectionTerminals(_.map(terminals, function(terminal) {
                return new ConnectionTerminal(terminal);
            }));
            that.routeTypes = new RouteTypes([new RouteType({value: 'BARGE'}), new RouteType({value: 'RAIL'}),
                new RouteType({value: 'BARGE_RAIL'})]);
            if (that.connectionId) {
                that.server.getConnection(that.connectionId, function (connection) {
                    that.connection = that.mapper.connectionFromJson(connection, that.seaports, that.terminals);
                    callback();
                });
            } else {
                that.connection = new Connection();
                callback();
            }
        });
    });
};

ConnectionApp.prototype.registerEvents = function () {
    'use strict';
    this.connection.bind('updateConnection', this.update);
    this.seaports.bind('selectionChange', this.updateSeaport);
    this.terminals.bind('selectionChange', this.updateTerminal);
    this.routeTypes.bind('selectionChange', this.updateRouteType);
    this.connection.get('subconnections').bind('addNew', this.addNewSubconnection);
};

ConnectionApp.prototype.updateTerminal = function (updatedValue) {
    'use strict';
    this.connection.updateTerminal(updatedValue);
};

ConnectionApp.prototype.updateSeaport = function (updatedValue) {
    'use strict';
    this.connection.updateSeaport(updatedValue);
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
            MessageView.prototype.create({message: "Updated connection."});
            that.loadModels(that.createView);
        }, function () {
            MessageView.prototype.create({message: "Failed to update connection.", className: "message message-error message-width"});
        });
    } else {
        this.server.createConnection(that.mapper.connectionToJson(this.connection), function (location) {
            that.redirect(location + '?success=true');
        }, function () {
            MessageView.prototype.create({message: "Failed to create connection.", className: "message message-error message-width"});
        });
    }
};

ConnectionApp.prototype.redirect = function (location) {
    window.location.href = location;
};

ConnectionApp.prototype.addNewSubconnection = function () {
    'use strict';
    this.connection.createSubconnection();
    this.connectionView.render();
};