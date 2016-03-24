'use strict';

angular.module('opendatacollectorApp')
    .controller('BarrioController', function ($scope, $state, Barrio, BarrioSearch, ParseLinks) {

        $scope.barrios = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Barrio.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.barrios = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            BarrioSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.barrios = result;
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
            $scope.barrio = {
                nombre: null,
                nombreCsvOrigen: null,
                nombreCsvDestino: null,
                id: null
            };
        };
    });
