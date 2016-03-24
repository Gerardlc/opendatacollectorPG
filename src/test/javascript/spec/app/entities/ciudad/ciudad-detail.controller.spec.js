'use strict';

describe('Controller Tests', function() {

    describe('Ciudad Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCiudad, MockCategoria, MockDataSet;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCiudad = jasmine.createSpy('MockCiudad');
            MockCategoria = jasmine.createSpy('MockCategoria');
            MockDataSet = jasmine.createSpy('MockDataSet');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Ciudad': MockCiudad,
                'Categoria': MockCategoria,
                'DataSet': MockDataSet
            };
            createController = function() {
                $injector.get('$controller')("CiudadDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'opendatacollectorApp:ciudadUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
