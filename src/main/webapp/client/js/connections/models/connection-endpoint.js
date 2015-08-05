var ConnectionEndpoint = Backbone.Model.extend({

    defaults: {
        uniqueId: undefined,
        name: undefined,
        selected: false,
        enabled: false
    }
});

var ConnectionEndpoints = Backbone.Collection.extend({
    model: ConnectionEndpoint,

    setSelected: function(value, silent) {
        'use strict';
        if (value) {
            this.each(function (connectionEndpoint) {
                connectionEndpoint.set('selected', connectionEndpoint.get('uniqueId') === value);
            });
        } else {
            this.first().set('selected', true);
            value = this.first().get('uniqueId');
        }
        if (!silent) {
            this.trigger('selectionChange', this.getSelected());
        }
    },

    getSelectedName: function () {
        'use strict';
        return this.getSelected().get('name');
    },

    getSelected: function () {
        'use strict';
        return this.find(function(connectionEndpoint) {
            return connectionEndpoint.get('selected');
        });
    },

    getByUniqueId: function (id) {
        'use strict';
        return this.find(function(endpoint) {
            return endpoint.get('uniqueId') === id;
        });
    }
});