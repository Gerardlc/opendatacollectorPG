'use strict';

angular.module('opendatacollectorApp')
    .factory('DataSetSearch', function ($resource) {
        return $resource('api/_search/dataSets/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
