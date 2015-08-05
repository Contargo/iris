var Subconnection = Backbone.Model.extend({
    defaults: function() {
        return {
            id: undefined,
            endpoint1: new ConnectionEndpoint(),
            endpoint2: new ConnectionEndpoint(),
            distances: new Distances(),
            routeType: new RouteType()
        };
    }
});

var Subconnections = Backbone.Collection.extend({
    model: Subconnection
});