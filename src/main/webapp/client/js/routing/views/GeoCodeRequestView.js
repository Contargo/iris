var GeoCodeRequestView = Backbone.View.extend({

    templateName: 'GeoCodeRequest',

    initialize: function (options) {

        Helper.isDefined(options.searchStatus, 'searchStatus');
        this.searchStatus = options.searchStatus;

        this.template = getTemplate(this.templateName);

        _.bindAll(this, 'render', 'search', 'reset', 'resetForm');

        this.model.bind('change', this.render);
        this.render();
    },

    events: {
        'click button.search': 'search',
        'click button.reset': 'reset',
        'change select.suburbSearch': 'render'
    },

    render: function () {
        var model = this.getModel();
        this.$el.html(this.template(model));

        this.countryView = new BootstrapDropDownView({
            el: this.$('.country-container'),
            model: this.model.get('countries')
        });

        this.$('.info').popover();
    },

    search: function () {
        this.model.set(this.getModel());
        return false;
    },

    reset: function () {
        this.model.trigger('reset');
        this.resetForm();
        return false;
    },

    resetForm: function () {
        $(':input').each(function (index, element) {
            var placeholder = $(this).attr('placeholder');
            if (element.type === 'text') {
                if (!Modernizr.input.placeholder && (placeholder !== '' && placeholder !== undefined)) {
                    $(element).val(placeholder);
                } else {
                    $(element).val('');
                }
            }
        });
    },

    getModel: function () {
        var model = this.model;

        return {
            street: this.extract('.street'),
            city: this.extract('.city'),
            postalcode: this.extract('.postalcode'),
            country: model.get('countries').getSelected().get('value')
        };
    },

    extract: function (selector) {
        var val = this.$(selector).val();
        var placeholder = this.$(selector).attr('placeholder');
        if (val !== placeholder) {
            return val;
        } else {
            return '';
        }
    }
});