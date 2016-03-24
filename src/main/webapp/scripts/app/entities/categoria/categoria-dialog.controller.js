'use strict';

angular.module('opendatacollectorApp').controller('CategoriaDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Categoria', 'SubCategoria', 'DataSet', 'Ciudad',
        function($scope, $stateParams, $uibModalInstance, entity, Categoria, SubCategoria, DataSet, Ciudad) {

        $scope.categoria = entity;
        $scope.subcategorias = SubCategoria.query();
        $scope.datasets = DataSet.query();
        $scope.ciudads = Ciudad.query();
        $scope.load = function(id) {
            Categoria.get({id : id}, function(result) {
                $scope.categoria = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('opendatacollectorApp:categoriaUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.categoria.id != null) {
                Categoria.update($scope.categoria, onSaveSuccess, onSaveError);
            } else {
                Categoria.save($scope.categoria, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
