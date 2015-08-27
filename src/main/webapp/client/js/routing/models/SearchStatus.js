var SearchStatus = Backbone.Model.extend({

    defaults: {
        haveSearched: false
    },

    setHaveSearched: function (value) {
        this.set('haveSearched', value);
    },

    haveSearched: function () {
        return this.get('haveSearched');
    }
});