function ConnectionMapper() {
}

ConnectionMapper.prototype.getRouteTypeName = function (value) {
    'use strict';
    var map = {BARGE: 'Barge', RAIL: 'Rail'};
    return map[value];
};

ConnectionMapper.prototype.connectionFromJson = function (connection) {
    'use strict';
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
        enabled: connection.enabled
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
        enabled: connection.get('enabled')
    };
};
