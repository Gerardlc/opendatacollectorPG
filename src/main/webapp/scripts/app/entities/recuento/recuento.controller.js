'use strict';

angular.module('opendatacollectorApp')
    .controller('RecuentoController', function ($scope, $state, Recuento, RecuentoSearch) {

        $scope.recuentos = [];
        $scope.loadAll = function() {
            Recuento.query(function(result) {
               $scope.recuentos = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            RecuentoSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.recuentos = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.recuento = {
                id: null
            };
        };
    });
