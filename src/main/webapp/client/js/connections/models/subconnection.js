var Subconnection = Backbone.Model.extend({
    defaults: function() {
        return {
            id: undefined,
            endpoint1: new ConnectionEndpoint(),
            endpoint2: new ConnectionEndpoint(),
            distances: new Distances(),
            routeType: new RouteType()
        };
    },

    createTemplateModel: function() {
        'use strict';
        return {
            id: this.get('id'),
            endpoint1: this.get('endpoint1').toJSON(),
            endpoint2: this.get('endpoint2').toJSON(),
            distances: this.get('distances').toJSON(),
            routeType: this.get('routeType').toJSON()
        }
    }
});

var Subconnections = Backbone.Collection.extend({
    model: Subconnection
});