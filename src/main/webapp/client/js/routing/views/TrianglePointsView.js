var TrianglePointsView = Backbone.View.extend({

    templateName : 'TrianglePoints',
    events : {
        'click .clear' : 'clear'
    },

    initialize : function(options) {

        Helper.isDefined(options, 'options');
        Helper.isDefined(options.model, 'options.model');

        if (options.childView) {
            this.childView = options.childView;
        } else {
            this.childView = TriangleRoutePartView;
        }
        this.template = getTemplate(this.templateName);

        if (this.model === undefined) {
            throw 'model is undefined';
        }

        _.bindAll(this, 'render', 'renderRoutingPoint', 'clear', 'renderTotals');
        this.model.bind('add', this.render);
        this.model.bind('remove', this.render);
        this.model.bind('reset', this.render);

        this.model.totals.on('change', this.renderTotals);
        this.render();
    },

    renderTotals: function() {

        var childView = new TriangleRouteTotalView({model : this.model.totals});
        this.$('.totals').empty();
        this.$('.totals').append(childView.$el);
    },

    render: function() {
        this.$el.html(this.template({}));

        var points = this.model.getRoutePairs();

        if (points.size() > 0) {
            points.each(this.renderRoutingPoint);
        }
        this.renderTotals();
    },

    renderRoutingPoint : function(child) {
        var childView = new this.childView({model : child});
        this.$('.children').append(childView.$el);
    },

    clear : function() {
        this.model.reset([]);
        return false;
    }
});