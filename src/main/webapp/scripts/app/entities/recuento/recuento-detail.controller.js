'use strict';

angular.module('opendatacollectorApp')
    .controller('RecuentoDetailController', function ($scope, $rootScope, $stateParams, entity, Recuento) {
        $scope.recuento = entity;
        $scope.load = function (id) {
            Recuento.get({id: id}, function(result) {
                $scope.recuento = result;
            });
        };
        var unsubscribe = $rootScope.$on('opendatacollectorApp:recuentoUpdate', function(event, result) {
            $scope.recuento = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
