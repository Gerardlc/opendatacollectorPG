'use strict';

describe('Controller Tests', function() {

    describe('Recuento Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRecuento;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRecuento = jasmine.createSpy('MockRecuento');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Recuento': MockRecuento
            };
            createController = function() {
                $injector.get('$controller')("RecuentoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'opendatacollectorApp:recuentoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
