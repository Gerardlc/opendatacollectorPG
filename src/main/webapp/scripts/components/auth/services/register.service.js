'use strict';

angular.module('opendatacollectorApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


