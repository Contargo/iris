function ConnectionApp(connectionServer, connectionId) {
    'use strict';
    this.connectionId = connectionId;
    this.server = connectionServer;
    this.connection = undefined;
}

ConnectionApp.prototype.start = function () {
    'use strict';

    var that = this;
    this.loadModels(function () {
        var connectionView = ConnectionView.prototype.create({
            model: that.connection,
            seaports: that.seaports,
            terminals: that.terminals,
            routeTypes: that.routeTypes
        });
        $('#connection').html(connectionView.el);
    });
};

ConnectionApp.prototype.loadModels = function (callback) {
    'use strict';

    var that = this;
    this.server.getConnection(this.connectionId, function (connection) {
        that.server.getSeaports(function (seaports) {
            that.server.getTerminals(function (terminals) {
                that.connection = new Connection(connection);
                that.seaports = seaports;
                that.terminals = terminals;
                that.routeTypes = [{name: 'BARGE'}, {name: 'RAIL'}, {name: 'BARGE_RAIL'}];
                callback();
            });
        });
    });
};