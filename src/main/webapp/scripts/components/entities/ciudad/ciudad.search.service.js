'use strict';

angular.module('opendatacollectorApp')
    .factory('CiudadSearch', function ($resource) {
        return $resource('api/_search/ciudads/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
