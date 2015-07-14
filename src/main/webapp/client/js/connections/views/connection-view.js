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
    },

    createChildren: function() {
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

        if (this.model.get('routeType').get('value') === 'BARGE_RAIL') {
            this.createSubconnections();
        } else {
            DistancesView.prototype.create({
                model: this.model.get('distances'),
                el: this.$('#distances')
            });
        }
    },

    createSubconnections: function() {
        'use strict';
        this.$('#subconnections').empty();
        var that = this;
        this.model.get('subconnections').each(function(subconnection, index) {
            var subView = SubconnectionView.prototype.create({
                model: subconnection,
                terminals: new ConnectionTerminals(that.terminals.map(function(terminal) {
                    return terminal.clone();
                })),
                latest: that.model.get('subconnections').size() -1 === index
            });
            that.$('#subconnections').append(subView.el);
        });
    },

    createHeader: function() {
        'use strict';
        var header;
        if (this.model.get("id")) {
            header = 'Edit main run connection - Seaport ';
            header += this.seaports.getSelectedName();
            header += " to Terminal ";
            header += this.terminals.getSelectedName();
        } else {
            header = 'Create new main run connection';
        }
        return header;
    },

    createSubmitText: function() {
        'use strict';
        if (this.model.get("id")) {
            return 'Update';
        } else {
            return 'Create';
        }
    },

    updateEnabled: function(event) {
        'use strict';
        this.model.set('enabled', $(event.target).is(':checked'));
    },

    submitConnection: function() {
        'use strict';
        this.model.trigger('updateConnection');
    },

    updateDistanceDisplayForRouteType: function(routeType) {
        if (routeType === 'BARGE_RAIL') {
            this.$('#distances').hide();
        } else {
            this.$('#distances').show();
        }
    }
});
