var DistancesView = Backbone.View.extend({

    templateName: 'distances',

    events: {
        'change #bargeDieselDistance': 'updateBarge',
        'change #railDieselDistance': 'updateRailDiesel',
        'change #railElectricDistance': 'updateRailElectric'
    },

    initialize: function () {
        'use strict';
        _.bindAll(this, 'render');

        this.template = getTemplate(this.templateName);
        this.render();
    },

    create: function (options) {
        'use strict';
        return new DistancesView(options);
    },

    render: function () {
        'use strict';
        this.$el.html(this.template(this.model.toJSON()));
    },

    updateBarge: function(event) {
        'use strict';
        this.update('barge', event.target.value);
    },

    updateRailDiesel: function(event) {
        'use strict';
        this.update('raildiesel', event.target.value);
    },

    updateRailElectric: function(event) {
        'use strict';
        this.update('railelectric', event.target.value);
    },

    update: function(field, value) {
        'use strict';
        this.model.set(field, parseInt(value));
    }
});