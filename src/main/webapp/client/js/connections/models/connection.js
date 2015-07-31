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
        if (this.get('subconnections').size() > 0) {
            this.get('subconnections').first().set('endpoint1', updatedValue);
        }
    },

    updateRouteType: function(updatedValue) {
        'use strict';
        var previous = this.get('routeType').get('value');
        this.set('routeType', updatedValue);
        if (updatedValue.get('value') === 'BARGE_RAIL' && this.get('subconnections').size() === 0) {
            this.createSubconnection();
        }
        if (updatedValue.get('value') === 'BARGE_RAIL') {
            this.set('distances', new Distances());
        }
        if (previous === 'BARGE_RAIL') {
            this.get('subconnections').reset();
        }
    },

    createSubconnection: function () {
        'use strict';
        var subconnection;
        if (this.get('subconnections').size() === 0) {
            subconnection = new Subconnection({
                endpoint1: this.get('seaport'),
                endpoint2: this.get('terminal'),
                routeType: new RouteType({value: 'BARGE', name: 'Barge'})
            });
        } else {
            var latest = this.get('subconnections').last();
            if (latest.get('endpoint2').get('uniqueId') === this.get('terminal').get('uniqueId')) {
                alert('Connection endpoint and latest subconnection endpoint are the same. Adding more subconnections is not necessary.');
                return;
            }
            subconnection = new Subconnection({
                endpoint1: latest.get('endpoint2'),
                endpoint2: this.get('terminal'),
                routeType: new RouteType({value: 'RAIL', name: 'Rail'})
            });
        }
        this.get('subconnections').add(subconnection);
    },

    hasValidLastSubConnectionTerminal: function () {
        if (this.get('routeType').get('value') === 'BARGE_RAIL') {
            var lastSubConnection = this.get('subconnections').last();
            return lastSubConnection.get('endpoint2').get('uniqueId') === this.get('terminal').get('uniqueId');
        } else {
            return true;
        }
    }
});