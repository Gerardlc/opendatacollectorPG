'use strict';

angular.module('opendatacollectorApp').controller('BarrioDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Barrio', 'MigracionBarrio',
        function($scope, $stateParams, $uibModalInstance, entity, Barrio, MigracionBarrio) {

        $scope.barrio = entity;
        $scope.migracionbarrios = MigracionBarrio.query();
        $scope.load = function(id) {
            Barrio.get({id : id}, function(result) {
                $scope.barrio = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('opendatacollectorApp:barrioUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.barrio.id != null) {
                Barrio.update($scope.barrio, onSaveSuccess, onSaveError);
            } else {
                Barrio.save($scope.barrio, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
