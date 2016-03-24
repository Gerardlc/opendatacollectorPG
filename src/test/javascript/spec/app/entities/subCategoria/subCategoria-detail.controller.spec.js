'use strict';

describe('Controller Tests', function() {

    describe('SubCategoria Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSubCategoria, MockCategoria, MockDataSet;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSubCategoria = jasmine.createSpy('MockSubCategoria');
            MockCategoria = jasmine.createSpy('MockCategoria');
            MockDataSet = jasmine.createSpy('MockDataSet');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SubCategoria': MockSubCategoria,
                'Categoria': MockCategoria,
                'DataSet': MockDataSet
            };
            createController = function() {
                $injector.get('$controller')("SubCategoriaDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'opendatacollectorApp:subCategoriaUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
