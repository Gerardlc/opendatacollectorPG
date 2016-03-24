'use strict';

angular.module('opendatacollectorApp')
    .controller('SubCategoriaDetailController', function ($scope, $rootScope, $stateParams, entity, SubCategoria, Categoria, DataSet) {
        $scope.subCategoria = entity;
        $scope.load = function (id) {
            SubCategoria.get({id: id}, function(result) {
                $scope.subCategoria = result;
            });
        };
        var unsubscribe = $rootScope.$on('opendatacollectorApp:subCategoriaUpdate', function(event, result) {
            $scope.subCategoria = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
