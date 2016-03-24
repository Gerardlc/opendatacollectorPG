'use strict';

angular.module('opendatacollectorApp')
    .factory('SubCategoriaSearch', function ($resource) {
        return $resource('api/_search/subCategorias/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
