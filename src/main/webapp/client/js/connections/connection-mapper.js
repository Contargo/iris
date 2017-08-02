function ConnectionMapper() {
}

ConnectionMapper.prototype.getRouteTypeName = function (value) {
    'use strict';
    var map = {BARGE: 'Barge', RAIL: 'Rail'};
    return map[value];
};

ConnectionMapper.prototype.connectionFromJson = function (connection, seaports, terminals) {
    'use strict';
    var that = this;
    return new Connection({
        id: connection.id,
        seaport: new ConnectionSeaport({uniqueId: connection.seaportUid}),
        terminal: new ConnectionTerminal({uniqueId: connection.terminalUid}),
        distances: new Distances({
            barge: connection.bargeDieselDistance,
            raildiesel: connection.railDieselDistance,
            railelectric: connection.railElectricDistance,
            road: connection.roadDistance
        }),
        routeType: new RouteType({value: connection.routeType}),
        enabled: connection.enabled,
        subconnections: new Subconnections(_.map(connection.subConnections, function (sub) {
            return that.subconnectionFromJson(sub, seaports, terminals);
        }))
    });
};

ConnectionMapper.prototype.subconnectionFromJson = function (subconnection, seaports, terminals) {
    'use strict';
    return new Subconnection({
        id: subconnection.id,
        distances: new Distances({
            barge: subconnection.bargeDieselDistance,
            raildiesel: subconnection.railDieselDistance,
            railelectric: subconnection.railElectricDistance
        }),
        endpoint1: subconnection.routeType === 'BARGE' ? seaports.getByUniqueId(subconnection.seaportUid) : terminals.getByUniqueId(subconnection.terminalUid),
        endpoint2: subconnection.routeType === 'BARGE' ? terminals.getByUniqueId(subconnection.terminalUid) : terminals.getByUniqueId(subconnection.terminal2Uid),
        routeType: new RouteType({value: subconnection.routeType, name: this.getRouteTypeName(subconnection.routeType)})
    });
};

ConnectionMapper.prototype.connectionToJson = function (connection) {
    'use strict';
    return {
        id: connection.get('id'),
        seaportUid: connection.get('seaport').get('uniqueId'),
        terminalUid: connection.get('terminal').get('uniqueId'),
        bargeDieselDistance: connection.get('distances').get('barge'),
        railDieselDistance: connection.get('distances').get('raildiesel'),
        railElectricDistance: connection.get('distances').get('railelectric'),
        roadDistance: connection.get('distances').get('road'),
        routeType: connection.get('routeType').get('value'),
        enabled: connection.get('enabled'),
        subConnections: this.subconnectionsToJson(connection.get('subconnections'))
    };
};

ConnectionMapper.prototype.subconnectionsToJson = function (subconnections) {
    'use strict';
    var that = this;
    return subconnections.map(function (subconnection) {
        return that.subconnectionToJson(subconnection);
    });
};

ConnectionMapper.prototype.subconnectionToJson = function (subconnection) {
    'use strict';
    var json = {
        id: subconnection.get('id'),
        routeType: subconnection.get('routeType').get('value'),
        bargeDieselDistance: subconnection.get('distances').get('barge'),
        railDieselDistance: subconnection.get('distances').get('raildiesel'),
        railElectricDistance: subconnection.get('distances').get('railelectric')
    };
    if (subconnection.get('routeType').get('value') === 'BARGE') {
        json.seaportUid = subconnection.get('endpoint1').get('uniqueId');
        json.terminalUid = subconnection.get('endpoint2').get('uniqueId');
    } else {
        json.terminalUid = subconnection.get('endpoint1').get('uniqueId');
        json.terminal2Uid = subconnection.get('endpoint2').get('uniqueId');
    }
    return json;
};
