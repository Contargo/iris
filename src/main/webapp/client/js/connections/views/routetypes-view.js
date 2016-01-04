var RouteTypesView = Backbone.View.extend({

    templateName: 'routetypes',

    events: {
        'change #routeType': 'changeRouteType'
    },

    initialize: function () {
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

    changeRouteType: function (event) {
        'use strict';
        this.model.setSelected(event.target.value);
    }
});