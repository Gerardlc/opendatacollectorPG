'use strict';

angular.module('opendatacollectorApp')
    .factory('MigracionBarrioSearch', function ($resource) {
        return $resource('api/_search/migracionBarrios/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
