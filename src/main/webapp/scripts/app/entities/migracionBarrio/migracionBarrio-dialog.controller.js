'use strict';

angular.module('opendatacollectorApp').controller('MigracionBarrioDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'MigracionBarrio', 'Barrio',
        function($scope, $stateParams, $uibModalInstance, entity, MigracionBarrio, Barrio) {

        $scope.migracionBarrio = entity;
        $scope.barrios = Barrio.query();
        $scope.load = function(id) {
            MigracionBarrio.get({id : id}, function(result) {
                $scope.migracionBarrio = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('opendatacollectorApp:migracionBarrioUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.migracionBarrio.id != null) {
                MigracionBarrio.update($scope.migracionBarrio, onSaveSuccess, onSaveError);
            } else {
                MigracionBarrio.save($scope.migracionBarrio, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
