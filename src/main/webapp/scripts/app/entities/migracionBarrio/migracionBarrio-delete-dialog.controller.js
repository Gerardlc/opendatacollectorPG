'use strict';

angular.module('opendatacollectorApp')
	.controller('MigracionBarrioDeleteController', function($scope, $uibModalInstance, entity, MigracionBarrio) {

        $scope.migracionBarrio = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MigracionBarrio.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
