'use strict';

describe('Controller Tests', function() {

    describe('DataSet Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDataSet, MockCiudad, MockCategoria, MockSubCategoria;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDataSet = jasmine.createSpy('MockDataSet');
            MockCiudad = jasmine.createSpy('MockCiudad');
            MockCategoria = jasmine.createSpy('MockCategoria');
            MockSubCategoria = jasmine.createSpy('MockSubCategoria');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'DataSet': MockDataSet,
                'Ciudad': MockCiudad,
                'Categoria': MockCategoria,
                'SubCategoria': MockSubCategoria
            };
            createController = function() {
                $injector.get('$controller')("DataSetDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'opendatacollectorApp:dataSetUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
