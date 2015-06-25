var ConnectionServer = function (contextpath) {
    'use strict';

    if (!contextpath) {
        throw 'need context path';
    }

    var api = contextpath + 'api/';
    var connectionUrl = api + 'connections/{connectionId}';
    var seaportsUrl = api + 'seaports';
    var terminalsUrl = api + 'terminals';

    return {
        getConnection: function (connectionId, callback) {
            get(connectionUrl.replace('{connectionId}', connectionId),
                function (responseData) {
                    callback(responseData);
                },
                function () {
                    alert('fehler');//TODO
                }
            );
        },
        getSeaports: function (callback) {
            get(seaportsUrl, function (response) {
                callback(response.seaports)
            }, function () {
                    alert('fehler');//TODO
                }
            )
        },
        getTerminals: function (callback) {
            get(terminalsUrl, function (response) {
                callback(response.response.terminals)
            }, function () {
                    alert('fehler');//TODO
                }
            )
        }
    };

    function get(url, success, error) {
        $.ajax({
            url: url,
            dataType: 'json',
            cache: false,
            async: true,
            success: success,
            error: error
        });
    }
};
