var TriangleRoutingApp = Backbone.Model.extend({

    defaults: {
        contextpath: '/'
    },
    initialize: function () {
        // initializing
        this.server = new Server(this.get('contextpath'), this.error);
        this.countries = new SelectableAwareCollection([
            {name: 'Not loaded yet', value: 'Not loaded yet'}
        ]);
        this.router = new TriangleRoutingRouter({
            app: this
        });
    },

    start: function () {

        // the order of this methods must be in this specific order, PLEASE DO NOT CHANGE
        this.queryCountries();
        this.loadTerminals();
        this.loadSeaports();

        Backbone.history.start({
            pushState: false,
            root: '/'
        });
    },

    loadTerminals: function () {

        var terminals = new TerminalList();
        this.terminals = terminals;
        this.server.terminals(function (data) {
            terminals.add(data);
        });
        terminals.setDefaultValue();
    },

    loadSeaports: function () {

        var seaports = new SeaportList();
        this.seaports = seaports;

        this.server.getActiveSeaports(function (data) {
            seaports.add(data);
        });
        seaports.setDefaultValue();
    },

    showRequest: function (forcenew) {

        if ((!this.triangleModel) || forcenew) {

            this.triangleModel = new TriangleModel({
                terminals: this.terminals,
                seaports: this.seaports,
                server: this.server,
                countries: this.countries
            });

            this.triangleView = new TriangleView({
                el: '#appcontent',
                model: this.triangleModel
            });
        }

        console.log('Currently ' + this.triangleModel.get('points').length + ' points in triangle.');
    },

    queryCountries: function () {
        var countries = this.countries;

        this.server.countries(function (countryList) {
            countries.reset(countryList);
        });

        countries.setDefaultByValue('DE');
    },

    /**
     * boolean permanent, if the notify window will stay and not fadeOut by itself
     */
    error: function (msg, permanent) {
        var fadeOut = true;
        var closable = true;
        if (permanent !== undefined) {
            fadeOut = false;
            closable = true;
        }

        // for the ones that aren't closable and don't fade out there is a .close() function.
        $('.notifications').notify({
            type: 'error',
            message: {
                text: msg
            },
            fadeOut: {
                enabled: fadeOut
            },
            closable: closable
        }).show();
    }
});