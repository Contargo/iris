var TriangleView = Backbone.View.extend({

    templateName: 'TriangleView',
    searchStatus: new SearchStatus(),

    initialize: function (options) {
        Helper.isDefined(options, 'options');
        Helper.isDefined(options.model, 'options.model');
        this.template = getTemplate(this.templateName);

        if (this.model === undefined) {
            throw 'model is undefined';
        }
        _.bindAll(this, 'render');

        this.render();
    },

    render: function () {
        var model = {};
        this.$el.html(this.template(model));

        this.trianglePointsView = new TrianglePointsView({
            el: this.$('.points'),
            model: this.model.get('points')
        });

        var geocodingModel = this.model.get('geocoding');
        geocodingModel.set('searchStatus',this.searchStatus);

        this.geocodingrequestview = new GeoCodingView({
            el: this.$('.geocoding'),
            model: geocodingModel
        });

        this.terminalsView = new TriangleDropDownWithAddButtonView({
            el: this.$('.terminals'),
            model: this.model.get('terminals')
        });

        this.seaportsView = new TriangleDropDownWithAddButtonView({
            el: this.$('.seaports'),
            model: this.model.get('seaports')
        });
    }
});