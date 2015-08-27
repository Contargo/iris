var TriangleRoutingRouter = Backbone.Router.extend({

    initialize: function (options) {
        this.app = options.app;
    },

    routes: {
        '*default': 'request'
    },

    request: function () {
        console.log('ROUTER: request');
        this.app.showRequest();

        if (window.adjustHelpLink) {
            adjustHelpLink('trianglerouting');
        }
    }
});