var Seaport = Backbone.Model.extend({

    defaults: {
        name: 'unknown',
        possibleTypes: undefined,
        invisible: false
    },

    initialize: function () {
        if (this.get('possibleTypes') === undefined) {
            this.set('possibleTypes', []);
        }
    },

    canAcceptTransport: function (transport) {
        // does any of the types support the transport in question?
        return _.any(this.get('possibleTypes'), function (type) {
            return type === transport;
        });
    },

    isInvisible: function () {
        return this.get('invisible');
    }
});

var SeaportList = SelectableAwareCollection.extend({

    model: Seaport,

    filterByPossibleType: function (type) {
        return new SeaportList(this.filter(function (e) {
            return e.canAcceptTransport(type);
        }));
    },

    setVisibility: function (type) {
        this.each(function (e) {
            var visible = e.canAcceptTransport(type);
            e.set('invisible', !visible);
        });

        this.getDefault().set('defaultValue', true);
    },

    modelSelected: function () {
        // override the deselection of other items since seaportlist allows multiple elements to be selected
    },

    setDefaultValue: function () {
        if (this.size() > 0) {
            this.first().set('defaultValue', 'true');
        }
    },

    comparator: function (model) {
        return model.get('name');
    },

    getByName: function (name) {
        var selected;
        this.forEach(function (seaport) {
            if (seaport.get('name') === name) {
                selected = seaport;
            }
        });

        return selected;
    }
});
