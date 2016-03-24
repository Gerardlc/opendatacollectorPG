'use strict';

angular.module('opendatacollectorApp')
    .controller('DataSetController', function ($scope, $state, DataSet, DataSetSearch, ParseLinks) {

        $scope.dataSets = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            DataSet.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.dataSets = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            DataSetSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.dataSets = result;
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
            $scope.dataSet = {
                nombre: null,
                descripcion: null,
                fecha: null,
                enlaceDescarga: null,
                id: null
            };
        };
    });
