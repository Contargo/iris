var Connection = Backbone.Model.extend({
    defaults: function () {
        return {
            id: undefined,
            seaport: new ConnectionSeaport(),
            terminal: new ConnectionTerminal(),
            distances: new Distances(),
            routeType: new RouteType(),
            enabled: true,
            subconnections: new Subconnections()
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
        this.get('routeType').set('value', updatedValue);
        if (updatedValue === 'BARGE_RAIL' && this.get('subconnections').size() === 0) {
            this.createSubconnection();
        }
    },

    createSubconnection: function () {
        'use strict';
        var subconnection;
        if (this.get('subconnections').size() === 0) {
            subconnection = new Subconnection({
                endpoint1: this.get('seaport'),
                endpoint2: this.get('terminal'),
                routeType: new RouteType({value: 'BARGE'})
            });
        } else {
            var latest = this.get('subconnections').last();
            if (latest.get('endpoint2').get('uniqueId') === this.get('terminal').get('uniqueId')) {
                alert('Connection endpoint and latest subconnection endpoint are the same. Adding more subconnections is not necessary.');
                return;
            }
            subconnection = new Subconnection({
                endpoint1: latest.get('endpoint2'),
                endpoint2: latest.get('endpoint2'),
                routeType: new RouteType({value: 'RAIL'})
            });
        }
        this.get('subconnections').add(subconnection);
    }
});