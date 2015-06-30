var RouteType = Backbone.Model.extend({

    defaults: {
        value: undefined,
        selected: false
    }
});

var RouteTypes = Backbone.Collection.extend({
    model: RouteType,

    setSelected: function(value) {
        'use strict';
        this.each(function(type) {
            if (type.get('uniqueId') === value) {
                type.set('selected', true);
            }
        });
    }
});