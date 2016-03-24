'use strict';

angular.module('opendatacollectorApp')
    .controller('CiudadController', function ($scope, $state, Ciudad, CiudadSearch, ParseLinks) {

        $scope.ciudads = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Ciudad.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.ciudads = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            CiudadSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.ciudads = result;
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
            $scope.ciudad = {
                nombre: null,
                pais: null,
                id: null
            };
        };
    });
