var SelectableAwareCollection = Backbone.Collection.extend({

    initialize : function() {

        _.bindAll(this, 'modelSelected', 'getSelected', 'setDefaultToSelected', 'defaultChanged');

        this.bind('change:selected', this.modelSelected);
        this.bind('change:defaultValue', this.defaultChanged);
    },

    getDefault : function() {
        var element = this.find(function(e) {
            return e.get('defaultValue');
        });

        if (element === undefined && this.size() > 0) {
            element = this.at(0);
        }

        return element;
    },

    valuePredicate : function(v, what) {
        var field = what;
        if (! field) {
            field = 'value';
        }
        var value = '' + v;
        return function(e) {
            // otherwise it does not work for booleans
            var stringval = '' +  e.get(field);
            return  value === stringval;
        };
    },

    findByValue : function(value, what) {
        return this.find(this.valuePredicate(value, what));
    },

    setDefaultByValue : function(value, what) {
        var e = this.findByValue(value, what);
        if (e !== undefined) {
            e.set('defaultValue', true);
        }
    },

    setDefaultToSelected : function() {
        var element = this.getDefault();

        if (element !== undefined) {
            element.set('selected', true);
        }
    },

    getSelected : function() {
        return this.find(function(e) {
            return e.get('selected');
        });
    },

    modelSelected : function(e) {
        if (e.get('selected')) {

            this.each(function(m) {
                if (m !== e) {
                    m.set('selected', false);
                }
            });

            this.trigger('selectedchanged', e);
        }
    },

    defaultChanged : function(e) {

        if (e.get('defaultValue')) {

            this.each(function(m) {
                if (m !== e) {
                    m.set('defaultValue', false);
                }
            });

            this.trigger('defaultchanged', e);
        }
    }
});