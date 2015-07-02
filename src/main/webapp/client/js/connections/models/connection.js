var Connection = Backbone.Model.extend({
    defaults: {
        id: undefined,
        seaport: new ConnectionSeaport(),
        terminal: new ConnectionTerminal(),
        distances: new Distances(),
        routeType: new RouteType(),
        enabled: true
    },

    updateTerminal: function(updatedValue) {
        'use strict';
        this.get('terminal').set('uniqueId', updatedValue);
    },

    updateSeaport: function(updatedValue) {
        'use strict';
        this.get('seaport').set('uniqueId', updatedValue);
    },
    
    updateRouteType: function(updatedValue) {
        'use strict';
        this.get('routeType').set('value', updatedValue);
    },

    createJsonModel: function() {
        'use strict';
        return {
            id: this.get('id'),
            seaportUid: this.get('seaport').get('uniqueId'),
            terminalUid: this.get('terminal').get('uniqueId'),
            bargeDieselDistance: this.get('distances').get('barge'),
            railDieselDistance: this.get('distances').get('raildiesel'),
            railElectricDistance: this.get('distances').get('railelectric'),
            routeType: this.get('routeType').get('value'),
            enabled: this.get('enabled'),
            subConnections: []
        };
    },

    update: function(updated) {
        'use strict';
        this.set('id', updated.id);
        this.get('seaport').set('uniqueId', updated.seaportUid);
        this.get('terminal').set('uniqueId', updated.terminalUid);
        this.set('distances', new Distances({
            barge: updated.bargeDieselDistance,
            raildiesel: updated.railDieselDistance,
            railelectric: updated.railElectricDistance
        }));
        this.get('routeType').set('value', updated.routeType);
        this.set('enabled', updated.enabled);
    }
});