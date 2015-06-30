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
        this.each(function(connectionEndpoint) {
            connectionEndpoint.set('selected', connectionEndpoint.get('uniqueId') === value);
        });
        this.trigger('selectionChange', value)
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
    }
});