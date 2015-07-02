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
        if (value) {
            this.each(function (type) {
                type.set('selected', type.get('value') === value);
            });
            this.trigger('selectionChange', value);
        } else {
            this.first().set('selected', true);
            this.trigger('selectionChange', this.first().get('value'));
        }
    }
});