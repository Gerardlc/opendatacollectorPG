'use strict';

angular.module('opendatacollectorApp')
    .controller('MigracionBarrioController', function ($scope, $state, MigracionBarrio, MigracionBarrioSearch, ParseLinks) {

        $scope.migracionBarrios = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            MigracionBarrio.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.migracionBarrios = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            MigracionBarrioSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.migracionBarrios = result;
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
            $scope.migracionBarrio = {
                numeroPersonas: null,
                anyo: null,
                id: null
            };
        };
    });
