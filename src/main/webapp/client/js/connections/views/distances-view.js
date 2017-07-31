var DistancesView = Backbone.View.extend({

    templateName: 'distances',

    events: {
        'change #bargeDieselDistance': 'updateBarge',
        'change #railDieselDistance': 'updateRailDiesel',
        'change #railElectricDistance': 'updateRailElectric',
        'change #roadDistance': 'updateRoad'
    },

    initialize: function (options) {
        'use strict';
        _.bindAll(this, 'render');

        this.isBarge = options.isBarge;
        this.isRail = options.isRail;
        this.isDtruck = options.isDtruck;
        this.isDtruckAvailable = options.isDtruckAvailable;
        this.template = getTemplate(this.templateName);
        this.render();
    },

    create: function (options) {
        'use strict';
        return new DistancesView(options);
    },

    render: function () {
        'use strict';
        var model = this.model.toJSON();
        model.isRail = this.isRail;
        model.isBarge = this.isBarge;
        model.isDtruck = this.isDtruck;
        model.isDtruckAvailable = this.isDtruckAvailable;
        this.$el.html(this.template(model));
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

    updateRoad: function(event) {
        'use strict';
        this.update('road', event.target.value);
    },

    update: function(field, value) {
        'use strict';
        this.model.set(field, parseInt(value));
    }
});