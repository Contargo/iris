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

    setSelected: function(value) {
        'use strict';
        if (value) {
            this.each(function (connectionEndpoint) {
                connectionEndpoint.set('selected', connectionEndpoint.get('uniqueId') === value);
            });
            this.trigger('selectionChange', value);
        } else {
            this.first().set('selected', true);
            this.trigger('selectionChange', this.first().get('uniqueId'));
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