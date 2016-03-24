'use strict';

angular.module('opendatacollectorApp')
    .factory('Ciudad', function ($resource, DateUtils) {
        return $resource('api/ciudads/:id', {}, {
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
