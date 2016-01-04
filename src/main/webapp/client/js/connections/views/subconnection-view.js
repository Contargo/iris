var SubconnectionView = Backbone.View.extend({

    templateName: 'subconnection',

    events: {
        'change .barge': 'updateBarge',
        'change .raildiesel': 'updateRailDiesel',
        'change .railelectric': 'updateRailElectric',
        'change .end': 'updateEndpoint2',
        'click .new-subconnection': 'addNewSubconnection',
        'click .remove-subconnection': 'removeSubconnection'
    },

    initialize: function (options) {
        'use strict';
        _.bindAll(this, 'render');

        this.terminals = options.terminals;
        this.terminals.setSelected(this.model.get('endpoint2').get('uniqueId'));
        this.latest = options.latest;
        this.first = options.first;

        this.template = getTemplate(this.templateName);
        this.render();
    },

    create: function (options) {
        'use strict';
        return new SubconnectionView(options);
    },

    render: function () {
        'use strict';
        var model = {
            model: this.createTemplateModel(this.model),
            terminals: this.terminals.toJSON(),
            latest: this.latest,
            latestAndNotFirst: this.latest && !this.first,
            first: this.first,
            isBarge: this.model.get('routeType').get('value') === 'BARGE',
            isRail: this.model.get('routeType').get('value') === 'RAIL'
        };
        this.$el.html(this.template(model));
    },

    updateBarge: function (event) {
        'use strict';
        this.model.get('distances').set('barge', parseInt(event.target.value));
    },

    updateRailDiesel: function (event) {
        'use strict';
        this.model.get('distances').set('raildiesel', parseInt(event.target.value));
    },

    updateRailElectric: function (event) {
        'use strict';
        this.model.get('distances').set('railelectric', parseInt(event.target.value));
    },

    updateEndpoint2: function (event) {
        'use strict';
        this.terminals.setSelected(event.target.value);
        this.model.set('endpoint2', this.terminals.getSelected());
    },

    addNewSubconnection: function () {
        'use strict';
        this.model.collection.trigger('addNew');
    },

    removeSubconnection: function () {
        'use strict';
        this.model.collection.trigger('removeLast');
    },

    createTemplateModel: function (model) {
        'use strict';
        return {
            id: model.get('id'),
            endpoint1: model.get('endpoint1').toJSON(),
            endpoint2: model.get('endpoint2').toJSON(),
            distances: model.get('distances').toJSON(),
            routeType: model.get('routeType').toJSON()
        };
    }
});