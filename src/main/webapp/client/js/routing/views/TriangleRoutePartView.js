var TriangleRoutePartView = Backbone.View.extend({

    templateName : "TriangleRoutePart",
    tagName: "tr",
    events : {
        "click .up" : "moveup",
        "click .down" : "movedown",
        "click .remove" : "remove"
    },

    initialize : function(options) {

        Helper.isDefined(options, "options");
        Helper.isDefined(options.model, "options.model");

        this.template = getTemplate(this.templateName);

        if (this.model === undefined) {
            throw "model is undefined";
        }

        _.bindAll(this, "render", "moveup", "movedown", "remove");
        this.model.bind("change", this.render);

        this.render();

    },


    render: function() {

        var model = this.model.toJSON();
        this.$el.html(this.template(model));

        var from = Helper.renderCorrectGeolocationView(this.model.get("from"), this.$(".from"));
        var to = Helper.renderCorrectGeolocationView(this.model.get("to"),this.$(".to"));

    },
    remove : function() {
        this.model.destroy();
        return false;
    },
    moveup : function() {
        this.model.trigger("up", this.model);
        return false;
    },
    movedown : function() {
        this.model.trigger("down", this.model);
        return false;
    }
});
