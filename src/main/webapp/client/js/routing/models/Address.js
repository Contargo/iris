var Address = Backbone.Model.extend({

    defaults: {
        latitude: 0,
        longitude: 0,
        displayName: 'unknown Place',
        selected: false,
        expanded: false
    },

    initialize: function () {
        _.bindAll(this, 'updateName');

        this.on('change', this.updateName);
    },

    updateName: function () {
        var name = this.get('niceName');

        if (!name) {
            name = this.get('latitude') + ':' + this.get('longitude');
        }
        this.set('niceName', name);
    }
});

var AddressList = Backbone.Collection.extend({

    model: Address,

    initialize: function () {
        _.bindAll(this, 'addressSelected');
        this.bind('change:selected', this.addressSelected);
    },

    hasSelected: function () {
        return this.find(function (e) {
                return e.get('selected');
            }) !== undefined;
    },

    addressSelected: function (e) {
        // trigger change event (if not triggered manually, only a generic 'change' event without reference to member is set off)
        this.trigger('change:selectedAddressOfAddressList', e);
    }
});

var AddressListModel = Backbone.Model.extend({

    defaults: {
        addresses: undefined,
        name: ''
    },

    initialize: function (options) {
        _.bindAll(this, 'addressSelected');

        this.set('name', options.name);

        var addresses = new AddressList(options.addresses);
        addresses.bind('change:selectedAddressOfAddressList', this.addressSelected);
        this.set('addresses', addresses);
    },

    addressSelected: function (e) {
        this.trigger('change:selectedAddressOfAddressListModel', e);
    }
});

var AddressListList = Backbone.Collection.extend({

    model: AddressListModel,

    defaults: {
        selectedAddressOfAddressList: undefined
    },

    initialize: function () {
        _.bindAll(this, 'addressSelected');
        this.bind('change:selectedAddressOfAddressListModel', this.addressSelected);
    },

    addressSelected: function (e) {
        // only handle when a new address was selected
        if (e.get('selected')) {
            this.each(function (addrList) {
                addrList.get('addresses').each(function (addr) {
                    if (addr !== e) {
                        addr.set('selected', false);
                    }
                });
            });

            // set selected address variable
            this.selectedAddressOfAddressList = e;

        } else {
            delete this.selectedAddressOfAddressList;

        }
        // trigger change event (if not triggered manually, only a generic 'change' event without reference to member is set off)
        this.trigger('change:selectedAddressOfAddressListList', this.selectedAddressOfAddressList);
    }
});
