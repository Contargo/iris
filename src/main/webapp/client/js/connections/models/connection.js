var Connection = Backbone.Model.extend({
    defaults: function () {
        return {
            id: undefined,
            seaport: new ConnectionSeaport(),
            terminal: new ConnectionTerminal(),
            distances: new Distances(),
            routeType: new RouteType(),
            enabled: true
        };
    },

    updateTerminal: function(updatedValue) {
        'use strict';
        this.set('terminal', updatedValue);
    },

    updateSeaport: function(updatedValue) {
        'use strict';
        this.set('seaport', updatedValue);
    },

    updateRouteType: function(updatedValue) {
        'use strict';
        this.set('routeType', updatedValue);
    }
});