var ConnectionView = Backbone.View.extend({

    templateName: 'connection',
    tagName: 'div',
    className: 'connection-form',

    initialize: function (options) {
        'use strict';

        this.seaports = options.seaports;
        this.terminals = options.terminals;
        this.routeTypes = options.routeTypes;

        _.bindAll(this, 'render');

        this.template = getTemplate(this.templateName);
        this.render();
    },

    create: function (options) {
        'use strict';
        return new ConnectionView(options);
    },

    render: function () {
        'use strict';

        this.setSelected(this.seaports, 'uniqueId',this.model.get('seaportUid'));
        this.setSelected(this.terminals, 'uniqueId',this.model.get('terminalUid'));
        this.setSelected(this.routeTypes, 'name',this.model.get('routeType'));

        this.$el.html(this.template({
            connection: this.model.toJSON(),
            seaports: this.seaports,
            terminals: this.terminals,
            routeTypes: this.routeTypes
        }));
    },

    setSelected: function (elements, key, value) {
        elements.forEach(function (element) {
            if (element[key] === value) {
                element.selected = true;
            }
        });
    }
});
