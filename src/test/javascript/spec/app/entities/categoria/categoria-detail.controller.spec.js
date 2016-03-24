'use strict';

describe('Controller Tests', function() {

    describe('Categoria Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCategoria, MockSubCategoria, MockDataSet, MockCiudad;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCategoria = jasmine.createSpy('MockCategoria');
            MockSubCategoria = jasmine.createSpy('MockSubCategoria');
            MockDataSet = jasmine.createSpy('MockDataSet');
            MockCiudad = jasmine.createSpy('MockCiudad');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Categoria': MockCategoria,
                'SubCategoria': MockSubCategoria,
                'DataSet': MockDataSet,
                'Ciudad': MockCiudad
            };
            createController = function() {
                $injector.get('$controller')("CategoriaDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'opendatacollectorApp:categoriaUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
