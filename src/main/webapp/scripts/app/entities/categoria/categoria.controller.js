'use strict';

angular.module('opendatacollectorApp')
    .controller('CategoriaController', function ($scope, $state, Categoria, CategoriaSearch, ParseLinks) {

        $scope.categorias = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Categoria.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.categorias = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            CategoriaSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.categorias = result;
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
            $scope.categoria = {
                nombre: null,
                descripcion: null,
                id: null
            };
        };
    });
