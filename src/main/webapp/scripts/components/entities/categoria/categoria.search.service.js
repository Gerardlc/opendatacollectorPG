'use strict';

angular.module('opendatacollectorApp')
    .factory('CategoriaSearch', function ($resource) {
        return $resource('api/_search/categorias/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
