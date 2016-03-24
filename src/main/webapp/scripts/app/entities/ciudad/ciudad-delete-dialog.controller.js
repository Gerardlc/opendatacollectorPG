'use strict';

angular.module('opendatacollectorApp')
	.controller('CiudadDeleteController', function($scope, $uibModalInstance, entity, Ciudad) {

        $scope.ciudad = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Ciudad.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
