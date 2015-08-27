var Helper = {

    renderCorrectGeolocationView: function (model, el) {
        if (!model) {
            el.html('<span title="?"></span>');
        }
        else if (model.get('type') === 'SEAPORT') {
            return new SeaportView({el: el, model: model});
        } else if (model.get('type') === 'TERMINAL') {
            return new TerminalView({el: el, model: model});
        }
        // GEOLOCATION AND ADDRESS
        else {
            // if we have no nicename but coordinates...
            if (model.get('niceName') === undefined && model.get('latitude')) {
                model.set('niceName', model.get('latitude').toFixed(2) + ':' + model.get('longitude').toFixed(2));
            }
            return new DetailledAddressView({el: el, model: model});
        }
    },

    isDefined: function (name, what) {

        if (name === undefined) {
            throw 'need ' + what + ' defined .';
        }

        return name;
    },
    formatKM: function (value) {
        if (value !== undefined && value !== null) {
            value = value.toFixed(0) + ' km';
        }
        return value;

    },

    formatDurationInDetail: function (duration) {
        // given duration: min
        // to be returned: days, hours (e.g. 02ds 06.03hrs)
        if (duration !== undefined && duration !== null) {
            var days = Math.floor(duration / (60 * 24));
            var hours = Math.floor((duration % (60 * 24)) / 60);
            var minutes = Math.floor((duration % (60 * 24)) % 60);

            var formattedDuration = '';

            if (days > 0) {
                formattedDuration += this.setFirstCharToZeroIfSingleCharDuration(days) + 'ds ';
            }
            formattedDuration += this.setFirstCharToZeroIfSingleCharDuration(hours) + '.' + this.setFirstCharToZeroIfSingleCharDuration(minutes) + 'hrs';

            return formattedDuration;
        } else {
            return '-';
        }
    },

    setFirstCharToZeroIfSingleCharDuration: function (time) {
        if (time !== undefined) {

            if (time < 10) {
                time = '0' + time;
            }
            return time;
        } else {
            return '-';
        }
    },

    setPopoverOnClickCloseEvent: function (that) {
        var self = this;
        that.$('.info').click(function () {
            self.closeAllPopover();
        });
    },

    closeAllPopover: function () {
        $('.popover').remove();
    }
};