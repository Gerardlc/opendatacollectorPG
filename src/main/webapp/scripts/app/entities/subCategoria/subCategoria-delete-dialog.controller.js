'use strict';

angular.module('opendatacollectorApp')
	.controller('SubCategoriaDeleteController', function($scope, $uibModalInstance, entity, SubCategoria) {

        $scope.subCategoria = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            SubCategoria.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
