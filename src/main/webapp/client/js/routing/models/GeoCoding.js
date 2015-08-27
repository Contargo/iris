var GeoCoding = Backbone.Model.extend({

    defaults: {
        request: undefined,
        results: undefined,
        server: undefined,
        countries: undefined,
        searching: false
    },

    initialize: function (options) {

        _.bindAll(this, 'doSearch', 'updateSelected', 'resetRequest');

        Helper.isDefined(options, 'options');
        Helper.isDefined(options.server, 'server');
        Helper.isDefined(options.countries, 'countries');

        this.server = options.server;
        this.countries = options.countries;

        var geoCodeRequest = new GeoCodeRequest({countries: options.countries});
        var addressListResult = new AddressListList();

        geoCodeRequest.bind('change', this.doSearch);

        addressListResult.bind('change:selectedAddressOfAddressListList', this.updateSelected);
        this.set('request', geoCodeRequest);
        this.set('results', addressListResult);
        geoCodeRequest.bind('reset', this.resetRequest);
    },

    getRequest: function () {
        return this.get('request');
    },

    resetRequest: function () {
        this.get('searchStatus').setHaveSearched(false);
        this.get('request').set({
            street: '',
            postalcode: '',
            city: '',
            country: ''
        });
        this.get('results').reset();
    },

    getResults: function () {
        return this.get('results');
    },

    getSelected: function () {
        return this.get('geoCodingSelectedAddress');
    },

    doSearch: function () {
        var request = this.get('request');

        if (request === undefined || request.isEmpty()) {
            return;
        }

        var self = this;
        self.set('searching', true);
        this.server.geoCode(request, function (addressLists) {
            self.get('searchStatus').setHaveSearched(true);
            self.get('results').reset(addressLists);
            self.set('searching', false);
        });
    },

    updateSelected: function (address) {
        this.trigger('selectedchanged', address);
    }
});
