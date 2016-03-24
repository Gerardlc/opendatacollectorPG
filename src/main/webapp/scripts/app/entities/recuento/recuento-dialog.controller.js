'use strict';

angular.module('opendatacollectorApp').controller('RecuentoDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Recuento',
        function($scope, $stateParams, $uibModalInstance, entity, Recuento) {

        $scope.recuento = entity;
        $scope.load = function(id) {
            Recuento.get({id : id}, function(result) {
                $scope.recuento = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('opendatacollectorApp:recuentoUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.recuento.id != null) {
                Recuento.update($scope.recuento, onSaveSuccess, onSaveError);
            } else {
                Recuento.save($scope.recuento, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
