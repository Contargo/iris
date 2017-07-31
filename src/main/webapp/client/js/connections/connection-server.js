var ConnectionServer = function (contextpath) {
    'use strict';

    if (!contextpath) {
        throw 'need context path';
    }
    var api = contextpath + 'api/';
    var connectionsUrl = api + 'connections/';
    var connectionUrl = api + 'connections/{connectionId}';
    var seaportsUrl = api + 'seaports?activeOnly=false';
    var terminalsUrl = api + 'terminals?activeOnly=false';
    var typesUrl = api + 'connections/types';

    return {
        getConnection: function (connectionId, callback, errorCallback) {
            get(connectionUrl.replace('{connectionId}', connectionId),
                function (responseData) {
                    callback(responseData);
                },
                function () {
                    errorCallback('Could not find connection with id ' + connectionId);
                }
            );
        },
        getSeaports: function (callback, errorCallback) {
            get(seaportsUrl, function (response) {
                    callback(response.seaports);
                }, function () {
                    errorCallback('Error retrieving seaports');
                }
            );
        },
        getTerminals: function (callback, errorCallback) {
            get(terminalsUrl, function (response) {
                    callback(response.response.terminals);
                }, function () {
                    errorCallback('Error retrieving terminals');
                }
            );
        },
        getTypes: function (callback, errorCallback) {
            get(typesUrl, function (response) {
                callback(response);
            }, function () {
                errorCallback('Error retrieving types');
            })
        },
        updateConnection: function (connection, callback, errorCallback) {
            put(connectionUrl.replace('{connectionId}', connection.id), connection,
                function (responseData) {
                    callback(responseData);
                },
                function (responseData) {
                    errorCallback(responseData);
                }
            );
        },
        createConnection: function (connection, callback, errorCallback) {
            post(connectionsUrl, connection,
                function (responseData, status, request) {
                    callback(request.getResponseHeader('location'));
                },
                function (responseData) {
                    errorCallback(responseData);
                }
            );
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

    function put(url, content, success, error) {
        $.ajax({
            type: 'PUT',
            url: url,
            contentType: 'application/json',
            data: JSON.stringify(content),
            dataType: 'json',
            cache: false,
            async: true,
            success: success,
            error: error
        });
    }

    function post(url, content, success, error) {
        $.ajax({
            type: 'POST',
            url: url,
            contentType: 'application/json',
            data: JSON.stringify(content),
            dataType: 'json',
            cache: false,
            async: true,
            success: success,
            error: error
        });
    }
};