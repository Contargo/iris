
var SeaportView = Backbone.View.extend({
    tagName: 'span',

    templateName : 'Seaport',

    initialize : function(options) {
        _.bindAll(this, 'render');

        Helper.isDefined(options, 'options');
        Helper.isDefined(this.model, 'model');

        this.template = getTemplate(this.templateName);

        this.render();
    },

    render: function() {
        var model = this.model.toJSON();
        this.$el.html(this.template(model));

        this.$('.info').popover();
    }
});
