var TriangleModel = Backbone.Model.extend({

        defaults: {
            geocoding: undefined,
            terminals: undefined,
            seaports: undefined,
            points: undefined

        },

        initialize: function (options) {
            _.bindAll(this, 'update', 'addAddress', 'addTerminal', 'addSeaport');

            Helper.isDefined(options, 'TriangleModel needs options');
            Helper.isDefined(options.server, 'TriangleModel needs options.server');
            this.server = options.server;

            this.set('geocoding', new GeoCoding({server: this.server, countries: options.countries}));
            this.set('points', new TrianglePoints([], {server: this.server}));
            this.get('geocoding').on('selectedchanged', this.addAddress);
            this.get('terminals').on('selectedadded', this.addTerminal);
            this.get('seaports').on('selectedadded', this.addSeaport);

        },

        update: function () {
        },

        addAddress: function (e) {
            var points = this.get('points');
            points.add(new Address(e.toJSON()));

            var geocoding = this.get('geocoding');
            geocoding.resetRequest();
        },

        addTerminal: function (e) {
            var points = this.get('points');
            // we 'kindof clone' this to that it can be added more than once to points
            var t = new Terminal(e.toJSON());
            t.set('id', undefined);
            points.add(t);
        },

        addSeaport: function (e) {
            // copy seaport to list of points
            var points = this.get('points');
            var seaport = new Seaport(e.toJSON());
            seaport.set('id', undefined);
            points.add(seaport);
        }
    }
);