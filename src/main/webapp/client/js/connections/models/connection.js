var Connection = Backbone.Model.extend({
    defaults: {
        id: undefined,
        seaportUid: undefined,
        terminalUid: undefined,
        bargeDieselDistance: undefined,
        railDieselDistance: undefined,
        railElectricDistance: undefined,
        routeType: undefined
    }
});