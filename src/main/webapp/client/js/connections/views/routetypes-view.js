var RouteTypesView = Backbone.View.extend({

    templateName: 'routetypes',

    events: {
        'change #routeType': 'changeRouteType'
    },
    
    initialize: function (options) {
        'use strict';
        _.bindAll(this, 'render');

        this.template = getTemplate(this.templateName);
        this.render();
    },

    create: function (options) {
        'use strict';
        return new RouteTypesView(options);
    },

    render: function () {
        'use strict';
        this.$el.html(this.template({types: this.model.toJSON()}));
    },

    changeRouteType: function() {
        'use strict';
        this.model.setSelected(this.$('#routeType').val());
    }
});