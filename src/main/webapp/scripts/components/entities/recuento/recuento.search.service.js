'use strict';

angular.module('opendatacollectorApp')
    .factory('RecuentoSearch', function ($resource) {
        return $resource('api/_search/recuentos/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
