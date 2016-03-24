'use strict';

angular.module('opendatacollectorApp').controller('CiudadDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ciudad', 'Categoria', 'DataSet',
        function($scope, $stateParams, $uibModalInstance, entity, Ciudad, Categoria, DataSet) {

        $scope.ciudad = entity;
        $scope.categorias = Categoria.query();
        $scope.datasets = DataSet.query();
        $scope.load = function(id) {
            Ciudad.get({id : id}, function(result) {
                $scope.ciudad = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('opendatacollectorApp:ciudadUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.ciudad.id != null) {
                Ciudad.update($scope.ciudad, onSaveSuccess, onSaveError);
            } else {
                Ciudad.save($scope.ciudad, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
