var GeoCodingView = Backbone.View.extend({

    tagName: 'div',
    className: 'span8',

    initialize: function (options) {

        Helper.isDefined(options, 'options');
        Helper.isDefined(options.model, 'options.model');

        this.searchStatus = options.model.get('searchStatus');

        _.bindAll(this, 'render', 'handleSearchChanged');

        this.model.bind('change:searching', this.handleSearchChanged);

        this.render();
    },

    render: function () {

        this.requestNode = $('<div class="geocoderequest"></div>');
        this.listNode = $('<div class="addresslist"></div>');
        this.infoNode = $('<div class="searching">Searching...</div>');

        this.geocodingrequestview = new GeoCodeRequestView({
            el: this.requestNode,
            model: this.model.getRequest(),
            searchStatus: this.searchStatus
        });

        this.addresslist = new AddressCategoryView({
            el: this.listNode,
            model: this.model.getResults(),
            searchStatus: this.searchStatus
        });

        this.$el.empty();
        this.$el.append(this.requestNode);
        this.$el.append(this.infoNode);
        this.$el.append(this.listNode);

        this.handleSearchChanged();
    },

    handleSearchChanged: function () {

        if (this.model.get('searching')) {
            this.infoNode.show();
            this.listNode.hide();
        } else {
            this.listNode.show();
            this.infoNode.hide();
        }
    }
});