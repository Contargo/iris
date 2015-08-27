var MessageView = Backbone.View.extend({

    tagName:'div',
    id:'page-message',
    className: 'message message-success message-width',

    initialize: function(options){
        'use strict';
        this.$el.html(options.message);
        $('#page-message-container').html(this.el);
    },

    create: function (options) {
        'use strict';
        return new MessageView(options);
    }
});
