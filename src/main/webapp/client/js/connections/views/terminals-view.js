var TerminalsView = Backbone.View.extend({

    templateName: 'terminals',

    events: {
        'change #terminal': 'changeTerminal'
    },

    initialize: function () {
        'use strict';
        _.bindAll(this, 'render');

        this.template = getTemplate(this.templateName);
        this.render();
    },

    create: function (options) {
        'use strict';
        return new TerminalsView(options);
    },

    render: function () {
        'use strict';
        var model = this.model.toJSON();
        model.forEach(function (element) {
            element.nicename = element.name + (element.enabled ? ' (enabled)' : ' (not enabled)');
        });
        this.$el.html(this.template({terminals: model}));
    },

    changeTerminal: function (event) {
        'use strict';
        this.model.setSelected(event.target.value);
    }
});