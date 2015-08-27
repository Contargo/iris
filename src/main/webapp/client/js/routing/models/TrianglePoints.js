var TrianglePoints = Backbone.Collection.extend({

    initialize : function(models, options) {

        _.bindAll(this, 'removeitem', 'moveItemUp', 'moveItemDown', 'routeArrived');

        Helper.isDefined(options, 'options');
        Helper.isDefined(options.server, 'server');

        this.server = options.server;

        this.pairs = new TriangleRoutePartList();
        this.pairs.bind('remove', this.removeitem);
        this.pairs.bind('up', this.moveItemUp);
        this.pairs.bind('down', this.moveItemDown);

        this.totals = new Backbone.Model({totalDistance: 0, totalToll: 0});

    },

    removeitem: function(route) {

        var to = route.get('to');
        this.remove(to);
    },

    moveItemUp : function(item) {
        var to = item.get('to');
        var oldpos = this.getPosition(to);

        this.switchPos(to, oldpos, oldpos-1);
    },

    moveItemDown : function(item) {
        var to = item.get('to');
        var oldpos = this.getPosition(to);
        this.switchPos(to, oldpos, oldpos+1);
    },

    switchPos : function(item, oldpos, newpos) {
        if (newpos < this.size() && newpos >=0) {
            var elements = this.toArray();
            var other = elements[newpos];
            elements[newpos] = item;
            elements[oldpos] = other;
            this.reset(elements);
        }
    },

    getPosition : function(item) {
        for (var i = 0; i < this.size(); i++) {
            if (this.at(i) === item) {
                return i;
            }
        }
        throw 'item not found';
    },

    getRoutePairs : function() {
        this.pairs.reset([]);

        var that = this;
        this.each(function(e,i){

            var from = undefined;
            // skip the from of the first entry
            if (i > 0) {
                from = that.at(i-1);
            }

            // all others are 'the one before me to me'
            that.pairs.add(new TriangleRoutePart({from : from, to: e}));
        });

        this.totals.set({
            distance : undefined,
            toll : undefined  ,
            size: this.size(),
            duration: undefined
        });

        this.server.getRouteDetails(this.pairs, this.routeArrived);

        return this.pairs;
    },

    routeArrived : function(route) {
        this.totals.set({
            distance : Helper.formatKM(route.data.totalDistance),
            toll : Helper.formatKM(route.data.totalTollDistance)  ,
            duration : Helper.formatDurationInDetail(route.data.totalDuration),
            size: this.size()
        });

        var parts = route.data.parts;

        this.pairs.each(  function(pair,index) {

            if (index > 0) {
                var part = parts[index-1];
                var distance = Helper.formatKM(part.data.distance);
                var toll = Helper.formatKM(part.data.tollDistance);
                var duration  = Helper.formatDurationInDetail(part.data.duration);

                pair.set({distance: distance, toll: toll, duration: duration});
            }
        });
    }
});