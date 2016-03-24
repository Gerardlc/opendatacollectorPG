'use strict';

angular.module('opendatacollectorApp')
	.controller('DataSetDeleteController', function($scope, $uibModalInstance, entity, DataSet) {

        $scope.dataSet = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            DataSet.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
