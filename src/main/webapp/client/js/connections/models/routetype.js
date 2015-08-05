var RouteType = Backbone.Model.extend({

    defaults: {
        value: undefined,
        name: undefined,
        selected: false
    }
});

var RouteTypes = Backbone.Collection.extend({
    model: RouteType,

    setSelected: function(value, silent) {
        'use strict';
        if (value) {
            this.each(function (type) {
                type.set('selected', type.get('value') === value);
            });
        } else {
            this.first().set('selected', true);
            value = this.first().get('value');
        }
        if (!silent) {
            this.trigger('selectionChange', this.getSelected());
        }
    },

    getSelected: function () {
        'use strict';
        return this.find(function(type) {
            return type.get('selected');
        });
    }
});