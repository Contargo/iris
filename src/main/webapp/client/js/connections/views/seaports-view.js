var SeaportsView = Backbone.View.extend({

    templateName: 'seaports',

    events: {
        'change #seaport': 'changeSeaport'
    },

    initialize: function () {
        'use strict';
        _.bindAll(this, 'render');

        this.template = getTemplate(this.templateName);
        this.render();
    },

    create: function (options) {
        'use strict';
        return new SeaportsView(options);
    },

    render: function () {
        'use strict';
        var model = this.model.toJSON();
        model.forEach(function (element) {
            element.nicename = element.name + (element.enabled ? ' (enabled)' : ' (not enabled)');
        });
        this.$el.html(this.template({seaports: model}));
    },

    changeSeaport: function (event) {
        'use strict';
        this.model.setSelected(event.target.value);
    }
});