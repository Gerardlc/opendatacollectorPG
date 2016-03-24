'use strict';

angular.module('opendatacollectorApp').controller('DataSetDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'DataSet', 'Ciudad', 'Categoria', 'SubCategoria',
        function($scope, $stateParams, $uibModalInstance, entity, DataSet, Ciudad, Categoria, SubCategoria) {

        $scope.dataSet = entity;
        $scope.ciudads = Ciudad.query();
        $scope.categorias = Categoria.query();
        $scope.subcategorias = SubCategoria.query();
        $scope.load = function(id) {
            DataSet.get({id : id}, function(result) {
                $scope.dataSet = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('opendatacollectorApp:dataSetUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.dataSet.id != null) {
                DataSet.update($scope.dataSet, onSaveSuccess, onSaveError);
            } else {
                DataSet.save($scope.dataSet, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForFecha = {};

        $scope.datePickerForFecha.status = {
            opened: false
        };

        $scope.datePickerForFechaOpen = function($event) {
            $scope.datePickerForFecha.status.opened = true;
        };
}]);
