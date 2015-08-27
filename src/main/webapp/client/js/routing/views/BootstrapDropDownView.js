var BootstrapDropDownView = Backbone.View.extend({

    initialize: function (options) {
        _.bindAll(this, 'render', 'addOne', 'selectedchanged');

        this.model.bind('add', this.render);
        this.model.bind('remove', this.render);
        this.model.bind('reset', this.render);
        this.model.bind('change', this.render);

        this.outputField = this.getOutputField(options.outputField);

        // the element with defaultValue = true has to be set as selected if there is nothing selected
        if (this.model.getSelected() === undefined) {
            this.model.setDefaultToSelected();
        }

        this.render();
    },

    events: {
        'click .selectLink': 'selectedchanged'
    },

    getOutputField: function (outputField) {
        if (outputField !== undefined && outputField !== null) {
            return outputField;
        } else {
            return 'name';
        }
    },

    render: function () {

        if (this.model.getSelected() === undefined) {
            this.model.setDefaultToSelected();
        }

        this.$el.html('<div class="btn-group"><ul class="dropdown-menu"></ul></div>');
        this.addAll();
    },

    addOne: function (model) {

        if (model.get('invisible')) {
            return;
        }

        if (model === this.model.getSelected()) {
            var element = $('<a class="btn dropdown-toggle" data-toggle="dropdown"></a>').attr('id', model.cid).html(model.get(this.outputField) + '&nbsp;<span class="caret"></span>');
            this.$('div.btn-group').append(element);
        }

        var option = $('<a class="selectLink"></a>').attr('id', model.cid).html(model.get(this.outputField));
        var listElement = $('<li></li>').html(option);
        this.$('ul.dropdown-menu').append(listElement);
    },

    addAll: function () {
        this.model.forEach(this.addOne);
    },

    selectedchanged: function (element) {

        var cid = element.target.id;
        var selectedElement = this.model.get(cid);
        var dropdownToggle;

        this.$('a.dropdown-toggle').remove();
        if (this.title !== undefined && this.title !== null) {
            dropdownToggle = $('<a class="btn dropdown-toggle" data-toggle="dropdown"></a>').html(this.title + '&nbsp;<span class="caret"></span>');
        } else {
            dropdownToggle = $('<a class="btn dropdown-toggle" data-toggle="dropdown"></a>').attr('id', cid).html(selectedElement.get(this.outputField) + '&nbsp;<span class="caret"></span>');
        }
        this.$('div.btn-group').append(dropdownToggle);

        this.changeSelectStrategy(selectedElement);
    },

    changeSelectStrategy: function (selectedElement) {
        selectedElement.set('selected', true);
    }
});