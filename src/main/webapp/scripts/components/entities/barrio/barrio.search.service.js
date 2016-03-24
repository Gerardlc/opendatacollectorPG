'use strict';

angular.module('opendatacollectorApp')
    .factory('BarrioSearch', function ($resource) {
        return $resource('api/_search/barrios/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
