var TriangleDropDownWithAddButtonView = Backbone.View.extend({
    templateName : "TriangleDropDownWithAddButtonView",
    events : {
        "click .add" : "addclicked"
    },
    initialize : function(options) {
        _.bindAll(this, "render", "saveselected", "addclicked" );

        Helper.isDefined(options, "options");
        Helper.isDefined(this.model, "model");

        this.template = getTemplate(this.templateName);

        this.model.bind("selectedchanged", this.saveselected);

        this.render();
    },

    render : function() {

        var model = {};
        this.$el.html(this.template(model));

        var node = this.$(".autoComplete");
        var dropdown = new TriangularTrafficDropDownView({el: node, model: this.model});
    },

    saveselected : function(e) {
        this.current = e;
    },
    addclicked : function() {
        if (this.current) {
            this.model.trigger("selectedadded", this.current);
        }
        return false;
    }
});


var TriangularTrafficDropDownView = Backbone.View.extend({

    initialize : function(options) {

        _.bindAll(this, "render", "addOne", "selectedchanged");
        this.model.bind("add", this.render);
        this.model.bind("remove", this.render);
        this.model.bind("reset", this.render);
        this.model.bind("change", this.render);

        var elementToSelect;
        if (options !== undefined && options.selectedText !== undefined) {
            this.model.each(function (element) {
                if (element.get("name") === options.selectedText) {
                    elementToSelect = element;

                }
            });
        }

        if (this.model.size() > 0) {
            elementToSelect = this.model.first();
            elementToSelect.set("defaultValue", true);

            this.model.trigger("selectedchanged", elementToSelect);
        }

        this.render();
    },
    render: function() {
        this.$el.empty();
        var model = this.model.toJSON();
        this.addAll();
        this.$el.select2({ width: 'resolve'});
        var that = this;
        this.$el.on('change', function(e){
            that.selectedchanged(e);
        });

    },
    addOne: function(model) {
        if(model.get("invisible")){
            return;
        }
        var option = $("<option/>").html(model.get("name"));
        if(model.get("defaultValue")){
            option.attr("selected", "selected");
        }
        this.$el.append(option);

    },
    addAll : function() {
        this.model.forEach(this.addOne);
    },
    selectedchanged : function(element) {
        var elementName = element.val;
        var selectedElement = this.model.getByName(elementName);
        this.model.trigger("selectedchanged", selectedElement);
    }
});


