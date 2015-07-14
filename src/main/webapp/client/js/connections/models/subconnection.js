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
    },

    createJsonModel: function() {
        'use strict';
        var model =  {
            id: this.get('id'),
            routeType: this.get('routeType').get('value'),
            bargeDieselDistance: this.get('distances').get('barge'),
            railDieselDistance: this.get('distances').get('raildiesel'),
            railElectricDistance: this.get('distances').get('railelectric')
        };
        if (this.get('routeType').get('value') === 'BARGE') {
            model.seaportUid = this.get('endpoint1').get('uniqueId');
            model.terminalUid = this.get('endpoint2').get('uniqueId');
        } else {
            model.terminalUid = this.get('endpoint1').get('uniqueId');
            model.terminal2Uid = this.get('endpoint2').get('uniqueId');
        }
        return model;
    }
});

var Subconnections = Backbone.Collection.extend({
    model: Subconnection,

    createJsonModel: function() {
        'use strict';
        return this.map(function(subcon) {
            return subcon.createJsonModel();
        });
    }
});