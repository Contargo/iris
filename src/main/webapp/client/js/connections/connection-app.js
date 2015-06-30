function ConnectionApp(connectionServer, connectionId) {
    'use strict';
    this.connectionId = connectionId;
    this.server = connectionServer;
    this.connection = undefined;
}

ConnectionApp.prototype.start = function () {
    'use strict';

    _.bindAll(this, 'update', 'registerEvents', 'updateTerminal', 'updateSeaport', 'updateRouteType', 'loadModels');

    var that = this;
    this.loadModels(function () {
        that.connectionView = ConnectionView.prototype.create({
            model: that.connection,
            seaports: that.seaports,
            terminals: that.terminals,
            routeTypes: that.routeTypes
        });
        $('#connection').html(that.connectionView.el);
        that.registerEvents();
    });
};

ConnectionApp.prototype.loadModels = function (callback) {
    'use strict';

    var that = this;
    this.server.getConnection(this.connectionId, function (connection) {
        that.server.getSeaports(function (seaports) {
            that.server.getTerminals(function (terminals) {
                that.connection = new Connection({
                    id: connection.id,
                    seaport: new ConnectionSeaport({uniqueId: connection.seaportUid}),
                    terminal: new ConnectionTerminal({uniqueId: connection.terminalUid}),
                    distances: new Distances({
                        barge: connection.bargeDieselDistance,
                        raildiesel: connection.railDieselDistance,
                        railelectric: connection.railElectricDistance
                    }),
                    routeType: new RouteType({value: connection.routeType}),
                    enabled: connection.enabled
                });
                that.seaports = new ConnectionSeaports(_.map(seaports, function(seaport) {
                    return new ConnectionSeaport(seaport);
                }));
                that.terminals = new ConnectionTerminals(_.map(terminals, function(terminal) {
                    return new ConnectionTerminal(terminal);
                }));
                that.routeTypes = new RouteTypes([new RouteType({value: 'BARGE'}), new RouteType({value: 'RAIL'}),
                    new RouteType({value: 'BARGE_RAIL'})]);
                callback();
            });
        });
    });
};

ConnectionApp.prototype.registerEvents = function () {
    this.connection.bind('updateConnection', this.update);
    this.seaports.bind('selectionChange', this.updateSeaport);
    this.terminals.bind('selectionChange', this.updateTerminal);
    this.routeTypes.bind('selectionChange', this.updateRouteType);
};

ConnectionApp.prototype.updateTerminal = function (updatedValue) {
    this.connection.updateTerminal(updatedValue);
};

ConnectionApp.prototype.updateSeaport = function (updatedValue) {
    this.connection.updateSeaport(updatedValue);
};

ConnectionApp.prototype.updateRouteType = function (updatedValue) {
    this.connection.updateRouteType(updatedValue);
};

ConnectionApp.prototype.update = function () {
    var that = this;
    this.server.updateConnection(this.connection.createJsonModel(), function(con) {
        console.log('updated connection', con);
        that.connection.update(con);
        that.connectionView.render();
    });
};