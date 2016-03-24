'use strict';

angular.module('opendatacollectorApp').controller('SubCategoriaDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'SubCategoria', 'Categoria', 'DataSet',
        function($scope, $stateParams, $uibModalInstance, entity, SubCategoria, Categoria, DataSet) {

        $scope.subCategoria = entity;
        $scope.categorias = Categoria.query();
        $scope.datasets = DataSet.query();
        $scope.load = function(id) {
            SubCategoria.get({id : id}, function(result) {
                $scope.subCategoria = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('opendatacollectorApp:subCategoriaUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.subCategoria.id != null) {
                SubCategoria.update($scope.subCategoria, onSaveSuccess, onSaveError);
            } else {
                SubCategoria.save($scope.subCategoria, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
