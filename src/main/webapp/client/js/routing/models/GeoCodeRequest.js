var GeoCodeRequest = Backbone.Model.extend({

    defaults: {
        street : '',
        postalcode :  '',
        city : '',
        country : ''
    },

    initialize: function(options) {
        _.bindAll(this, 'getParam', 'getQueryString', 'isEmpty');
        this.set('countries', options.countries);

        if(this.get('countries').getSelected() === undefined) {
            this.get('countries').setDefaultToSelected();
        }
    },

    getParam : function(param) {
      var val = this.get(param);
      if (val !== undefined && (val.length > 0)) {
          return param + '=' + val+ '&';
      } else {
          return '';
      }
    },

    getQueryString : function() {
        return encodeURI(this.getParam('street') + this.getParam('postalcode') + this.getParam('city') + this.getParam('country'));
    },

    isEmpty : function() {
        return ((this.getParam('street') === '') && (this.getParam('postalcode') === '') && (this.getParam('city') === '') && (this.getParam('country') === ''));
    }

});
