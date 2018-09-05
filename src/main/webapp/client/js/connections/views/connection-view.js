var ConnectionView = Backbone.View.extend({

    templateName: 'connection',
    tagName: 'div',
    className: 'connection-form',

    events: {
        'click #submit-button': 'submitConnection',
        'change #enabled': 'updateEnabled'
    },

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

        this.seaports.setSelected(this.model.get('seaport').get('uniqueId'), true);
        this.model.set('seaport', this.seaports.getSelected());
        this.terminals.setSelected(this.model.get('terminal').get('uniqueId'), true);
        this.model.set('terminal', this.terminals.getSelected());
        this.routeTypes.setSelected(this.model.get('routeType').get('value'), true);
        this.model.set('routeType', this.routeTypes.getSelected());

        this.$el.html(this.template({
            header: this.createHeader(),
            submitText: this.createSubmitText(),
            enabled: this.model.get('enabled')
        }));
        this.createChildren();
        numberEnforcer();
    },

    createChildren: function () {
        'use strict';
        SeaportsView.prototype.create({
            model: this.seaports,
            el: this.$('#seaports')
        });

        TerminalsView.prototype.create({
            model: this.terminals,
            el: this.$('#terminals')
        });

        RouteTypesView.prototype.create({
            model: this.routeTypes,
            el: this.$('#routeTypes')
        });

        DistancesView.prototype.create({
            model: this.model.get('distances'),
            el: this.$('#distances'),
            isBarge: this.model.get('routeType').get('value') === 'BARGE',
            isRail: this.model.get('routeType').get('value') === 'RAIL',
            isDtruck: this.model.get('routeType').get('value') === 'DTRUCK',
            isDtruckAvailable:  this.routeTypes.find(function (rt) {
                return rt.get('value') === 'DTRUCK';
            }) !== undefined
        });
    },

    createHeader: function () {
        'use strict';
        var header;
        if (this.model.get('id')) {
            header = 'Edit main run connection - Seaport ';
            header += this.seaports.getSelectedName();
            header += ' to Terminal ';
            header += this.terminals.getSelectedName();
        } else {
            header = 'Create new main run connection';
        }
        return header;
    },

    createSubmitText: function () {
        'use strict';
        if (this.model.get('id')) {
            return 'Update';
        } else {
            return 'Create';
        }
    },

    updateEnabled: function (event) {
        'use strict';
        this.model.set('enabled', $(event.target).is(':checked'));
    },

    submitConnection: function () {
        'use strict';
        if (this.hasFormError()) {
            MessageView.prototype.create({
                message: 'Cannot create or update connection: validation errors.',
                className: 'message message-error message-width'
            });
        } else {
            this.model.trigger('updateConnection');
        }
    },

    hasFormError: function () {
        return this.$('input.error').size() > 0;
    }

});
