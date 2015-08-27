var TemplateManager = Backbone.Model.extend({

    defaults: {
        prefix: 'templates/',
        postfix: '.html'
    },

    initialize: function () {
        this.set('cache', {});
    },

    getUrl: function (name) {
        return this.get('prefix') + name + this.get('postfix');
    },

    loadTemplate: function (url) {
        var result = 'Problem loading the Template from url ' + url;

        $.ajax({
            url: url,
            context: this,
            async: false,
            dataType: 'html',
            cache: false,

            success: function (data) {
                result = data;
            },
            error: function (jqXHR, textStatus) {
                throw 'Could not load template ' + url + ': ' + textStatus;
            }

        });
        return this.getHandleBarsTemplateFromResult(result);
    },

    getHandleBarsTemplateFromResult: function (result) {
        return Handlebars.compile(result);
    },

    getTemplate: function (name) {
        var url = this.getUrl(name);
        var template = this.get('cache')[url];
        if (template === undefined) {
            template = this.loadTemplate(url);
            this.get('cache')[url] = template;
        }
        return template;
    }
});

function exportTemplateManagerAsGlobalFunction(prefix) {

    // define global method getTemplate
    window.getTemplate = function () {
        var templateManager = new TemplateManager({
            prefix: prefix
        });
        return function (name) {
            return templateManager.getTemplate(name);
        };
    }();
}