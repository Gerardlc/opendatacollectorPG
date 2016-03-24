 'use strict';

angular.module('opendatacollectorApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-opendatacollectorApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-opendatacollectorApp-params')});
                }
                return response;
            }
        };
    });
