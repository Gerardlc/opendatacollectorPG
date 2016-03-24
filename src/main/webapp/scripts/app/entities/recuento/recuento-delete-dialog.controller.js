'use strict';

angular.module('opendatacollectorApp')
	.controller('RecuentoDeleteController', function($scope, $uibModalInstance, entity, Recuento) {

        $scope.recuento = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Recuento.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
