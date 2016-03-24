'use strict';

angular.module('opendatacollectorApp')
    .factory('Barrio', function ($resource, DateUtils) {
        return $resource('api/barrios/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
