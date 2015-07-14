function ConnectionApp(connectionServer, connectionId) {
    'use strict';
    this.connectionId = connectionId;
    this.server = connectionServer;
    this.connection = undefined;
}

ConnectionApp.prototype.start = function () {
    'use strict';

    _.bindAll(this, 'update', 'registerEvents', 'updateTerminal', 'updateSeaport', 'updateRouteType', 'loadModels', 'createView', 'addNewSubconnection');

    var that = this;

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
                        enabled: connection.enabled,
                        subconnections: new Subconnections(_.map(connection.subConnections, function(sub) {
                            return new Subconnection({
                                id: sub.id,
                                distances: new Distances({
                                    barge: sub.bargeDieselDistance,
                                    raildiesel: sub.railDieselDistance,
                                    railelectric: sub.railElectricDistance
                                }),
                                endpoint1: sub.routeType === 'BARGE' ? that.seaports.getByUniqueId(sub.seaportUid) : that.terminals.getByUniqueId(sub.terminalUid),
                                endpoint2: sub.routeType === 'BARGE' ? that.terminals.getByUniqueId(sub.terminalUid) : that.terminals.getByUniqueId(sub.terminal2Uid),
                                routeType: new RouteType({value: sub.routeType})
                            });
                        }))
                    });
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
        this.server.updateConnection(this.connection.createJsonModel(), function (con) {
            that.connection.update(con);
            that.connectionView.render();
        });
    } else {
        this.server.createConnection(this.connection.createJsonModel(), function (location) {
            window.location.href = location;
        });
    }
};

ConnectionApp.prototype.addNewSubconnection = function () {
    'use strict';
    this.connection.createSubconnection();
    this.connectionView.render();
};