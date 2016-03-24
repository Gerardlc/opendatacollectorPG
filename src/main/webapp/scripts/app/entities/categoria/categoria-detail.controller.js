'use strict';

angular.module('opendatacollectorApp')
    .controller('CategoriaDetailController', function ($scope, $rootScope, $stateParams, entity, Categoria, SubCategoria, DataSet, Ciudad) {
        $scope.categoria = entity;
        $scope.load = function (id) {
            Categoria.get({id: id}, function(result) {
                $scope.categoria = result;
            });
        };
        var unsubscribe = $rootScope.$on('opendatacollectorApp:categoriaUpdate', function(event, result) {
            $scope.categoria = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
