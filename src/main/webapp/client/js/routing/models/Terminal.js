var Terminal = Backbone.Model.extend({

    defaults: {
        id: undefined,
        name: undefined
    }
});

var TerminalList = Backbone.Collection.extend({
    model: Terminal,

    setDefaultValue : function(){
        if(this.size() > 0){
            this.first().set('defaultValue', 'true');
        }
    },

    comparator : function (model) {
        return model.get('name');
    },
    getByName : function (name) {
        var selected;
        this.forEach( function(terminal){
            if(terminal.get('name') === name){
                selected = terminal;
                return;
            }
        });
        return selected;
    }
});