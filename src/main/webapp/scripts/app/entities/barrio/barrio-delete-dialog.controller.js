'use strict';

angular.module('opendatacollectorApp')
	.controller('BarrioDeleteController', function($scope, $uibModalInstance, entity, Barrio) {

        $scope.barrio = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Barrio.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
