var TriangleRouteTotalView = Backbone.View.extend({

    templateName : 'TriangleRouteTotal',
    tagName: 'tr',

    initialize : function(options) {
        Helper.isDefined(options, 'options');
        Helper.isDefined(options.model, 'options.model');

        this.template = getTemplate(this.templateName);

        if (this.model === undefined) {
            throw 'model is undefined';
        }

        _.bindAll(this, 'render');
        this.model.bind('change', this.render);
        this.render();
    },

    render: function() {
        var model = this.model.toJSON();
        this.$el.html(this.template(model));
    }
});