'use strict';

angular.module('opendatacollectorApp')
    .controller('SubCategoriaController', function ($scope, $state, SubCategoria, SubCategoriaSearch, ParseLinks) {

        $scope.subCategorias = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            SubCategoria.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.subCategorias = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            SubCategoriaSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.subCategorias = result;
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
            $scope.subCategoria = {
                nombre: null,
                descripcion: null,
                id: null
            };
        };
    });
