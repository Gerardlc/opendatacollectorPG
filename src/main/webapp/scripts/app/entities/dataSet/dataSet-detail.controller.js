'use strict';

angular.module('opendatacollectorApp')
    .controller('DataSetDetailController', function ($scope, $rootScope, $stateParams, entity, DataSet, Ciudad, Categoria, SubCategoria) {
        $scope.dataSet = entity;
        $scope.load = function (id) {
            DataSet.get({id: id}, function(result) {
                $scope.dataSet = result;
            });
        };
        var unsubscribe = $rootScope.$on('opendatacollectorApp:dataSetUpdate', function(event, result) {
            $scope.dataSet = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
