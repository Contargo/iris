var Server = function (contextpath, errorhandler) {

    if (contextpath === undefined) {
        contextpath = '../';
    }

    var api = contextpath + 'api/';
    var countriesUrl = api + 'countries';
    var geoCodeUrl = api + 'geocodes/';
    var seaPortsUrl = api + 'seaports';
    var terminalsUrl = api + 'terminals';

    var reportError = errorhandler;
    if (reportError === undefined) {
        throw 'need errorhandler';
    }

    var thrownPermanentFailurs = {};

    var req = function (url, callback, what, async, permanentFailureMessage, errorFunction) {
        var mode = true;
        if (async !== undefined) {
            mode = async;
        }

        $.ajax({
            url: url,
            success: callback,
            context: this,
            dataType: 'json',
            async: mode,
            cache: false,
            error: function (jqXHR) {
                if (permanentFailureMessage === undefined) {
                    if (!what) {
                        what = 'Abfrage des Servers';
                    }
                    reportError('Bei ' + what + ' ist ein Fehler aufgetreten.');
                }
                else if (thrownPermanentFailurs[jqXHR.status] === undefined) {
                    reportError(permanentFailureMessage, true);
                    thrownPermanentFailurs[jqXHR.status] = jqXHR;
                }
                if (typeof  errorFunction === 'function') {
                    errorFunction();
                }
            }
        });
    };

    return {
        generateDetailsUrl: function (parts) {
            var containerType = 'TWENTY_LIGHT';
            var routeType = 'TRUCK';
            var containerStatus = 'FULL';

            var query = 'routedetails?';

            var i = 0;
            parts.each(function (e) {
                if (e.get('from')) {
                    query += 'data.parts[' + i + '].origin.latitude=' + e.get('from').get('latitude') + '&';
                    query += 'data.parts[' + i + '].origin.longitude=' + e.get('from').get('longitude') + '&';
                    query += 'data.parts[' + i + '].destination.latitude=' + e.get('to').get('latitude') + '&';
                    query += 'data.parts[' + i + '].destination.longitude=' + e.get('to').get('longitude') + '&';
                    query += 'data.parts[' + i + '].containerType=' + containerType + '&';
                    query += 'data.parts[' + i + '].containerState=' + containerStatus + '&';
                    query += 'data.parts[' + i + '].routeType=' + routeType + '&';
                    i++;
                }
            });
            return query;
        },

        getRouteDetails: function (parts, callback) {
            var query = api + this.generateDetailsUrl(parts);

            if (parts.size() > 0) {
                req(query, function (data) {
                    if (data === undefined || data.response === undefined || data.response.route === undefined) {
                        reportError('invalid response');
                        return;
                    }
                    callback(data.response.route);
                }, 'der Dreiecksroutinganfrage', true, 'Routing must be checked. Please contact iris@contargo.net for further information.');
            }
        },

        geoCode: function (request, callback) {
            var url = geoCodeUrl + '?' + request.getQueryString();
            req(url, function (data) {
                if (data === undefined || data.geoCodeResponse === undefined
                    || data.geoCodeResponse.addresses === undefined) {
                    reportError('invalid response');
                    return;
                }
                callback(data.geoCodeResponse.addresses);
            }, 'der Adressauflösung');
        },

        countries: function (callback) {
            req(countriesUrl, function (data) {
                if (data === undefined || data.countriesResponse === undefined || data.countriesResponse.countries === undefined) {
                    reportError('invalid response');
                    return;
                }
                callback(data.countriesResponse.countries);
            }, 'dem Abrufen der möglichen Länder', false);
        },

        getActiveSeaports: function (callback) {
            req(seaPortsUrl, function (data) {
                if (data === undefined || data.seaports === undefined) {
                    reportError('invalid response');
                    return;
                }
                callback(data.seaports);
            }, 'dem Abrufen der Seehäfen', false);
        },

        terminals: function (callback) {
            req(terminalsUrl, function (data) {
                if (data === undefined || data.response === undefined || data.response.terminals === undefined) {
                    reportError('invalid response');
                    return;
                }
                callback(data.response.terminals);
            }, 'dem Abrufen der Terminals', false);
        }
    };
};
