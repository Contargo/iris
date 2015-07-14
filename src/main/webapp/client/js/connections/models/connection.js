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
            subConnections: this.get('subconnections').createJsonModel()
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